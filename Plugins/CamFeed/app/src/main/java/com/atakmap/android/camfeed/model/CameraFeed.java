package com.atakmap.android.camfeed.model;

/**
 * Represents a single public camera feed discovered from any source.
 */
public class CameraFeed {

    public enum StreamType {
        RTSP,    // rtsp:// — opens in ATAK native video tool
        MJPEG,   // http(s)://...mjpg — opens in ATAK native video tool
        HLS,     // http(s)://...m3u8 — opens in ATAK native video tool
        WEB,     // http(s):// web page — opens in WebView
        EMBED,   // Windy/EarthCam embed URL — opens in WebView
        UNKNOWN  // No stream known; marker only
    }

    public String uid;          // "camfeed-{source}-{id}"
    public String name;         // Human-readable label
    public double latitude;
    public double longitude;
    public String source;       // "windy" | "osm" | "kmz"
    public String streamUrl;    // The stream / embed URL (may be null)
    public StreamType streamType;
    public String thumbnailUrl; // Preview image URL (may be null)
    public String city;
    public String country;
    public boolean isActive;    // Whether the feed is currently reachable (best-effort)
    public long fetchedAt;      // Epoch ms when this entry was fetched

    public CameraFeed() {
        this.streamType = StreamType.UNKNOWN;
        this.isActive = true;
        this.fetchedAt = System.currentTimeMillis();
    }

    /**
     * Infer StreamType from a URL string.
     */
    public static StreamType detectStreamType(String url) {
        if (url == null || url.isEmpty()) return StreamType.UNKNOWN;
        String lower = url.toLowerCase();
        if (lower.startsWith("rtsp://"))                          return StreamType.RTSP;
        if (lower.contains(".mjpg") || lower.contains(".mjpeg")) return StreamType.MJPEG;
        if (lower.contains(".m3u8"))                              return StreamType.HLS;
        if (lower.contains("windy.com") || lower.contains("webcams.windy")) return StreamType.EMBED;
        if (lower.startsWith("http://") || lower.startsWith("https://")) return StreamType.WEB;
        return StreamType.UNKNOWN;
    }

    /** True if ATAK's native video tool can handle this stream. */
    public boolean isNativeStream() {
        return streamType == StreamType.RTSP
            || streamType == StreamType.MJPEG
            || streamType == StreamType.HLS;
    }

    public String getSourceLabel() {
        if (source == null) return "UNK";
        switch (source) {
            case "windy": return "WIN";
            case "osm":   return "OSM";
            case "kmz":   return "KMZ";
            default:      return source.toUpperCase().substring(0, Math.min(3, source.length()));
        }
    }

    public int getSourceColor() {
        if (source == null) return 0xFF888888;
        switch (source) {
            case "windy": return 0xFF00BFFF;
            case "osm":   return 0xFF77DD77;
            case "kmz":   return 0xFFFFAA00;
            default:      return 0xFF888888;
        }
    }

    @Override
    public String toString() {
        return "CameraFeed{uid=" + uid + ", name=" + name
            + ", source=" + source + ", type=" + streamType + "}";
    }
}
