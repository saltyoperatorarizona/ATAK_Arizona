package com.atakmap.android.camfeed.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Persists CamFeed plugin settings via SharedPreferences.
 */
public class SettingsManager {

    private static final String PREFS_NAME = "camfeed_prefs";

    private static final String KEY_WINDY_API_KEY    = "windy_api_key";
    private static final String KEY_OVERPASS_URL     = "overpass_url";
    private static final String KEY_KMZ_PATH         = "kmz_path";
    private static final String KEY_RADIUS_KM        = "radius_km";
    private static final String KEY_USE_MAP_CENTER   = "use_map_center";
    private static final String KEY_POST_COT         = "post_cot";
    private static final String KEY_OSM_SKIP_NO_STREAM = "osm_skip_no_stream";
    private static final String KEY_SRC_WINDY        = "src_windy";
    private static final String KEY_SRC_OSM          = "src_osm";
    private static final String KEY_SRC_KMZ          = "src_kmz";

    private final SharedPreferences prefs;

    public SettingsManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // --- Windy ---

    public String getWindyApiKey() {
        return prefs.getString(KEY_WINDY_API_KEY, "");
    }

    public void setWindyApiKey(String key) {
        prefs.edit().putString(KEY_WINDY_API_KEY, key).apply();
    }

    // --- OSM ---

    public String getOverpassUrl() {
        return prefs.getString(KEY_OVERPASS_URL, "https://overpass-api.de/api/interpreter");
    }

    public void setOverpassUrl(String url) {
        prefs.edit().putString(KEY_OVERPASS_URL, url).apply();
    }

    public boolean isOsmSkipNoStream() {
        return prefs.getBoolean(KEY_OSM_SKIP_NO_STREAM, false);
    }

    public void setOsmSkipNoStream(boolean skip) {
        prefs.edit().putBoolean(KEY_OSM_SKIP_NO_STREAM, skip).apply();
    }

    // --- KMZ ---

    public String getKmzPath() {
        return prefs.getString(KEY_KMZ_PATH, "");
    }

    public void setKmzPath(String path) {
        prefs.edit().putString(KEY_KMZ_PATH, path).apply();
    }

    // --- Fetch params ---

    public int getRadiusKm() {
        return prefs.getInt(KEY_RADIUS_KM, 50);
    }

    public void setRadiusKm(int km) {
        prefs.edit().putInt(KEY_RADIUS_KM, km).apply();
    }

    public boolean isUseMapCenter() {
        return prefs.getBoolean(KEY_USE_MAP_CENTER, true);
    }

    public void setUseMapCenter(boolean useMapCenter) {
        prefs.edit().putBoolean(KEY_USE_MAP_CENTER, useMapCenter).apply();
    }

    // --- Source toggles ---

    public boolean isSrcWindyEnabled() {
        return prefs.getBoolean(KEY_SRC_WINDY, true);
    }

    public void setSrcWindyEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SRC_WINDY, enabled).apply();
    }

    public boolean isSrcOsmEnabled() {
        return prefs.getBoolean(KEY_SRC_OSM, true);
    }

    public void setSrcOsmEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SRC_OSM, enabled).apply();
    }

    public boolean isSrcKmzEnabled() {
        return prefs.getBoolean(KEY_SRC_KMZ, true);
    }

    public void setSrcKmzEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SRC_KMZ, enabled).apply();
    }

    // --- Map posting ---

    public boolean isPostCot() {
        return prefs.getBoolean(KEY_POST_COT, true);
    }

    public void setPostCot(boolean post) {
        prefs.edit().putBoolean(KEY_POST_COT, post).apply();
    }
}
