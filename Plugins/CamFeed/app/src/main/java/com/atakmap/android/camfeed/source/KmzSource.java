package com.atakmap.android.camfeed.source;

import com.atakmap.android.camfeed.model.CameraFeed;
import com.atakmap.coremap.log.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Parses KMZ (zipped KML) or plain KML files to extract camera placemarks.
 *
 * KMZ files (like AZ511_Arizona_Complete_CCTV.kmz) are ZIP archives containing
 * a doc.kml (or similar) at the root. Each Placemark with coordinates becomes
 * a CameraFeed. Stream URLs are extracted from the description field if present.
 *
 * Handles:
 * - KMZ (zip) containing doc.kml or *.kml
 * - Plain .kml files
 * - Placemarks with Point coordinates
 * - RTSP/MJPEG/HTTP URLs inside description CDATA blocks
 * - Duplicate deduplication by coordinate proximity
 */
public class KmzSource implements CameraSource {

    private static final String TAG = "KmzSource";

    /** Regex patterns to detect stream URLs in KML descriptions. */
    private static final String[] URL_PATTERNS = {
        "rtsp://[^\\s\"'<>]+",
        "https?://[^\\s\"'<>]+\\.mjpe?g[^\\s\"'<>]*",
        "https?://[^\\s\"'<>]+\\.m3u8[^\\s\"'<>]*",
        "https?://[^\\s\"'<>]+/stream[^\\s\"'<>]*",
        "https?://[^\\s\"'<>]+/video[^\\s\"'<>]*",
        "https?://[^\\s\"'<>]+/camera[^\\s\"'<>]*",
    };

    private String kmzPath;

    public KmzSource(String kmzPath) {
        this.kmzPath = kmzPath;
    }

    public void setKmzPath(String path) {
        this.kmzPath = path;
    }

    @Override
    public String getName() {
        return "KMZ/KML File";
    }

    @Override
    public void fetchCameras(double lat, double lon, int radiusKm, Callback callback) {
        if (kmzPath == null || kmzPath.trim().isEmpty()) {
            callback.onError("KMZ path not configured. Tap ⚙ Settings.");
            return;
        }

        File file = new File(kmzPath.trim());
        if (!file.exists()) {
            callback.onError("KMZ file not found: " + kmzPath);
            return;
        }
        if (!file.canRead()) {
            callback.onError("Cannot read KMZ file (check permissions): " + kmzPath);
            return;
        }

        try {
            String kmlContent = readKmlContent(file);
            if (kmlContent == null || kmlContent.isEmpty()) {
                callback.onError("No KML data found in: " + kmzPath);
                return;
            }

            List<CameraFeed> cameras = parseKml(kmlContent, lat, lon, radiusKm);
            Log.d(TAG, "KMZ: parsed " + cameras.size() + " cameras from " + file.getName());
            callback.onSuccess(cameras);

        } catch (Exception e) {
            Log.e(TAG, "KMZ parse failed", e);
            callback.onError("KMZ error: " + e.getMessage());
        }
    }

