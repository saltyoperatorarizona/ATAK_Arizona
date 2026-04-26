package com.atakmap.android.camfeed.source;

import com.atakmap.android.camfeed.model.CameraFeed;
import com.atakmap.coremap.log.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Fetches surveillance camera nodes from OpenStreetMap via the Overpass API.
 *
 * Queries for nodes tagged man_made=surveillance that are publicly visible.
 * OSM cameras often lack stream URLs — they provide location and type info only.
 * When skipNoStream is enabled, nodes without a stream URL are omitted.
 */
public class OsmSource implements CameraSource {

    private static final String TAG = "OsmSource";

    private static final String DEFAULT_OVERPASS_URL = "https://overpass-api.de/api/interpreter";

    /** Hard cap on results to avoid crushing the public Overpass instance. */
    private static final int MAX_NODE_LIMIT = 500;

    private final OkHttpClient http;
    private String overpassUrl;
    private boolean skipNoStream;

    public OsmSource(OkHttpClient http, String overpassUrl, boolean skipNoStream) {
        this.http = http;
        this.overpassUrl = (overpassUrl != null && !overpassUrl.isEmpty())
            ? overpassUrl : DEFAULT_OVERPASS_URL;
        this.skipNoStream = skipNoStream;
    }

    public void setOverpassUrl(String url) {
        this.overpassUrl = (url != null && !url.isEmpty()) ? url : DEFAULT_OVERPASS_URL;
    }

    public void setSkipNoStream(boolean skipNoStream) {
        this.skipNoStream = skipNoStream;
    }

    @Override
    public String getName() {
        return "OpenStreetMap";
    }

    /** Fallback Overpass endpoints tried in order if the primary returns 5xx. */
    private static final String[] FALLBACK_URLS = {
        "https://overpass.kumi.systems/api/interpreter",
        "https://overpass.openstreetmap.ru/api/interpreter"
    };

    @Override
    public void fetchCameras(double lat, double lon, int radiusKm, Callback callback) {
        // Single-branch query: all man_made=surveillance nodes in radius.
        // Avoids 504 caused by 4 separate union+around clauses on public servers.
        int radiusM = radiusKm * 1000;
        String query = "[out:json][timeout:25];"
            + "node[\"man_made\"=\"surveillance\"]"
            + "(around:" + radiusM + "," + lat + "," + lon + ");"
            + "out body " + MAX_NODE_LIMIT + ";";

        String encodedQuery;
        try {
            encodedQuery = "data=" + java.net.URLEncoder.encode(query, "UTF-8");
        } catch (Exception e) {
            callback.onError("OSM query encode error: " + e.getMessage());
            return;
        }
        MediaType formType = MediaType.parse("application/x-www-form-urlencoded");

        // Build endpoint list: configured URL first, then fallbacks
        List<String> endpoints = new ArrayList<>();
        endpoints.add(overpassUrl);
        for (String fb : FALLBACK_URLS) {
            if (!fb.equals(overpassUrl)) endpoints.add(fb);
        }

        IOException lastError = null;
        for (String endpoint : endpoints) {
            RequestBody requestBody = RequestBody.create(encodedQuery, formType);
            Request request = new Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .header("Accept", "application/json")
                .build();

            try (Response response = http.newCall(request).execute()) {
                if (response.code() == 504 || response.code() == 502 || response.code() == 503) {
                    Log.w(TAG, "OSM endpoint " + endpoint + " returned " + response.code() + ", trying next");
                    lastError = new IOException("HTTP " + response.code());
                    continue;
                }
                if (!response.isSuccessful()) {
                    callback.onError("OSM Overpass HTTP " + response.code());
                    return;
                }

                okhttp3.ResponseBody responseBodyObj = response.body();
                String responseBody = responseBodyObj != null ? responseBodyObj.string() : "";
                if (responseBody.isEmpty()) {
                    callback.onSuccess(new ArrayList<>());
                    return;
                }

                List<CameraFeed> cameras = parseResponse(responseBody);
                Log.d(TAG, "OSM: parsed " + cameras.size() + " cameras via " + endpoint);
                callback.onSuccess(cameras);
                return;

            } catch (Exception e) {
                Log.w(TAG, "OSM endpoint " + endpoint + " failed: " + e.getMessage());
                lastError = (e instanceof IOException) ? (IOException) e : new IOException(e.getMessage());
            }
        }

        // All endpoints failed
        String msg = lastError != null ? lastError.getMessage() : "all endpoints failed";
        Log.e(TAG, "OSM fetch failed on all endpoints");
        callback.onError("OSM error: " + msg);
    }

    private List<CameraFeed> parseResponse(String json) throws Exception {
        List<CameraFeed> cameras = new ArrayList<>();
        JSONObject root = new JSONObject(json);
        JSONArray elements = root.optJSONArray("elements");
        if (elements == null) return cameras;

        for (int i = 0; i < elements.length(); i++) {
            try {
                JSONObject el = elements.getJSONObject(i);
                if (!"node".equals(el.optString("type"))) continue;

                double lat = el.optDouble("lat", Double.NaN);
                double lon = el.optDouble("lon", Double.NaN);
                if (Double.isNaN(lat) || Double.isNaN(lon)) continue;

                long nodeId = el.optLong("id", 0);
                JSONObject tags = el.optJSONObject("tags");

                CameraFeed cam = new CameraFeed();
                cam.uid = "camfeed-osm-" + nodeId;
                cam.source = "osm";
                cam.latitude = lat;
                cam.longitude = lon;

                if (tags != null) {
                    // Try to get a useful name from various tags
                    cam.name = firstNonEmpty(
                        tags.optString("name", ""),
                        tags.optString("description", ""),
                        tags.optString("operator", ""),
                        tags.optString("ref", "")
                    );

                    // Check for a stream URL embedded in tags
                    String streamUrl = firstNonEmpty(
                        tags.optString("url", ""),
                        tags.optString("contact:webcam", ""),
                        tags.optString("website", "")
                    );
                    if (!streamUrl.isEmpty()) {
                        cam.streamUrl = streamUrl;
                        cam.streamType = CameraFeed.detectStreamType(streamUrl);
                    }

                    cam.city = tags.optString("addr:city", "");
                    cam.country = tags.optString("addr:country", "");
                }

                if (cam.name.isEmpty()) {
                    cam.name = "OSM Camera #" + nodeId;
                }

                // Optionally skip if no stream URL
                if (skipNoStream && (cam.streamUrl == null || cam.streamUrl.isEmpty())) {
                    continue;
                }

                cameras.add(cam);
            } catch (Exception e) {
                Log.w(TAG, "Skipping OSM element at index " + i + ": " + e.getMessage());
            }
        }

        return cameras;
    }

    /** Return first non-empty string from the candidates, or "" if all empty. */
    private static String firstNonEmpty(String... candidates) {
        for (String s : candidates) {
            if (s != null && !s.trim().isEmpty()) return s.trim();
        }
        return "";
    }
}
