package com.optimalzero.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.optimalzero.ballistics.TrajectoryPoint;

import java.util.List;

/**
 * Canvas-based trajectory chart.
 *
 * Layout (dark theme):
 *   ┌─────────────────────────────────────────┐
 *   │  +V/2 ──── vital zone ceiling (green)   │
 *   │  LOS  ···· horizontal (white dashed)     │
 *   │  -V/2 ──── vital zone floor (green)     │
 *   │  curve = bullet path (yellow)            │
 *   │  |    = zero distance (cyan dashed)      │
 *   └─────────────────────────────────────────┘
 *
 * Call setData() to update; the view invalidates itself.
 */
public class TrajectoryChartView extends View {

    // Colours
    private static final int CLR_BG         = Color.parseColor("#1A1A2E");
    private static final int CLR_GRID       = Color.parseColor("#2A2A4A");
    private static final int CLR_LOS        = Color.WHITE;
    private static final int CLR_VZ_FILL    = Color.parseColor("#2200CC44");
    private static final int CLR_VZ_EDGE    = Color.parseColor("#AA00CC44");
    private static final int CLR_TRAJ       = Color.parseColor("#FFD700");   // gold
    private static final int CLR_ZERO_LINE  = Color.parseColor("#00BFFF");   // deep sky blue
    private static final int CLR_LABEL      = Color.parseColor("#CCCCCC");

    private final Paint paintBg      = new Paint();
    private final Paint paintGrid    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintLos     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintVzFill  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintVzEdge  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintTraj    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintZero    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintLabel   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintHint    = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Chart margins
    private static final float ML = 60f, MR = 20f, MT = 20f, MB = 40f;

    // Data
    private List<TrajectoryPoint> points;
    private double zeroDist_m;
    private double vzRadius_in;

    // Computed bounds (set in setData)
    private double maxX_yd, yMin_in, yMax_in;