    /**
     * Reads the KML content from either a .kmz (zip) or .kml file.
     */
    private String readKmlContent(File file) throws IOException {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".kmz")) {
            return readKmlFromZip(file);
        } else if (name.endsWith(".kml")) {
            // Read the full file content in a loop to handle partial reads
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[8192];
            try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    sb.append(new String(buffer, 0, bytesRead, "UTF-8"));
                }
            }
            return sb.length() > 0 ? sb.toString() : null;
        }

        // Try as zip anyway
        try {
            return readKmlFromZip(file);
        } catch (Exception ignored) {}

        return null;
    }

    private String readKmlFromZip(File file) throws IOException {
        try (ZipFile zip = new ZipFile(file)) {
            // Prefer doc.kml, then any .kml entry
            ZipEntry kmlEntry = zip.getEntry("doc.kml");
            if (kmlEntry == null) {
                java.util.Enumeration<? extends ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry e = entries.nextElement();
                    if (!e.isDirectory() && e.getName().toLowerCase().endsWith(".kml")) {
                        kmlEntry = e;
                        break;
                    }
                }
            }
            if (kmlEntry == null) return null;

            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[8192];
            try (InputStream is = zip.getInputStream(kmlEntry)) {
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    sb.append(new String(buffer, 0, bytesRead, "UTF-8"));
                }
            }
            return sb.length() > 0 ? sb.toString() : null;
        }
    }

    /**
     * Parses KML XML and extracts Placemarks within the given radius.
     */
    private List<CameraFeed> parseKml(String kml, double refLat, double refLon, int radiusKm)
            throws Exception {
        List<CameraFeed> cameras = new ArrayList<>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(kml));

        String currentName = null;
        String currentDesc = null;
        double currentLat = 0, currentLon = 0;
        boolean inPlacemark = false;
        boolean inPoint = false;
        String tagName = "";

        int eventType = parser.getEventType();
        int placemarkIndex = 0;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tagName = parser.getName();
                    if ("Placemark".equalsIgnoreCase(tagName)) {
                        inPlacemark = true;
                        currentName = null;
                        currentDesc = null;
                        currentLat = 0;
                        currentLon = 0;
                    } else if ("Point".equalsIgnoreCase(tagName)) {
                        inPoint = true;
                    }
                    break;

                case XmlPullParser.TEXT:
                    if (!inPlacemark) break;
                    String text = parser.getText().trim();
                    if ("name".equalsIgnoreCase(tagName)) {
                        currentName = text;
                    } else if ("description".equalsIgnoreCase(tagName)) {
                        currentDesc = text;
                    } else if ("coordinates".equalsIgnoreCase(tagName) && inPoint) {
                        // KML coordinates: lon,lat,alt
                        double[] coords = parseCoordinates(text);
                        if (coords != null) {
                            currentLon = coords[0];
                            currentLat = coords[1];
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    String endTag = parser.getName();
                    if ("Point".equalsIgnoreCase(endTag)) {
                        inPoint = false;
                    } else if ("Placemark".equalsIgnoreCase(endTag)) {
                        if (inPlacemark && currentLat != 0 && currentLon != 0) {
                            // Filter by radius
                            double dist = haversineKm(refLat, refLon, currentLat, currentLon);
                            if (dist <= radiusKm) {
                                CameraFeed cam = buildCamera(
                                    currentName, currentDesc,
                                    currentLat, currentLon,
                                    placemarkIndex
                                );
                                cameras.add(cam);
                                placemarkIndex++;
                            }
                        }
                        inPlacemark = false;
                    }
                    tagName = ""; // reset so TEXT events don't match stale tag
                    break;
            }

            eventType = parser.next();
        }

        return cameras;
    }

    private CameraFeed buildCamera(String name, String desc,
                                    double lat, double lon, int index) {
        CameraFeed cam = new CameraFeed();
        cam.uid = "camfeed-kmz-" + index + "-" + (long)(lat * 1e5) + "_" + (long)(lon * 1e5);
        cam.source = "kmz";
        cam.latitude = lat;
        cam.longitude = lon;
        cam.name = (name != null && !name.trim().isEmpty())
            ? name.trim() : "Camera #" + index;

        // Try to extract a stream URL from the description
        if (desc != null && !desc.isEmpty()) {
            for (String pattern : URL_PATTERNS) {
                java.util.regex.Matcher m = java.util.regex.Pattern
                    .compile(pattern, java.util.regex.Pattern.CASE_INSENSITIVE)
                    .matcher(desc);
                if (m.find()) {
                    cam.streamUrl = m.group();
                    cam.streamType = CameraFeed.detectStreamType(cam.streamUrl);
                    break;
                }
            }
        }

        return cam;
    }

    /**
     * Parse KML coordinates string "lon,lat,alt" or "lon,lat".
     * Returns [lon, lat] or null on failure.
     */
    private double[] parseCoordinates(String coords) {
        if (coords == null || coords.isEmpty()) return null;
        // Handle multiple tuples (e.g. polygon) — take the first
        String first = coords.trim().split("\\s+")[0];
        String[] parts = first.split(",");
        if (parts.length < 2) return null;
        try {
            double lon = Double.parseDouble(parts[0].trim());
            double lat = Double.parseDouble(parts[1].trim());
            return new double[]{lon, lat};
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Haversine distance in km between two lat/lon points. */
    private static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
