package com.optimalzero.plugin;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.atakmap.android.dropdown.DropDown;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.maps.MapView;
import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.coremap.log.Log;

import com.optimalzero.ballistics.AmmoDatabase;
import com.optimalzero.ballistics.Cartridge;
import com.optimalzero.ballistics.MpbrSolver;
import com.optimalzero.ballistics.MuzzleVelocityTable;
import com.optimalzero.ballistics.TrajectoryPoint;
import com.optimalzero.ballistics.ZeroResult;
import com.optimalzero.ui.TrajectoryChartView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main UI panel for Optimal Zero.
 *
 * Extends DropDownReceiver so it can:
 *   1. Act as a BroadcastReceiver (onReceive → showDropDown)
 *   2. Manage its own ATAK side-panel lifecycle (OnStateListener)
 *
 * Threading: ballistics computation runs on a single-thread ExecutorService;
 * UI updates are posted back to the main thread via Handler.
 *
 * Layout: res/layout/dropdown_optimal_zero.xml
 */
public class OptimalZeroDropDown extends DropDownReceiver
        implements DropDown.OnStateListener {

    public static final String TAG         = "OptimalZeroDropDown";
    public static final String SHOW_PLUGIN = "com.optimalzero.plugin.SHOW";

    private final Context      pluginContext;
    private final View         mainView;

    // ── UI references ──────────────────────────────────────────────────────
    private Spinner           spinnerAmmo;
    private Spinner           spinnerBarrel;
    private Spinner           spinnerOptic;
    private EditText          editHob;
    private EditText          editRiser;
    private RadioGroup        radioCowitness;
    private EditText          editVitalZone;
    private EditText          editMvOverride;
    private Button            btnCalculate;
    private TextView          tvResults;
    private TrajectoryChartView chartView;

    // ── Data ───────────────────────────────────────────────────────────────
    private final AmmoDatabase       ammoDb  = new AmmoDatabase();
    private final MuzzleVelocityTable mvTable = new MuzzleVelocityTable();
    private final float[]             barrelOptions = MuzzleVelocityTable.getBarrelOptions();

    private final ExecutorService executor  = Executors.newSingleThreadExecutor();
    private final Handler         uiHandler = new Handler(Looper.getMainLooper());

    private static final String[] OPTIC_TYPES = {
            "Red Dot — MPBR optimize",
            "BDC Reticle (Phase 2)",
            "MOA Reticle (Phase 2)",
            "Mil Reticle (Phase 2)"
    };

    // ── Constructor ────────────────────────────────────────────────────────

    public OptimalZeroDropDown(MapView mapView, Context pluginContext) {
        super(mapView);
        this.pluginContext = pluginContext;

        // PluginLayoutInflater resolves R.layout from the plugin APK's resources,
        // not from ATAK's resource pool — required for plugin layouts to inflate.
        mainView = PluginLayoutInflater.inflate(pluginContext,
                R.layout.dropdown_optimal_zero, null);

        bindViews();
        populateSpinners();
        setDefaults();
        wireListeners();
    }

    // ── View binding ──────────────────────────────────────────────────────

    private void bindViews() {
        spinnerAmmo     = mainView.findViewById(R.id.spinner_ammo);
        spinnerBarrel   = mainView.findViewById(R.id.spinner_barrel);
        spinnerOptic    = mainView.findViewById(R.id.spinner_optic);
        editHob         = mainView.findViewById(R.id.edit_hob);
        editRiser       = mainView.findViewById(R.id.edit_riser);
        radioCowitness  = mainView.findViewById(R.id.radio_cowitness);
        editVitalZone   = mainView.findViewById(R.id.edit_vital_zone);
        editMvOverride  = mainView.findViewById(R.id.edit_mv_override);
        btnCalculate    = mainView.findViewById(R.id.btn_calculate);
        tvResults       = mainView.findViewById(R.id.tv_results);
        chartView       = mainView.findViewById(R.id.chart_view);
    }

    private void populateSpinners() {
        // Ammo spinner
        ArrayAdapter<String> ammoAdapter = new ArrayAdapter<>(pluginContext,
                R.layout.spinner_item, ammoDb.getDisplayNames());
        ammoAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerAmmo.setAdapter(ammoAdapter);

        // Barrel spinner (show as "7\"", "10.5\"" etc.)
        String[] barrelLabels = new String[barrelOptions.length];
        for (int i = 0; i < barrelOptions.length; i++) {
            float b = barrelOptions[i];
            barrelLabels[i] = (b == Math.floor(b)) ? (int)b + "\"" : b + "\"";
        }
        ArrayAdapter<String> barrelAdapter = new ArrayAdapter<>(pluginContext,
                R.layout.spinner_item, barrelLabels);
        barrelAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerBarrel.setAdapter(barrelAdapter);

        // Optic type spinner
        ArrayAdapter<String> opticAdapter = new ArrayAdapter<>(pluginContext,
                R.layout.spinner_item, OPTIC_TYPES);
        opticAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerOptic.setAdapter(opticAdapter);
    }

    private void setDefaults() {
        // Default to 16" barrel (index 19 in getBarrelOptions())
        spinnerBarrel.setSelection(19);

        editHob.setText("1.5");
        editVitalZone.setText("6");
        editRiser.setHint("0");

        tvResults.setTypeface(android.graphics.Typeface.MONOSPACE);
        tvResults.setText("Configure your setup and press CALCULATE.");

        updateMvHint();
    }

    private void wireListeners() {
        // Re-compute MV hint when ammo or barrel changes
        AdapterView.OnItemSelectedListener mvHintListener =
                new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int i, long id) { updateMvHint(); }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        };
        spinnerAmmo.setOnItemSelectedListener(mvHintListener);
        spinnerBarrel.setOnItemSelectedListener(mvHintListener);

        // Cowitness radio → HOB hint
        radioCowitness.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.radio_absolute)    editHob.setHint("~1.5\" absolute");
            else if (id == R.id.radio_lower3) editHob.setHint("~2.26\" lower 1/3");
            else                               editHob.setHint("HOB (in)");
        });

        btnCalculate.setOnClickListener(v -> onCalculateClicked());
    }

    private void updateMvHint() {
        try {
            Cartridge c  = ammoDb.getAt(spinnerAmmo.getSelectedItemPosition());
            float barrelIn = barrelOptions[spinnerBarrel.getSelectedItemPosition()];
            int mv = mvTable.getMuzzleVelocity(c.caliberKey, barrelIn);
            editMvOverride.setHint("Auto: " + mv + " fps");
        } catch (Exception e) { /* ignore during init */ }
    }

    // ── Calculate ─────────────────────────────────────────────────────────

    private void onCalculateClicked() {
        // ── Parse and validate inputs ──────────────────────────────────────
        Cartridge c = ammoDb.getAt(spinnerAmmo.getSelectedItemPosition());
        float barrelIn = barrelOptions[spinnerBarrel.getSelectedItemPosition()];

        double hob_in, riser_in = 0, vz_in, mv_fps;

        try {
            String hobStr = editHob.getText().toString().trim();
            if (hobStr.isEmpty()) { tvResults.setText("Enter Height Over Bore."); return; }
            hob_in = Double.parseDouble(hobStr);
            if (hob_in < 0.5 || hob_in > 10) { tvResults.setText("HOB must be 0.5–10 inches."); return; }
        } catch (NumberFormatException e) { tvResults.setText("Invalid HOB."); return; }

        String riserStr = editRiser.getText().toString().trim();
        if (!riserStr.isEmpty()) {
            try { riser_in = Double.parseDouble(riserStr); }
            catch (NumberFormatException e) { tvResults.setText("Invalid riser value."); return; }
        }

        try {
            String vzStr = editVitalZone.getText().toString().trim();
            if (vzStr.isEmpty()) { tvResults.setText("Enter vital zone size."); return; }
            vz_in = Double.parseDouble(vzStr);
            if (vz_in < 1 || vz_in > 24) { tvResults.setText("Vital zone must be 1–24 inches."); return; }
        } catch (NumberFormatException e) { tvResults.setText("Invalid vital zone."); return; }

        String mvStr = editMvOverride.getText().toString().trim();
        if (!mvStr.isEmpty()) {
            try {
                mv_fps = Double.parseDouble(mvStr);
                if (mv_fps < 500 || mv_fps > 5000) { tvResults.setText("MV must be 500–5000 fps."); return; }
            } catch (NumberFormatException e) { tvResults.setText("Invalid MV override."); return; }
        } else {
            mv_fps = mvTable.getMuzzleVelocity(c.caliberKey, barrelIn);
        }

        final double totalHob_in  = hob_in + riser_in;
        final double totalHob_m   = totalHob_in * 0.0254;
        final double mv_ms        = mv_fps * 0.3048;
        final double vz_m         = vz_in  * 0.0254;
        final double bc           = c.bcG1;

        // ── Run on background thread ───────────────────────────────────────
        tvResults.setText("Calculating…");
        btnCalculate.setEnabled(false);

        executor.execute(() -> {
            try {
                ZeroResult result = new MpbrSolver().solveMpbr(mv_ms, bc, totalHob_m, vz_m);
                uiHandler.post(() -> displayResults(result, vz_in, c, barrelIn, mv_fps,
                        totalHob_in));
            } catch (Exception ex) {
                Log.e(TAG, "Ballistics error", ex);
                uiHandler.post(() -> {
                    tvResults.setText("Calculation error: " + ex.getMessage());
                    btnCalculate.setEnabled(true);
                });
            }
        });
    }

    // ── Results display ────────────────────────────────────────────────────

    private void displayResults(ZeroResult result, double vz_in, Cartridge cartridge,
                                 float barrelIn, double mv_fps, double totalHob_in) {
        btnCalculate.setEnabled(true);

        if (result == null) {
            tvResults.setText("No valid zero found. Check your inputs.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("══ OPTIMAL ZERO ══════════════════\n");
        sb.append(String.format(Locale.US, "  Load:       %s\n", cartridge.displayName));
        String barrelLabel = (barrelIn == Math.floor(barrelIn)) ? (int)barrelIn + "\"" : barrelIn + "\"";
        sb.append(String.format(Locale.US, "  Barrel:     %s  MV: %.0f fps\n", barrelLabel, mv_fps));
        sb.append(String.format(Locale.US, "  HOB:        %.2f\"   VZ: %.0f\"\n", totalHob_in, vz_in));
        sb.append("\n");
        sb.append(String.format(Locale.US, "  Zero:       %.0f yd  (%.0f m)\n",
                result.recommendedZero_yd, result.recommendedZero_m));
        sb.append(String.format(Locale.US, "  MPBR:       %.0f yd  (%.0f m)\n",
                result.mpbr_yd, result.mpbr_m));
        sb.append(String.format(Locale.US, "  Near zero:  ~%.0f yd\n", result.nearZero_yd));
        sb.append("\n");
        sb.append("── DROP TABLE ─────────────────────\n");
        sb.append("  Dist    Drop     Zone\n");

        double[] checkYds = {25, 50, 75, 100, 125, 150, 175, 200, 250, 300, 400, 500};
        for (double dyd : checkYds) {
            double dm = dyd * 0.9144;
            if (dm > result.mpbr_m * 1.15) break; // don't print way past MPBR
            double drop = interpolateDrop(result.trajectory, dm);
            String inZone = Math.abs(drop) <= vz_in / 2.0 ? "✓" : "✗";
            sb.append(String.format(Locale.US, "  %3.0fyd  %+6.1f\"   %s\n", dyd, drop, inZone));
        }

        tvResults.setText(sb.toString());

        if (chartView != null) {
            chartView.setData(result.trajectory, result.recommendedZero_m, vz_in);
        }
    }

    private double interpolateDrop(List<TrajectoryPoint> traj, double dist_m) {
        if (traj == null || traj.isEmpty()) return 0;
        for (int i = 0; i < traj.size() - 1; i++) {
            TrajectoryPoint a = traj.get(i), b = traj.get(i + 1);
            if (dist_m >= a.distance_m && dist_m <= b.distance_m) {
                double t = (dist_m - a.distance_m) / (b.distance_m - a.distance_m);
                return a.dropFromLOS_in + t * (b.dropFromLOS_in - a.dropFromLOS_in);
            }
        }
        return traj.get(traj.size() - 1).dropFromLOS_in;
    }

    // ── DropDownReceiver ───────────────────────────────────────────────────

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SHOW_PLUGIN.equals(intent.getAction())) {
            showDropDown(mainView,
                    HALF_WIDTH, FULL_HEIGHT,   // portrait: half-screen panel
                    FULL_WIDTH, HALF_HEIGHT,   // landscape: bottom half
                    false, this);
        }
    }

    @Override public void onDropDownVisible(boolean v)   { Log.d(TAG, "visible=" + v); }
    @Override public void onDropDownSelectionRemoved()   {}
    @Override public void onDropDownClose()              { Log.d(TAG, "closed"); }
    @Override public void onDropDownSizeChanged(double w, double h) {}

    @Override
    protected void disposeImpl() {
        executor.shutdown();
    }
}
