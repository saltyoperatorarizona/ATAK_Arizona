package com.atakmap.android.camfeed;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ScrollView;
import android.content.SharedPreferences;
import android.webkit.WebResourceRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.atakmap.coremap.maps.assets.Icon;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.camfeed.model.CameraFeed;
import com.atakmap.android.camfeed.source.KmzSource;
import com.atakmap.android.camfeed.source.OsmSource;
import com.atakmap.android.camfeed.source.WindySource;
import com.atakmap.android.camfeed.util.SettingsManager;
import com.atakmap.android.dropdown.DropDown;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.maps.MapEvent;
import com.atakmap.android.maps.MapEventDispatcher;
import com.atakmap.android.maps.MapGroup;
import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.maps.Marker;
import com.atakmap.coremap.log.Log;
import com.atakmap.coremap.maps.coords.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class CamFeedDropDownReceiver extends DropDownReceiver
        implements DropDown.OnStateListener {

    public static final String SHOW_PLUGIN = "com.atakmap.android.camfeed.SHOW_PLUGIN";
    private static final String TAG = "CamFeedDropDown";

    private static final int TAB_ALL   = 0;
    private static final int TAB_WINDY = 1;
    private static final int TAB_OSM   = 2;
    private static final int TAB_KMZ   = 3;

    private final Context pluginContext;
    private final SettingsManager settings;
    private final Handler mainHandler;
    private final ExecutorService executor;
    private final OkHttpClient httpClient;

    private final WindySource windySource;
    private final OsmSource   osmSource;
    private final KmzSource   kmzSource;

    // All loaded cameras — only mutated on the main thread after fetch completes
    private final List<CameraFeed> allCameras = new ArrayList<>();

    // Views
    private View mainView;
    private View aboutView;
    private LinearLayout containerCameras;
    private LinearLayout layoutEmpty;
    private LinearLayout layoutControls;
    private LinearLayout layoutResultsBar;
    private TextView tvStatus;
    private TextView tvCountWindy, tvCountOsm, tvCountKmz, tvCountTotal;
    private TextView tvProgress;
    private EditText etRadius;
    private RadioButton rbMapCenter, rbMyLocation;
    private CheckBox cbWindy, cbOsm, cbKmz;
    private Button btnFetch;

    private int currentTab = TAB_ALL;

    // UIDs of CoT markers currently on the ATAK map
    private final List<String> postedCoTUids = new ArrayList<>();

    // UID → CameraFeed for fast lookup on marker tap
    private final Map<String, CameraFeed> camsByUid = new HashMap<>();
    private MapEventDispatcher.MapEventDispatchListener itemClickListener;

    // Persistent direct-URL feed list
    private static final String FEED_PREFS_NAME = "CamFeed_Feeds";
    private static final String FEED_PREFS_KEY  = "feeds";
    private SharedPreferences feedPrefs;
    private final List<Feed> savedFeeds = new ArrayList<>();

    public CamFeedDropDownReceiver(MapView mapView, Context pluginContext) {
        super(mapView);
        this.pluginContext = pluginContext;
        feedPrefs = pluginContext.getSharedPreferences(FEED_PREFS_NAME, Context.MODE_PRIVATE);
        savedFeeds.addAll(loadFeeds());
        this.settings      = new SettingsManager(pluginContext);
        this.mainHandler   = new Handler(Looper.getMainLooper());

        // Single-thread executor — sources run sequentially to avoid hammering APIs
        this.executor = Executors.newSingleThreadExecutor();

        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30,  TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();

        this.windySource = new WindySource(httpClient, settings.getWindyApiKey());
        this.osmSource   = new OsmSource(httpClient, settings.getOverpassUrl(),
                                          settings.isOsmSkipNoStream());
        this.kmzSource   = new KmzSource(settings.getKmzPath());

        mainView  = PluginLayoutInflater.inflate(pluginContext, R.layout.camfeed_layout, null);
        aboutView = PluginLayoutInflater.inflate(pluginContext, R.layout.camfeed_about, null);

        aboutView.findViewById(R.id.btn_about_back).setOnClickListener(v ->
            showDropDown(mainView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH, FULL_HEIGHT, false, this));

        setupUI();

        // Show saved URL feeds immediately — no fetch required on first open
        if (!savedFeeds.isEmpty()) {
            for (Feed f : savedFeeds) allCameras.add(feedToCameraFeed(f));
            updateCounts();
            rebuildCameraList();
            showResultsMode();
        }

        registerItemClickListener();
    }

    // -------------------------------------------------------------------------
    // Map marker click → stream opener
    // -------------------------------------------------------------------------

    private void registerItemClickListener() {
        itemClickListener = event -> {
            MapItem item = event.getItem();
            if (item == null) return;
            CameraFeed cam = camsByUid.get(item.getUID());
            if (cam != null) {
                openStream(cam);
            }
        };
        getMapView().getMapEventDispatcher()
            .addMapEventListener(MapEvent.ITEM_CLICK, itemClickListener);
    }

    // -------------------------------------------------------------------------
    // UI setup
    // -------------------------------------------------------------------------

    private void showResultsMode() {
        layoutControls.setVisibility(View.GONE);
        layoutResultsBar.setVisibility(View.VISIBLE);
    }

    private void showSearchMode() {
        layoutResultsBar.setVisibility(View.GONE);
        layoutControls.setVisibility(View.VISIBLE);
    }

    private void setupUI() {
        tvStatus = mainView.findViewById(R.id.tv_status);
        layoutControls   = mainView.findViewById(R.id.layout_controls);
        layoutResultsBar = mainView.findViewById(R.id.layout_results_bar);

        mainView.findViewById(R.id.btn_back_search).setOnClickListener(v -> showSearchMode());

        mainView.findViewById(R.id.btn_about).setOnClickListener(v ->
            showDropDown(aboutView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH, FULL_HEIGHT, false, this));
        mainView.findViewById(R.id.btn_settings).setOnClickListener(v -> showSettingsDialog());

        int[] tabIds = {R.id.btn_tab_all, R.id.btn_tab_windy, R.id.btn_tab_osm, R.id.btn_tab_kmz};
        for (int i = 0; i < tabIds.length; i++) {
            final int idx = i;
            mainView.findViewById(tabIds[i]).setOnClickListener(v -> {
                currentTab = idx;
                updateTabHighlight();
                rebuildCameraList();
            });
        }

        tvCountWindy = mainView.findViewById(R.id.tv_count_windy);
        tvCountOsm   = mainView.findViewById(R.id.tv_count_osm);
        tvCountKmz   = mainView.findViewById(R.id.tv_count_kmz);
        tvCountTotal = mainView.findViewById(R.id.tv_count_total);

        containerCameras = mainView.findViewById(R.id.container_cameras);
        layoutEmpty      = mainView.findViewById(R.id.layout_empty);
        tvProgress       = mainView.findViewById(R.id.tv_progress);

        etRadius = mainView.findViewById(R.id.et_radius);
        etRadius.setText(String.valueOf(settings.getRadiusKm()));

        rbMapCenter  = mainView.findViewById(R.id.rb_map_center);
        rbMyLocation = mainView.findViewById(R.id.rb_my_location);
        if (settings.isUseMapCenter()) rbMapCenter.setChecked(true);
        else rbMyLocation.setChecked(true);

        cbWindy = mainView.findViewById(R.id.cb_windy);
        cbOsm   = mainView.findViewById(R.id.cb_osm);
        cbKmz   = mainView.findViewById(R.id.cb_kmz);
        cbWindy.setChecked(settings.isSrcWindyEnabled());
        cbOsm.setChecked(settings.isSrcOsmEnabled());
        cbKmz.setChecked(settings.isSrcKmzEnabled());

        btnFetch = mainView.findViewById(R.id.btn_fetch);
        mainView.findViewById(R.id.btn_clear).setOnClickListener(v -> clearAll());
        btnFetch.setOnClickListener(v -> startFetch());

        updateTabHighlight();
        updateEmptyState();
    }

    // -------------------------------------------------------------------------
    // Fetch — runs all sources on background executor, races safely with CountDownLatch
    // -------------------------------------------------------------------------

    private void startFetch() {
        int radius;
        try {
            radius = Integer.parseInt(etRadius.getText().toString().trim());
            if (radius <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showToast("Enter a positive radius (km).");
            return;
        }

        boolean useMapCenter = rbMapCenter.isChecked();
        double lat, lon;

        if (useMapCenter) {
            // FIX #3: null-safe getPoint() chain
            com.atakmap.android.maps.MapView mv = getMapView();
            if (mv == null || mv.getPoint() == null) {
                showToast("Map center unavailable.");
                return;
            }
            GeoPoint center = mv.getPoint().get();
            if (center == null) {
                showToast("Map center unavailable.");
                return;
            }
            lat = center.getLatitude();
            lon = center.getLongitude();
        } else {
            com.atakmap.android.maps.MapItem self = getMapView().getSelfMarker();
            if (!(self instanceof com.atakmap.android.maps.PointMapItem)) {
                showToast("My Location not available. Use Map Center.");
                return;
            }
            GeoPoint pt = ((com.atakmap.android.maps.PointMapItem) self).getPoint();
            if (pt == null) {
                showToast("My Location fix not ready.");
                return;
            }
            lat = pt.getLatitude();
            lon = pt.getLongitude();
        }

        boolean doWindy = cbWindy.isChecked();
        boolean doOsm   = cbOsm.isChecked();
        boolean doKmz   = cbKmz.isChecked();

        if (!doWindy && !doOsm && !doKmz) {
            showToast("Enable at least one source (W/O/K).");
            return;
        }

        // Save prefs before fetch
        settings.setRadiusKm(radius);
        settings.setUseMapCenter(useMapCenter);
        settings.setSrcWindyEnabled(doWindy);
        settings.setSrcOsmEnabled(doOsm);
        settings.setSrcKmzEnabled(doKmz);

        btnFetch.setEnabled(false);
        btnFetch.setText("Fetching...");
        showProgress("Starting fetch...");

        final double finalLat = lat;
        final double finalLon = lon;
        final int    finalRad = radius;
        final boolean fw = doWindy, fo = doOsm, fk = doKmz;

        // FIX #5: Use a single background task that runs sources sequentially.
        // This eliminates the race condition — no partial callback counts possible.
        executor.execute(() -> {
            List<CameraFeed> combined = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            if (fw) {
                windySource.setApiKey(settings.getWindyApiKey());
                postProgress("Fetching Windy cameras...");
                windySource.fetchCameras(finalLat, finalLon, finalRad,
                    new SequentialCallback(combined, errors, "Windy"));
            }
            if (fo) {
                osmSource.setOverpassUrl(settings.getOverpassUrl());
                osmSource.setSkipNoStream(settings.isOsmSkipNoStream());
                postProgress("Fetching OSM cameras...");
                osmSource.fetchCameras(finalLat, finalLon, finalRad,
                    new SequentialCallback(combined, errors, "OSM"));
            }
            if (fk) {
                kmzSource.setKmzPath(settings.getKmzPath());
                postProgress("Parsing KMZ file...");
                kmzSource.fetchCameras(finalLat, finalLon, finalRad,
                    new SequentialCallback(combined, errors, "KMZ"));
            }

            // All sources done — deliver to UI thread
            List<CameraFeed> deduped = deduplicate(combined);
            mainHandler.post(() -> onFetchComplete(deduped, errors));
        });
    }

    /**
     * Synchronous callback adapter — since sources run sequentially on the executor
     * thread, each callback fires synchronously before the next source starts.
     * Combined list is NOT thread-shared during execution so no locks needed.
     */
    private static class SequentialCallback implements com.atakmap.android.camfeed.source.CameraSource.Callback {
        private final List<CameraFeed> combined;
        private final List<String>     errors;
        private final String           sourceName;

        SequentialCallback(List<CameraFeed> combined, List<String> errors, String sourceName) {
            this.combined    = combined;
            this.errors      = errors;
            this.sourceName  = sourceName;
        }

        @Override
        public void onSuccess(List<CameraFeed> cameras) {
            combined.addAll(cameras);
            Log.d("CamFeed", sourceName + ": " + cameras.size() + " cameras");
        }

        @Override
        public void onError(String errorMessage) {
            errors.add(sourceName + ": " + errorMessage);
            Log.w("CamFeed", sourceName + " error: " + errorMessage);
        }
    }

    private void onFetchComplete(List<CameraFeed> cameras, List<String> errors) {
        // Must be called on main thread
        // Keep only cameras that have an actual stream URL
        List<CameraFeed> withStream = new ArrayList<>();
        for (CameraFeed cam : cameras) {
            if (cam.streamUrl != null && !cam.streamUrl.trim().isEmpty()) {
                withStream.add(cam);
            }
        }
        cameras = withStream;

        allCameras.clear();
        for (Feed f : savedFeeds) allCameras.add(feedToCameraFeed(f));
        allCameras.addAll(cameras);

        if (settings.isPostCot()) {
            postCotMarkers(cameras);
        }

        updateCounts();
        rebuildCameraList();
        updateEmptyState();
        hideProgress();

        btnFetch.setEnabled(true);
        btnFetch.setText("FETCH CAMERAS");

        int total = cameras.size();
        tvStatus.setText("CamFeed — " + total + " camera" + (total == 1 ? "" : "s") + " loaded");

        // Collapse search panel so the list fills the screen
        if (total > 0) showResultsMode();

        // Report any source errors to user
        if (!errors.isEmpty()) {
            showToast("Errors: " + android.text.TextUtils.join("; ", errors));
        }
    }

    // -------------------------------------------------------------------------
    // CoT posting / removal
    // -------------------------------------------------------------------------

    private static final String CAMFEED_GROUP = "CamFeed";

    // Cache the per-source base64 icon strings — built once, reused for all markers
    private String iconWindy;
    private String iconOsm;
    private String iconKmz;

    private void buildIconCache() {
        if (iconWindy != null) return; // already built
        iconWindy = buildBase64Icon(0xFF00BFFF); // cyan
        iconOsm   = buildBase64Icon(0xFF77DD77); // green
        iconKmz   = buildBase64Icon(0xFFFFAA00); // orange
    }

    /**
     * Renders the camera marker drawable to a Bitmap, tints it with the given color,
     * then encodes it as a base64 PNG URI that ATAK's Icon.Builder accepts.
     */
    private String buildBase64Icon(int tintColor) {
        try {
            Drawable d = pluginContext.getResources().getDrawable(R.drawable.ic_camera_marker);
            int size = 64;
            Bitmap bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            d.setBounds(0, 0, size, size);

            // Draw base icon
            d.draw(canvas);

            // Apply tint overlay — paint the camera housing with source color
            Paint paint = new Paint();
            paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.MULTIPLY));
            // Re-draw with tint (only affects non-transparent pixels)
            Bitmap tinted = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas tintedCanvas = new Canvas(tinted);
            tintedCanvas.drawBitmap(bmp, 0, 0, paint);

            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            tinted.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bmp.recycle();
            tinted.recycle();

            return "base64://" + Base64.encodeToString(
                baos.toByteArray(), Base64.NO_WRAP | Base64.URL_SAFE);
        } catch (Exception e) {
            Log.e(TAG, "Failed to build icon", e);
            return null;
        }
    }

    private String getIconForSource(String source) {
        if ("windy".equals(source)) return iconWindy;
        if ("osm".equals(source))   return iconOsm;
        return iconKmz;
    }

    private MapGroup getOrCreateGroup() {
        MapGroup root = getMapView().getRootGroup();
        MapGroup group = root.findMapGroup(CAMFEED_GROUP);
        if (group == null) {
            group = root.addGroup(CAMFEED_GROUP);
        }
        return group;
    }

    private void postCotMarkers(List<CameraFeed> cameras) {
        removePostedCoT();
        buildIconCache();

        MapGroup group = getOrCreateGroup();
        List<String> newUids = new ArrayList<>();

        for (CameraFeed cam : cameras) {
            try {
                GeoPoint point = new GeoPoint(cam.latitude, cam.longitude);
                Marker marker = new Marker(point, cam.uid);
                marker.setType("b-m-p-s-m");
                marker.setTitle(cam.name);
                marker.setMetaString("callsign", cam.name);
                marker.setMetaString("remarks",
                    "[CamFeed/" + cam.source.toUpperCase() + "]"
                    + (cam.streamUrl != null ? " " + cam.streamUrl : ""));
                marker.setMetaBoolean("readiness", true);
                marker.setMetaBoolean("archive", false);
                marker.setMetaString("how", "m-g");
                marker.setClickable(true);

                // Apply camera icon tinted by source
                String iconUri = getIconForSource(cam.source);
                if (iconUri != null) {
                    Icon icon = new Icon.Builder()
                        .setImageUri(Icon.STATE_DEFAULT, iconUri)
                        .setSize(64, 64)
                        .build();
                    marker.setIcon(icon);
                }

                group.addItem(marker);
                newUids.add(cam.uid);
                camsByUid.put(cam.uid, cam);
            } catch (Exception e) {
                Log.w(TAG, "Marker add failed for " + cam.uid, e);
            }
        }

        postedCoTUids.clear();
        postedCoTUids.addAll(newUids);
        Log.d(TAG, "Added " + newUids.size() + " camera markers to map");
    }

    private void removePostedCoT() {
        try {
            MapGroup group = getMapView().getRootGroup().findMapGroup(CAMFEED_GROUP);
            if (group != null) {
                group.clearItems();
            }
        } catch (Exception e) {
            Log.w(TAG, "Could not clear CamFeed map group", e);
        }
        postedCoTUids.clear();
        camsByUid.clear();
    }

    // -------------------------------------------------------------------------
    // Camera list rendering
    // -------------------------------------------------------------------------

    private void rebuildCameraList() {
        containerCameras.removeAllViews();

        List<CameraFeed> toShow = filterByTab(allCameras);
        if (toShow.isEmpty()) {
            updateEmptyState();
            return;
        }

        layoutEmpty.setVisibility(View.GONE);
        mainView.findViewById(R.id.scroll_cameras).setVisibility(View.VISIBLE);

        for (CameraFeed cam : toShow) {
            View item = PluginLayoutInflater.inflate(pluginContext, R.layout.item_camera, null);

            TextView tvBadge = item.findViewById(R.id.tv_source_badge);
            TextView tvName  = item.findViewById(R.id.tv_camera_name);
            TextView tvLoc   = item.findViewById(R.id.tv_location);
            TextView tvType  = item.findViewById(R.id.tv_stream_type);
            TextView tvCity  = item.findViewById(R.id.tv_city);
            Button   btnView = item.findViewById(R.id.btn_open_stream);

            tvBadge.setText(cam.getSourceLabel());
            tvBadge.setBackgroundColor(cam.getSourceColor());
            tvBadge.setTextColor(Color.parseColor("#1A1A1A"));
            tvName.setText(cam.name);
            tvLoc.setText(String.format(Locale.US, "%.4f, %.4f", cam.latitude, cam.longitude));
            tvType.setText(cam.streamType.name());

            String city = buildCityLine(cam);
            tvCity.setText(city);
            tvCity.setVisibility(city.isEmpty() ? View.GONE : View.VISIBLE);

            if (cam.streamUrl == null || cam.streamUrl.isEmpty()) {
                btnView.setVisibility(View.GONE);
            } else {
                btnView.setVisibility(View.VISIBLE);
                btnView.setOnClickListener(v -> openStream(cam));
            }

            containerCameras.addView(item);
        }
    }

    private String buildCityLine(CameraFeed cam) {
        String city = cam.city != null ? cam.city.trim() : "";
        String country = cam.country != null ? cam.country.trim() : "";
        if (!city.isEmpty() && !country.isEmpty()) return city + ", " + country;
        if (!city.isEmpty()) return city;
        return country;
    }

    private List<CameraFeed> filterByTab(List<CameraFeed> cameras) {
        if (currentTab == TAB_ALL) return cameras;
        String src = tabSource(currentTab);
        List<CameraFeed> out = new ArrayList<>();
        for (CameraFeed c : cameras) {
            if (src.equals(c.source)) out.add(c);
        }
        return out;
    }

    private String tabSource(int tab) {
        switch (tab) {
            case TAB_WINDY: return "windy";
            case TAB_OSM:   return "osm";
            case TAB_KMZ:   return "kmz";
            default:        return "";
        }
    }

    private void updateTabHighlight() {
        int[] ids = {R.id.btn_tab_all, R.id.btn_tab_windy, R.id.btn_tab_osm, R.id.btn_tab_kmz};
        for (int i = 0; i < ids.length; i++) {
            mainView.findViewById(ids[i]).setAlpha(i == currentTab ? 1.0f : 0.5f);
        }
    }

    private void updateCounts() {
        int nw = 0, no = 0, nk = 0;
        for (CameraFeed c : allCameras) {
            if ("windy".equals(c.source))      nw++;
            else if ("osm".equals(c.source))   no++;
            else if ("kmz".equals(c.source))   nk++;
        }
        tvCountWindy.setText("W:" + nw);
        tvCountOsm.setText("O:" + no);
        tvCountKmz.setText("K:" + nk);
        int total = nw + no + nk;
        tvCountTotal.setText(total + " camera" + (total == 1 ? "" : "s"));
    }

    private void updateEmptyState() {
        boolean hasData = !allCameras.isEmpty();
        mainView.findViewById(R.id.scroll_cameras).setVisibility(hasData ? View.VISIBLE : View.GONE);
        layoutEmpty.setVisibility(hasData ? View.GONE : View.VISIBLE);
    }

    // -------------------------------------------------------------------------
    // Stream opening
    // -------------------------------------------------------------------------

    private void openStream(CameraFeed cam) {
        if (cam.streamUrl == null || cam.streamUrl.isEmpty()) {
            showToast("No stream URL for this camera.");
            return;
        }
        if (cam.isNativeStream()) {
            openNativeStream(cam);
        } else if (cam.streamType == CameraFeed.StreamType.EMBED
                && cam.thumbnailUrl != null && !cam.thumbnailUrl.isEmpty()) {
            // Windy cameras: JPEG snapshots — use OkHttp ImageView viewer (WebView can't
            // load external content in ATAK's plugin context reliably)
            openSnapshotViewer(cam);
        } else {
            openWebViewStream(cam);
        }
    }

    private void openNativeStream(CameraFeed cam) {
        try {
            Intent intent = new Intent("com.atakmap.android.video.DISPLAY");
            intent.putExtra("uid",   cam.uid);
            intent.putExtra("url",   cam.streamUrl);
            intent.putExtra("alias", cam.name);
            getMapView().getContext().sendBroadcast(intent);
        } catch (Exception e) {
            Log.w(TAG, "Native video intent failed, falling back to WebView", e);
            openWebViewStream(cam);
        }
    }

    /**
     * Windy snapshot viewer — downloads the JPEG via OkHttp and displays it in an
     * ImageView sized to the image's actual aspect ratio. Refreshes every 30 seconds.
     * Includes expand-to-fullscreen and close buttons.
     */
    private void openSnapshotViewer(CameraFeed cam) {
        mainHandler.post(() -> {
            Context ctx = getMapView().getContext();

            android.view.Display display =
                ((android.app.Activity) ctx).getWindowManager().getDefaultDisplay();
            android.graphics.Point screen = new android.graphics.Point();
            display.getSize(screen);

            float dp = ctx.getResources().getDisplayMetrics().density;
            int barH = (int)(44 * dp);
            int padH = (int)(14 * dp);

            // Root container
            android.widget.LinearLayout root = new android.widget.LinearLayout(ctx);
            root.setOrientation(android.widget.LinearLayout.VERTICAL);
            root.setBackgroundColor(Color.BLACK);

            // ── Title bar ──────────────────────────────────────────────────
            android.widget.LinearLayout titleBar = new android.widget.LinearLayout(ctx);
            titleBar.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            titleBar.setBackgroundColor(Color.parseColor("#1A1A1A"));
            titleBar.setGravity(android.view.Gravity.CENTER_VERTICAL);
            titleBar.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT, barH));

            TextView tvTitle = new TextView(ctx);
            tvTitle.setText(cam.name);
            tvTitle.setTextColor(Color.WHITE);
            tvTitle.setTextSize(13f);
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTitle.setSingleLine(true);
            tvTitle.setEllipsize(android.text.TextUtils.TruncateAt.END);
            tvTitle.setPadding(padH, 0, padH, 0);
            titleBar.addView(tvTitle, new android.widget.LinearLayout.LayoutParams(
                0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            Button btnExpand = new Button(ctx);
            btnExpand.setText("⤢");
            btnExpand.setTextColor(Color.parseColor("#888888"));
            btnExpand.setTextSize(18f);
            btnExpand.setBackground(null);
            btnExpand.setPadding(0, 0, 0, 0);
            titleBar.addView(btnExpand, new android.widget.LinearLayout.LayoutParams(barH, barH));

            Button btnClose = new Button(ctx);
            btnClose.setText("✕");
            btnClose.setTextColor(Color.parseColor("#CC2200"));
            btnClose.setTextSize(16f);
            btnClose.setBackground(null);
            btnClose.setPadding(0, 0, 0, 0);
            titleBar.addView(btnClose, new android.widget.LinearLayout.LayoutParams(barH, barH));

            root.addView(titleBar);

            // ── Image ──────────────────────────────────────────────────────
            android.widget.ImageView iv = new android.widget.ImageView(ctx);
            iv.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
            iv.setBackgroundColor(Color.BLACK);
            root.addView(iv);

            // ── Status bar ─────────────────────────────────────────────────
            TextView tvStatus = new TextView(ctx);
            tvStatus.setTextColor(Color.parseColor("#888888"));
            tvStatus.setTextSize(10f);
            tvStatus.setPadding(padH, (int)(6*dp), padH, (int)(6*dp));
            tvStatus.setBackgroundColor(Color.parseColor("#1A1A1A"));
            tvStatus.setText("Loading...");
            root.addView(tvStatus, new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

            // ── Dialog ─────────────────────────────────────────────────────
            android.app.Dialog dialog = new android.app.Dialog(ctx);
            dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
            dialog.setContentView(root);

            final boolean[] dismissed = {false};
            final boolean[] expanded  = {false};
            dialog.setOnDismissListener(d -> dismissed[0] = true);

            btnClose.setOnClickListener(v -> dialog.dismiss());

            btnExpand.setOnClickListener(v -> {
                android.view.Window win = dialog.getWindow();
                if (win == null) return;
                if (!expanded[0]) {
                    expanded[0] = true;
                    btnExpand.setText("⤡");
                    win.setLayout(
                        android.view.WindowManager.LayoutParams.MATCH_PARENT,
                        android.view.WindowManager.LayoutParams.MATCH_PARENT);
                    iv.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
                } else {
                    expanded[0] = false;
                    btnExpand.setText("⤢");
                    win.setLayout(
                        android.view.WindowManager.LayoutParams.WRAP_CONTENT,
                        android.view.WindowManager.LayoutParams.WRAP_CONTENT);
                }
            });

            dialog.show();

            String imgUrl = cam.thumbnailUrl.replace("/preview/", "/normal/");
            fetchAndShowSnapshot(imgUrl, iv, tvStatus, dismissed, expanded, cam.name, dialog, screen);
        });
    }

    private void fetchAndShowSnapshot(String imgUrl, android.widget.ImageView iv,
            TextView status, boolean[] dismissed, boolean[] expanded, String camName,
            android.app.Dialog dialog, android.graphics.Point screen) {
        if (dismissed[0]) return;

        new Thread(() -> {
            try {
                okhttp3.Request req = new okhttp3.Request.Builder()
                    .url(imgUrl + "?t=" + System.currentTimeMillis())
                    .header("Referer", "https://windy.com")
                    .header("User-Agent",
                        "Mozilla/5.0 (Linux; Android 11) AppleWebKit/537.36 "
                        + "Chrome/120.0.0.0 Safari/537.36")
                    .build();

                byte[] bytes;
                int httpCode;
                try (okhttp3.Response resp = httpClient.newCall(req).execute()) {
                    httpCode = resp.code();
                    if (!resp.isSuccessful() || resp.body() == null) {
                        mainHandler.post(() ->
                            status.setText("Feed unavailable (HTTP " + httpCode + ")"));
                        return;
                    }
                    bytes = resp.body().bytes();
                }

                android.graphics.Bitmap bmp =
                    android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bmp == null) {
                    mainHandler.post(() -> status.setText("Could not decode image"));
                    return;
                }

                // Fit image within 95% width / 85% height, preserving aspect ratio
                int bmpW = bmp.getWidth();
                int bmpH = bmp.getHeight();
                int maxW = (int)(screen.x * 0.95);
                int maxH = (int)(screen.y * 0.85);
                float scale = Math.min((float) maxW / bmpW, (float) maxH / bmpH);
                int dispW = Math.max(1, (int)(bmpW * scale));
                int dispH = Math.max(1, (int)(bmpH * scale));

                String time = new java.text.SimpleDateFormat("HH:mm:ss", Locale.US)
                    .format(new java.util.Date());

                mainHandler.post(() -> {
                    if (dismissed[0]) { bmp.recycle(); return; }

                    iv.setImageBitmap(bmp);

                    // Only resize dialog/ImageView when not in expanded (fullscreen) mode
                    if (!expanded[0]) {
                        android.widget.LinearLayout.LayoutParams ivLp =
                            new android.widget.LinearLayout.LayoutParams(dispW, dispH);
                        iv.setLayoutParams(ivLp);

                        android.view.Window win = dialog.getWindow();
                        if (win != null) {
                            win.setLayout(
                                dispW,
                                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
                        }
                    }

                    status.setText(camName + "  ·  " + time + "  ·  refreshes in 30s");
                });

                mainHandler.postDelayed(() -> {
                    if (!dismissed[0]) {
                        fetchAndShowSnapshot(imgUrl, iv, status, dismissed, expanded, camName,
                            dialog, screen);
                    }
                }, 30_000);

            } catch (Exception e) {
                Log.w(TAG, "Snapshot fetch failed: " + e.getMessage());
                mainHandler.post(() -> status.setText("Load error: " + e.getMessage()));
            }
        }, "CamFeed-Snapshot").start();
    }

    private void openWebViewStream(CameraFeed cam) {
        final WebView[] webViewHolder = new WebView[1];

        mainHandler.post(() -> {
            Context ctx = getMapView().getContext();
            float dp = ctx.getResources().getDisplayMetrics().density;
            int barH = (int)(44 * dp);
            int padH = (int)(14 * dp);

            android.view.Display display =
                ((android.app.Activity) ctx).getWindowManager().getDefaultDisplay();
            android.graphics.Point screen = new android.graphics.Point();
            display.getSize(screen);

            // Root container
            android.widget.LinearLayout root = new android.widget.LinearLayout(ctx);
            root.setOrientation(android.widget.LinearLayout.VERTICAL);
            root.setBackgroundColor(Color.BLACK);

            // ── Title bar ──────────────────────────────────────────────────
            android.widget.LinearLayout titleBar = new android.widget.LinearLayout(ctx);
            titleBar.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            titleBar.setBackgroundColor(Color.parseColor("#1A1A1A"));
            titleBar.setGravity(android.view.Gravity.CENTER_VERTICAL);
            titleBar.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT, barH));

            TextView tvTitle = new TextView(ctx);
            tvTitle.setText(cam.name);
            tvTitle.setTextColor(Color.WHITE);
            tvTitle.setTextSize(13f);
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTitle.setSingleLine(true);
            tvTitle.setEllipsize(android.text.TextUtils.TruncateAt.END);
            tvTitle.setPadding(padH, 0, padH, 0);
            titleBar.addView(tvTitle, new android.widget.LinearLayout.LayoutParams(
                0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            Button btnExpand = new Button(ctx);
            btnExpand.setText("⤢");
            btnExpand.setTextColor(Color.parseColor("#888888"));
            btnExpand.setTextSize(18f);
            btnExpand.setBackground(null);
            btnExpand.setPadding(0, 0, 0, 0);
            titleBar.addView(btnExpand, new android.widget.LinearLayout.LayoutParams(barH, barH));

            Button btnClose = new Button(ctx);
            btnClose.setText("✕");
            btnClose.setTextColor(Color.parseColor("#CC2200"));
            btnClose.setTextSize(16f);
            btnClose.setBackground(null);
            btnClose.setPadding(0, 0, 0, 0);
            titleBar.addView(btnClose, new android.widget.LinearLayout.LayoutParams(barH, barH));

            root.addView(titleBar);

            // ── WebView ────────────────────────────────────────────────────
            WebView wv = new WebView(ctx);
            webViewHolder[0] = wv;

            WebSettings ws = wv.getSettings();
            ws.setJavaScriptEnabled(true);
            ws.setDomStorageEnabled(true);
            ws.setMediaPlaybackRequiresUserGesture(false);
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            ws.setUseWideViewPort(true);
            ws.setLoadWithOverviewMode(true);
            ws.setUserAgentString(
                "Mozilla/5.0 (Linux; Android 11; Tablet) AppleWebKit/537.36 "
                + "Chrome/120.0.0.0 Safari/537.36");
            ws.setAllowFileAccess(false);
            ws.setAllowContentAccess(false);
            ws.setAllowFileAccessFromFileURLs(false);
            ws.setAllowUniversalAccessFromFileURLs(false);
            ws.setSafeBrowsingEnabled(true);
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String scheme = request.getUrl().getScheme();
                    if (scheme != null && scheme.equalsIgnoreCase("file")) return true;
                    return false;
                }
            });
            wv.loadUrl(cam.streamUrl);

            android.widget.LinearLayout.LayoutParams wvLp =
                new android.widget.LinearLayout.LayoutParams(
                    (int)(screen.x * 0.92), (int)(screen.y * 0.75));
            root.addView(wv, wvLp);

            // ── Dialog ─────────────────────────────────────────────────────
            android.app.Dialog dialog = new android.app.Dialog(ctx);
            dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
            dialog.setContentView(root);

            final boolean[] expanded = {false};

            dialog.setOnDismissListener(d -> {
                if (webViewHolder[0] != null) {
                    webViewHolder[0].stopLoading();
                    webViewHolder[0].destroy();
                    webViewHolder[0] = null;
                }
            });

            btnClose.setOnClickListener(v -> dialog.dismiss());

            btnExpand.setOnClickListener(v -> {
                android.view.Window win = dialog.getWindow();
                if (win == null) return;
                if (!expanded[0]) {
                    expanded[0] = true;
                    btnExpand.setText("⤡");
                    win.setLayout(
                        android.view.WindowManager.LayoutParams.MATCH_PARENT,
                        android.view.WindowManager.LayoutParams.MATCH_PARENT);
                    wv.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
                } else {
                    expanded[0] = false;
                    btnExpand.setText("⤢");
                    win.setLayout(
                        android.view.WindowManager.LayoutParams.WRAP_CONTENT,
                        android.view.WindowManager.LayoutParams.WRAP_CONTENT);
                    wv.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                        (int)(screen.x * 0.92), (int)(screen.y * 0.75)));
                }
            });

            dialog.show();
        });
    }

    // -------------------------------------------------------------------------
    // Settings dialog
    // -------------------------------------------------------------------------

    private void showSettingsDialog() {
        View dv = PluginLayoutInflater.inflate(pluginContext,
            R.layout.camfeed_settings_dialog, null);

        EditText etKey     = dv.findViewById(R.id.et_windy_key);
        EditText etOvp     = dv.findViewById(R.id.et_overpass_url);
        EditText etKmz     = dv.findViewById(R.id.et_kmz_path);
        CheckBox cbCot     = dv.findViewById(R.id.cb_post_cot);
        CheckBox cbOsmSkip = dv.findViewById(R.id.cb_osm_no_stream_only);

        etKey.setText(settings.getWindyApiKey());
        etOvp.setText(settings.getOverpassUrl());
        etKmz.setText(settings.getKmzPath());
        cbCot.setChecked(settings.isPostCot());
        cbOsmSkip.setChecked(settings.isOsmSkipNoStream());

        new AlertDialog.Builder(getMapView().getContext())
            .setTitle("CamFeed Settings")
            .setView(dv)
            .setPositiveButton("Save", (d, w) -> {
                settings.setWindyApiKey(etKey.getText().toString().trim());
                settings.setOverpassUrl(etOvp.getText().toString().trim());
                settings.setKmzPath(etKmz.getText().toString().trim());
                settings.setPostCot(cbCot.isChecked());
                settings.setOsmSkipNoStream(cbOsmSkip.isChecked());

                windySource.setApiKey(settings.getWindyApiKey());
                osmSource.setOverpassUrl(settings.getOverpassUrl());
                osmSource.setSkipNoStream(settings.isOsmSkipNoStream());
                kmzSource.setKmzPath(settings.getKmzPath());

                showToast("Settings saved.");
            })
            .setNeutralButton("Direct Feeds", (d, w) -> showDirectFeedsDialog())
            .setNegativeButton("Cancel", null)
            .show();
    }

    // -------------------------------------------------------------------------
    // Clear
    // -------------------------------------------------------------------------

    private void clearAll() {
        removePostedCoT();
        allCameras.clear();
        for (Feed f : savedFeeds) allCameras.add(feedToCameraFeed(f));
        containerCameras.removeAllViews();
        updateCounts();
        if (allCameras.isEmpty()) {
            updateEmptyState();
            showSearchMode();
        } else {
            rebuildCameraList();
            showResultsMode();
        }
        tvStatus.setText("CamFeed — No cameras loaded");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private List<CameraFeed> deduplicate(List<CameraFeed> cameras) {
        LinkedHashMap<String, CameraFeed> map = new LinkedHashMap<>();
        for (CameraFeed cam : cameras) map.putIfAbsent(cam.uid, cam);
        return new ArrayList<>(map.values());
    }

    /** Thread-safe progress update — can be called from any thread. */
    private void postProgress(String msg) {
        mainHandler.post(() -> showProgress(msg));
    }

    private void showProgress(String msg) {
        tvProgress.setText(msg);
        tvProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        tvProgress.setText("");
        tvProgress.setVisibility(View.GONE);
    }

    private void showToast(String msg) {
        mainHandler.post(() ->
            Toast.makeText(getMapView().getContext(), msg, Toast.LENGTH_LONG).show());
    }

    // -------------------------------------------------------------------------
    // DropDownReceiver lifecycle
    // -------------------------------------------------------------------------

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SHOW_PLUGIN.equals(intent.getAction())) {
            showDropDown(mainView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH, FULL_HEIGHT, false, this);
        }
    }

    @Override
    public void disposeImpl() {
        if (itemClickListener != null) {
            getMapView().getMapEventDispatcher()
                .removeMapEventListener(MapEvent.ITEM_CLICK, itemClickListener);
            itemClickListener = null;
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        httpClient.dispatcher().executorService().shutdown();
        removePostedCoT();
    }

    @Override public void onDropDownSelectionRemoved() {}
    @Override public void onDropDownVisible(boolean v) {}
    @Override public void onDropDownSizeChanged(double w, double h) {}

    @Override
    public void onDropDownClose() {
        try {
            settings.setRadiusKm(Integer.parseInt(etRadius.getText().toString().trim()));
        } catch (Exception ignored) {}
        settings.setUseMapCenter(rbMapCenter.isChecked());
        settings.setSrcWindyEnabled(cbWindy.isChecked());
        settings.setSrcOsmEnabled(cbOsm.isChecked());
        settings.setSrcKmzEnabled(cbKmz.isChecked());
    }

    // -------------------------------------------------------------------------
    // Direct URL feed persistence (4D/4E)
    // -------------------------------------------------------------------------

    public static class Feed {
        public String name;
        public String url;
        public boolean isDefault;

        public Feed(String name, String url, boolean isDefault) {
            this.name = name;
            this.url = url;
            this.isDefault = isDefault;
        }
    }

    private List<Feed> loadFeeds() {
        List<Feed> feeds = new ArrayList<>();
        String json = feedPrefs.getString(FEED_PREFS_KEY, null);
        if (json == null || json.isEmpty()) {
            feeds = seedDefaultFeeds();
            saveFeeds(feeds);
            return feeds;
        }
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                feeds.add(new Feed(
                    o.optString("name"),
                    o.optString("url"),
                    o.optBoolean("isDefault", false)
                ));
            }
        } catch (JSONException e) {
            Log.w(TAG, "feed JSON parse failed, reseeding", e);
            feeds = seedDefaultFeeds();
            saveFeeds(feeds);
        }
        return feeds;
    }

    private void saveFeeds(List<Feed> feeds) {
        try {
            JSONArray arr = new JSONArray();
            for (Feed f : feeds) {
                JSONObject o = new JSONObject();
                o.put("name", f.name);
                o.put("url", f.url);
                o.put("isDefault", f.isDefault);
                arr.put(o);
            }
            feedPrefs.edit().putString(FEED_PREFS_KEY, arr.toString()).apply();
        } catch (JSONException e) {
            Log.e(TAG, "feed JSON save failed", e);
        }
    }

    private List<Feed> seedDefaultFeeds() {
        List<Feed> seed = new ArrayList<>();
        seed.add(new Feed("Caltrans — Bay Bridge",
            "https://cwwp2.dot.ca.gov/vm/loc/d4/tv515fremontogden.htm", true));
        seed.add(new Feed("Arizona DOT 511",
            "https://az511.gov/cctv", true));
        seed.add(new Feed("NYC DOT — Times Square",
            "https://i5.nyctmc.org/cctv835.jpg", true));
        return seed;
    }

    private CameraFeed feedToCameraFeed(Feed f) {
        CameraFeed cam = new CameraFeed();
        cam.uid = "camfeed-url-" + Math.abs(f.url.hashCode());
        cam.name = f.name;
        cam.source = "url";
        cam.streamUrl = f.url;
        cam.streamType = CameraFeed.detectStreamType(f.url);
        cam.latitude = 0.0;
        cam.longitude = 0.0;
        return cam;
    }

    private void showDirectFeedsDialog() {
        Context ctx = getMapView().getContext();
        float dp = ctx.getResources().getDisplayMetrics().density;
        int pad = (int)(12 * dp);

        android.widget.LinearLayout root = new android.widget.LinearLayout(ctx);
        root.setOrientation(android.widget.LinearLayout.VERTICAL);
        root.setPadding(pad, pad, pad, pad);

        android.widget.LinearLayout list = new android.widget.LinearLayout(ctx);
        list.setOrientation(android.widget.LinearLayout.VERTICAL);

        android.widget.ScrollView scroll = new android.widget.ScrollView(ctx);
        scroll.addView(list);
        root.addView(scroll, new android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT, (int)(200 * dp)));

        Button btnAdd = new Button(ctx);
        btnAdd.setText("+ Add Feed");
        root.addView(btnAdd);

        buildFeedRows(ctx, list, dp);

        btnAdd.setOnClickListener(v -> {
            android.widget.LinearLayout al = new android.widget.LinearLayout(ctx);
            al.setOrientation(android.widget.LinearLayout.VERTICAL);
            al.setPadding(pad, pad, pad, pad);
            EditText etName = new EditText(ctx);
            etName.setHint("Name");
            EditText etUrl = new EditText(ctx);
            etUrl.setHint("URL (https:// or rtsp://)");
            al.addView(etName);
            al.addView(etUrl);
            new AlertDialog.Builder(ctx)
                .setTitle("Add Direct Feed")
                .setView(al)
                .setPositiveButton("Add", (d2, w2) -> {
                    String nm  = etName.getText().toString().trim();
                    String url = etUrl.getText().toString().trim();
                    if (nm.isEmpty() || url.isEmpty()) {
                        showToast("Name and URL required.");
                        return;
                    }
                    Feed nf = new Feed(nm, url, false);
                    savedFeeds.add(nf);
                    saveFeeds(savedFeeds);
                    allCameras.add(feedToCameraFeed(nf));
                    rebuildCameraList();
                    updateCounts();
                    if (!allCameras.isEmpty()) showResultsMode();
                    buildFeedRows(ctx, list, dp);
                })
                .setNegativeButton("Cancel", null)
                .show();
        });

        new AlertDialog.Builder(ctx)
            .setTitle("Direct URL Feeds")
            .setView(root)
            .setPositiveButton("Done", null)
            .show();
    }

    private void buildFeedRows(Context ctx, android.widget.LinearLayout list, float dp) {
        list.removeAllViews();
        for (int i = 0; i < savedFeeds.size(); i++) {
            final int idx = i;
            final Feed f  = savedFeeds.get(i);

            android.widget.LinearLayout row = new android.widget.LinearLayout(ctx);
            row.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            row.setPadding(0, (int)(4 * dp), 0, (int)(4 * dp));

            TextView tv = new TextView(ctx);
            tv.setText(f.name + "\n" + f.url);
            tv.setTextSize(12f);
            row.addView(tv, new android.widget.LinearLayout.LayoutParams(
                0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            Button btnDel = new Button(ctx);
            btnDel.setText("✕");
            btnDel.setBackground(null);
            btnDel.setTextColor(0xFFCC2200);
            btnDel.setOnClickListener(v -> {
                savedFeeds.remove(idx);
                saveFeeds(savedFeeds);
                String uid = "camfeed-url-" + Math.abs(f.url.hashCode());
                for (int j = allCameras.size() - 1; j >= 0; j--) {
                    if (uid.equals(allCameras.get(j).uid)) { allCameras.remove(j); break; }
                }
                rebuildCameraList();
                updateCounts();
                if (allCameras.isEmpty()) {
                    updateEmptyState();
                    showSearchMode();
                }
                buildFeedRows(ctx, list, dp);
            });
            row.addView(btnDel);
            list.addView(row);
        }
    }
}
