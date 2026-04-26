package com.atakmap.android.camfeed.cot;

import com.atakmap.android.camfeed.model.CameraFeed;
import com.atakmap.coremap.log.Log;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Builds CoT (Cursor-on-Target) XML events for camera feeds.
 *
 * For native streams (RTSP/MJPEG/HLS), the CoT includes a <__video> element
 * with a ConnectionEntry so ATAK's video tool can open the stream directly.
 *
 * For web/embed streams, the stream URL is stored in <remarks> and opened
 * via WebView from the drop-down.
 *
 * CoT type "b-m-p-s-m" = sensor/spot marker (standard for fixed sensors).
 */
public class CameraCoTBuilder {

    private static final String TAG = "CameraCoTBuilder";

    /** Stale after 24 hours — cameras don't move. */
    private static final long STALE_MS = 24 * 60 * 60 * 1000L;

    private static final SimpleDateFormat SDF;
    static {
        SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        SDF.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String buildCot(CameraFeed cam) {
        if (cam == null || cam.uid == null) return null;
        try {
            long now = System.currentTimeMillis();
            String nowStr   = SDF.format(new Date(now));
            String staleStr = SDF.format(new Date(now + STALE_MS));

            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version='1.0' encoding='UTF-8'?>")
              .append("<event version='2.0'")
              .append(" uid='").append(escape(cam.uid)).append("'")
              .append(" type='a-u-G'")
              .append(" time='").append(nowStr).append("'")
              .append(" start='").append(nowStr).append("'")
              .append(" stale='").append(staleStr).append("'")
              .append(" how='m-g'>")
              .append("<point lat='").append(cam.latitude).append("'")
              .append(" lon='").append(cam.longitude).append("'")
              .append(" hae='0' ce='9999999' le='9999999'/>")
              .append("<detail>")
              .append("<contact callsign='").append(escape(cam.name)).append("'/>");

            // Build remarks
            StringBuilder remarks = new StringBuilder();
            remarks.append("SRC:").append(cam.source.toUpperCase());
            if (cam.city != null && !cam.city.isEmpty()) {
                remarks.append(" | ").append(cam.city);
            }
            if (cam.country != null && !cam.country.isEmpty()) {
                remarks.append(", ").append(cam.country);
            }
            if (cam.streamUrl != null && !cam.streamUrl.isEmpty()) {
                remarks.append(" | TYPE:").append(cam.streamType.name());
                remarks.append(" | URL:").append(cam.streamUrl);
            }
            sb.append("<remarks>").append(escape(remarks.toString())).append("</remarks>");

            // For native streams, add __video ConnectionEntry so ATAK video tool works
            if (cam.isNativeStream() && cam.streamUrl != null) {
                appendVideoEntry(sb, cam);
            }

            sb.append("</detail>")
              .append("</event>");

            return sb.toString();

        } catch (Exception e) {
            Log.e(TAG, "buildCot failed for " + cam.uid, e);
            return null;
        }
    }

    /**
     * Appends the <__video> block for RTSP/MJPEG/HLS streams.
     * ATAK's video tool reads ConnectionEntry to open the stream.
     */
    private static void appendVideoEntry(StringBuilder sb, CameraFeed cam) {
        try {
            URI uri = new URI(cam.streamUrl);
            String protocol = resolveProtocol(cam.streamType, uri.getScheme());
            String address  = uri.getHost() != null ? uri.getHost() : "";
            int    port     = uri.getPort() > 0 ? uri.getPort() : defaultPort(cam.streamType);
            String path     = uri.getPath() != null ? uri.getPath() : "";
            if (uri.getQuery() != null) path += "?" + uri.getQuery();

            sb.append("<__video>")
              .append("<ConnectionEntry")
              .append(" uid='").append(escape(cam.uid)).append("'")
              .append(" alias='").append(escape(cam.name)).append("'")
              .append(" address='").append(escape(address)).append("'")
              .append(" port='").append(port).append("'")
              .append(" path='").append(escape(path)).append("'")
              .append(" protocol='").append(protocol).append("'")
              .append(" bufferTime='-1'")
              .append(" networkTimeout='12000'")
              .append(" rtspReliable='0'")
              .append(" ignoreEmbeddedKLV='false'")
              .append(" connectionEntry=''/>")
              .append("</__video>");
        } catch (Exception e) {
            Log.w(TAG, "Could not parse stream URL for video entry: " + cam.streamUrl, e);
        }
    }

    private static String resolveProtocol(CameraFeed.StreamType type, String uriScheme) {
        switch (type) {
            case RTSP:  return "rtsp";
            case MJPEG: return "http";
            case HLS:   return "http";
            default:    return uriScheme != null ? uriScheme : "http";
        }
    }

    private static int defaultPort(CameraFeed.StreamType type) {
        switch (type) {
            case RTSP:  return 554;
            case MJPEG: return 80;
            case HLS:   return 80;
            default:    return 80;
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("'", "&apos;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