    public TrajectoryChartView(Context context) {
        super(context); init();
    }
    public TrajectoryChartView(Context context, AttributeSet attrs) {
        super(context, attrs); init();
    }
    public TrajectoryChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); init();
    }

    private void init() {
        paintBg.setColor(CLR_BG);
        paintBg.setStyle(Paint.Style.FILL);

        paintGrid.setColor(CLR_GRID);
        paintGrid.setStrokeWidth(1f);
        paintGrid.setStyle(Paint.Style.STROKE);

        paintLos.setColor(CLR_LOS);
        paintLos.setStrokeWidth(1.5f);
        paintLos.setStyle(Paint.Style.STROKE);
        paintLos.setPathEffect(new DashPathEffect(new float[]{8f, 6f}, 0));

        paintVzFill.setColor(CLR_VZ_FILL);
        paintVzFill.setStyle(Paint.Style.FILL);

        paintVzEdge.setColor(CLR_VZ_EDGE);
        paintVzEdge.setStrokeWidth(1.5f);
        paintVzEdge.setStyle(Paint.Style.STROKE);

        paintTraj.setColor(CLR_TRAJ);
        paintTraj.setStrokeWidth(2.5f);
        paintTraj.setStyle(Paint.Style.STROKE);
        paintTraj.setStrokeJoin(Paint.Join.ROUND);
        paintTraj.setStrokeCap(Paint.Cap.ROUND);

        paintZero.setColor(CLR_ZERO_LINE);
        paintZero.setStrokeWidth(1.5f);
        paintZero.setStyle(Paint.Style.STROKE);
        paintZero.setPathEffect(new DashPathEffect(new float[]{10f, 5f}, 0));

        paintLabel.setColor(CLR_LABEL);
        paintLabel.setTextSize(28f);
        paintLabel.setAntiAlias(true);

        paintHint.setColor(CLR_LABEL);
        paintHint.setTextSize(32f);
        paintHint.setTextAlign(Paint.Align.CENTER);
        paintHint.setAntiAlias(true);
    }

    /**
     * Supply trajectory data and trigger a redraw.
     *
     * @param points      from ZeroResult.trajectory
     * @param zeroDist_m  recommended zero distance in metres
     * @param vitalZone_in vital zone diameter in inches
     */
    public void setData(List<TrajectoryPoint> points,
                        double zeroDist_m, double vitalZone_in) {
        this.points      = points;
        this.zeroDist_m  = zeroDist_m;
        this.vzRadius_in = vitalZone_in / 2.0;

        // Compute axis ranges
        if (points != null && !points.isEmpty()) {
            maxX_yd = points.get(points.size() - 1).distance_yd;
            yMin_in = Double.MAX_VALUE;
            yMax_in = -Double.MAX_VALUE;
            for (TrajectoryPoint pt : points) {
                if (pt.dropFromLOS_in < yMin_in) yMin_in = pt.dropFromLOS_in;
                if (pt.dropFromLOS_in > yMax_in) yMax_in = pt.dropFromLOS_in;
            }
            // Expand to always show vital zone + 20% padding
            double range = Math.max(yMax_in - yMin_in, vzRadius_in * 3);
            double mid   = (yMax_in + yMin_in) / 2.0;
            yMin_in = mid - range * 0.6;
            yMax_in = mid + range * 0.4;
            // Always include ±vzRadius + some headroom
            yMin_in = Math.min(yMin_in, -vzRadius_in * 2.5);
            yMax_in = Math.max(yMax_in,  vzRadius_in * 1.5);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = getWidth(), h = getHeight();

        // Background
        canvas.drawRect(0, 0, w, h, paintBg);

        if (points == null || points.isEmpty()) {
            canvas.drawText("Press CALCULATE to see trajectory",
                    w / 2f, h / 2f, paintHint);
            return;
        }

        float cw = w - ML - MR;   // chart area width
        float ch = h - MT - MB;   // chart area height

        // ── Helpers ────────────────────────────────────────────────────────
        // Convert data coords to screen coords
        // x: 0..maxX_yd  → ML..(ML+cw)
        // y: yMax_in..yMin_in → MT..(MT+ch)  [inverted: large drop = bottom]

        // ── Vital zone fill ────────────────────────────────────────────────
        float syTop    = dataYtoScreen( vzRadius_in, ch);
        float syBottom = dataYtoScreen(-vzRadius_in, ch);
        canvas.drawRect(ML, MT + syTop, ML + cw, MT + syBottom, paintVzFill);
        canvas.drawLine(ML, MT + syTop,    ML + cw, MT + syTop,    paintVzEdge);
        canvas.drawLine(ML, MT + syBottom, ML + cw, MT + syBottom, paintVzEdge);

        // ── Grid lines (horizontal, every 5") ─────────────────────────────
        double gridStepIn = pickGridStep(yMax_in - yMin_in);
        double gridStart  = Math.ceil(yMin_in / gridStepIn) * gridStepIn;
        for (double gv = gridStart; gv <= yMax_in; gv += gridStepIn) {
            float gy = MT + dataYtoScreen(gv, ch);
            canvas.drawLine(ML, gy, ML + cw, gy, paintGrid);
            canvas.drawText(String.format("%.0f\"", gv), 2f, gy + 10f, paintLabel);
        }

        // ── X axis tick labels (yards) ─────────────────────────────────────
        double gridStepYd = pickGridStep(maxX_yd);
        double xStart     = 0;
        for (double xv = xStart; xv <= maxX_yd; xv += gridStepYd) {
            float sx = ML + dataXtoScreen(xv, cw);
            canvas.drawLine(sx, MT, sx, MT + ch, paintGrid);
            canvas.drawText(String.format("%.0fyd", xv), sx - 18f, h - 5f, paintLabel);
        }

        // ── LOS (y=0 dashed) ──────────────────────────────────────────────
        float syLos = MT + dataYtoScreen(0, ch);
        canvas.drawLine(ML, syLos, ML + cw, syLos, paintLos);

        // ── Zero-distance vertical line ────────────────────────────────────
        float zeroYd = (float)(zeroDist_m * 1.09361);
        if (zeroYd <= maxX_yd) {
            float szx = ML + dataXtoScreen(zeroYd, cw);
            canvas.drawLine(szx, MT, szx, MT + ch, paintZero);
            paintLabel.setColor(CLR_ZERO_LINE);
            canvas.drawText(String.format("%.0fyd", zeroYd), szx + 3f, MT + 30f, paintLabel);
            paintLabel.setColor(CLR_LABEL);
        }

        // ── Trajectory curve ───────────────────────────────────────────────
        Path trajPath = new Path();
        boolean first = true;
        for (TrajectoryPoint pt : points) {
            float sx = ML + dataXtoScreen(pt.distance_yd, cw);
            float sy = MT + dataYtoScreen(pt.dropFromLOS_in, ch);
            if (first) { trajPath.moveTo(sx, sy); first = false; }
            else        { trajPath.lineTo(sx, sy); }
        }
        canvas.drawPath(trajPath, paintTraj);
    }

    /** Map data Y (inches, up=positive) to canvas offset from top of chart area. */
    private float dataYtoScreen(double y_in, float chartH) {
        // yMax_in → 0 (top), yMin_in → chartH (bottom)
        double frac = (yMax_in - y_in) / (yMax_in - yMin_in);
        return (float)(frac * chartH);
    }

    /** Map data X (yards) to canvas offset from left of chart area. */
    private float dataXtoScreen(double x_yd, float chartW) {
        return (float)(x_yd / maxX_yd * chartW);
    }

    /** Choose a round grid step that gives 4-8 lines. */
    private double pickGridStep(double range) {
        double[] candidates = {1, 2, 5, 10, 20, 25, 50, 100, 200, 500};
        for (double c : candidates) {
            if (range / c <= 8) return c;
        }
        return 100;
    }
}
