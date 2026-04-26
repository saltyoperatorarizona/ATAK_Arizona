package com.atakmap.android.camfeed.source;

import com.atakmap.android.camfeed.model.CameraFeed;
import com.atakmap.coremap.log.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fetches public webcams from the Windy Webcams API v3.
 *
 * API docs: https://api.windy.com/webcams/api/v3
 * Free tier: requires registration at windy.com/webcams/api
 *
 * Pagination: Windy returns up to 50 per page; we fetch pages until all are loaded
 *             or we hit the MAX_RESULTS limit to stay within rate limits.
 */
public class WindySource implements CameraSource {

    private static final String TAG = "WindySource";

    /** Max cameras to fetch in a single plugin refresh (avoids rate-limit bans). */
    private static final int MAX_RESULTS = 500;

    /** Windy API base URL for the webcams endpoint. */
    private static final String BASE_URL = "https://api.windy.com/webcams/api/v3/webcams";

    private final OkHttpClient http;
    private String apiKey;

    public WindySource(OkHttpClient http, String apiKey) {
        this.http = http;
        this.apiKey = apiKey;
    }

    public void setApiKey(String key) {
        this.apiKey = key;
    }

    @Override
    public String getName() {
        return "Windy Webcams";
    }

    @Override
    public void fetchCameras(double lat, double lon, int radiusKm, Callback callback) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            callback.onError("Windy API key not configured. Tap ⚙ Settings to add one.");
            return;
        }

        List<CameraFeed> allCameras = new ArrayList<>();
        int offset = 0;
        int limit = 50; // Windy max per page

        try {
            while (allCameras.size() < MAX_RESULTS) {
                // Windy v3 API: radius is in km, max 250
                int clampedRadius = Math.min(radiusKm, 250);
                String url = BASE_URL
                    + "?nearby=" + lat + "," + lon + "," + clampedRadius
                    + "&include=location,urls,player,images"
                    + "&limit=" + limit
                    + "&offset=" + offset
                    + "&orderby=distance";

                Request request = new Request.Builder()
                    .url(url)
                    .header("x-windy-api-key", apiKey)
                    .header("Accept", "application/json")
                    .build();

                List<CameraFeed> page = fetchPage(request);

                if (page.isEmpty()) {
                    // No more results
                    break;
                }

                allCameras.addAll(page);

                if (page.size() < limit) {
                    // Last page
                    break;
                }

                offset += limit;
            }

            Log.d(TAG, "Windy: fetched " + allCameras.size() + " cameras");
            callback.onSuccess(allCameras);

        } catch (Exception e) {
            Log.e(TAG, "Windy fetch failed", e);
            callback.onError("Windy error: " + e.getMessage());
        }
    }

    private List<CameraFeed> fetchPage(Request request) throws IOException {
        List<CameraFeed> cameras = new ArrayList<>();

        try (Response response = http.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 401 || response.code() == 403) {
                    throw new IOException("Invalid Windy API key (HTTP " + response.code() + ")");
                }
                if (response.code() == 429) {
                    throw new IOException("Windy rate limit exceeded. Try a smaller radius.");
                }
                throw new IOException("Windy HTTP " + response.code());
            }

            String body = response.body() != null ? response.body().string() : "";
            if (body.isEmpty()) return cameras;

            JSONObject root;
            try {
                root = new JSONObject(body);
            } catch (org.json.JSONException e) {
                throw new IOException("Windy response not valid JSON: " + e.getMessage());
            }
            JSONArray webcams = root.optJSONArray("webcams");
            if (webcams == null) return cameras;

            for (int i = 0; i < webcams.length(); i++) {
                try {
                    CameraFeed cam = parseWebcam(webcams.getJSONObject(i));
                    if (cam != null) cameras.add(cam);
                } catch (Exception e) {
                    Log.w(TAG, "Failed to parse Windy webcam at index " + i, e);
                }
            }
        }

        return cameras;
    }

    private CameraFeed parseWebcam(JSONObject obj) throws Exception {
        String webcamId = obj.optString("webcamId", "");
        if (webcamId.isEmpty()) return null;

        CameraFeed cam = new CameraFeed();
        cam.uid = "camfeed-windy-" + webcamId;
        cam.source = "windy";

        cam.name = obj.optString("title", "Windy Camera " + webcamId);
        if (cam.name.isEmpty()) cam.name = "Windy Camera " + webcamId;

        // Location
        JSONObject location = obj.optJSONObject("location");
        if (location == null) return null;
        // Use NaN as sentinel — JSONObject.optDouble returns 0.0 on missing key,
        // but (0,0) is a valid ocean coordinate.  Use Double.NaN fallback instead.
        cam.latitude  = location.optDouble("latitude",  Double.NaN);
        cam.longitude = location.optDouble("longitude", Double.NaN);
        if (Double.isNaN(cam.latitude) || Double.isNaN(cam.longitude)) return null;

        cam.city = location.optString("city", "");
        cam.country = location.optString("country", "");

        // player.day is a string URL in Windy API v3 (not a nested object)
        JSONObject player = obj.optJSONObject("player");
        if (player != null) {
            String embedUrl = player.optString("day", "");
            if (!embedUrl.isEmpty()) {
                cam.streamUrl = embedUrl;
                cam.streamType = CameraFeed.StreamType.EMBED;
            }
        }

        // Fallback to detail page URL
        if (cam.streamUrl == null || cam.streamUrl.isEmpty()) {
            JSONObject urls = obj.optJSONObject("urls");
            if (urls != null) {
                cam.streamUrl = urls.optString("detail", "");
                cam.streamType = CameraFeed.StreamType.WEB;
            }
        }

        // Thumbnail for the list view
        JSONObject images = obj.optJSONObject("images");
        if (images != null) {
            JSONObject current = images.optJSONObject("current");
            if (current != null) {
                cam.thumbnailUrl = current.optString("preview", "");
            }
        }

        return cam;
    }
}
