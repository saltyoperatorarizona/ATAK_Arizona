package com.optimalzero.ballistics;
import java.util.ArrayList;
import java.util.List;

/**
 * G1 point-mass ballistics engine.
 * LOS model: horizontal line at y = hob_m.
 * Drop = y_bullet(x) - hob_m (negative = below LOS).
 * DT = 0.005s: ~1% error vs 0.001s, but 5x faster — adequate for zeroing.
 */
public class BallisticsEngine {

    static final double G       = 9.80665;
    static final double C_SOUND = 340.29;
    static final double BC_CONV = 703.069;    // lb/in² → kg/m²
    private static final double DT = 0.005;

    private static final double[] G1_MACH = {
        0.00, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40,
        0.45, 0.50, 0.55, 0.60, 0.65, 0.70, 0.75, 0.80, 0.85,
        0.90, 0.95, 1.00, 1.05, 1.10, 1.15, 1.20, 1.25, 1.30,
        1.35, 1.40, 1.45, 1.50, 1.55, 1.60, 1.65, 1.70, 1.75,
        1.80, 1.85, 1.90, 1.95, 2.00, 2.05, 2.10, 2.15, 2.20,
        2.25, 2.30, 2.35, 2.40, 2.45, 2.50, 2.55, 2.60, 2.65,
        2.70, 2.75, 2.80, 2.85, 2.90, 2.95, 3.00, 3.10, 3.20,
        3.30, 3.40, 3.50, 3.60, 3.70, 3.80, 3.90, 4.00, 4.20,
        4.40, 4.60, 4.80, 5.00
    };
    private static final double[] G1_CD = {
        0.2629, 0.2558, 0.2487, 0.2413, 0.2344, 0.2278, 0.2214, 0.2155, 0.2104,
        0.2061, 0.2032, 0.2020, 0.2034, 0.2165, 0.2513, 0.3038, 0.3696, 0.4317,
        0.4781, 0.5112, 0.5245, 0.5208, 0.5116, 0.4974, 0.4783, 0.4561, 0.4325,
        0.4108, 0.3934, 0.3786, 0.3659, 0.3546, 0.3440, 0.3350, 0.3270, 0.3200,
        0.3141, 0.3086, 0.3038, 0.2994, 0.2955, 0.2919, 0.2886, 0.2856, 0.2828,
        0.2804, 0.2782, 0.2762, 0.2745, 0.2730, 0.2716, 0.2705, 0.2694, 0.2685,
        0.2677, 0.2670, 0.2663, 0.2658, 0.2653, 0.2649, 0.2645, 0.2639, 0.2634,
        0.2630, 0.2626, 0.2624, 0.2621, 0.2619, 0.2617, 0.2616, 0.2614, 0.2612,
        0.2610, 0.2608, 0.2606, 0.2604
    };

    /** Package-private: linear interpolation into G1 table. */
    double getCdG1(double mach) {
        int n = G1_MACH.length;
        if (mach <= G1_MACH[0])     return G1_CD[0];
        if (mach >= G1_MACH[n - 1]) return G1_CD[n - 1];
        for (int i = 0; i < n - 1; i++) {
            if (mach < G1_MACH[i + 1]) {
                double t = (mach - G1_MACH[i]) / (G1_MACH[i + 1] - G1_MACH[i]);
                return G1_CD[i] + t * (G1_CD[i + 1] - G1_CD[i]);
            }
        }
        return G1_CD[n - 1];
    }

    /**
     * Returns bullet Y (m) at targetX_m.
     * Bullet starts at (0,0), launched at theta_rad above horizontal.
     * Used by findLaunchAngle binary search.
     */
    public double bulletHeightAt(double targetX_m, double theta_rad,
                                  double mv_ms, double bc_g1) {
        double bc_si = bc_g1 * BC_CONV;
        double vx = mv_ms * Math.cos(theta_rad);
        double vy = mv_ms * Math.sin(theta_rad);
        double x = 0, y = 0;
        while (x < targetX_m) {
            double v = Math.sqrt(vx * vx + vy * vy);
            if (v < 10) break;
            double mach = v / C_SOUND;
            double cd   = getCdG1(mach);
            double drag = 0.5 * v * v * cd / bc_si;
            double ax   = -drag * (vx / v);
            double ay   = -drag * (vy / v) - G;
            // Adaptive last step: don't overshoot
            double dt = DT;
            if (vx > 1e-6) { double r = targetX_m - x; if (r / vx < dt) dt = r / vx; }
            vx += ax * dt;  vy += ay * dt;
            x  += vx * dt;  y  += vy * dt;
        }
        return y;
    }

    /**
     * Binary search (60 iters) for launch angle where bulletHeightAt(zeroDistance) == hob_m.
     * Converges to sub-microradian accuracy.
     */
    public double findLaunchAngle(double zeroDistance_m, double mv_ms,
                                   double bc_g1, double hob_m) {
        double lo = 0, hi = Math.toRadians(15);
        if (bulletHeightAt(zeroDistance_m, hi, mv_ms, bc_g1) < hob_m) return hi;
        for (int i = 0; i < 60; i++) {
            double mid = (lo + hi) / 2;
            if (bulletHeightAt(zeroDistance_m, mid, mv_ms, bc_g1) < hob_m) lo = mid;
            else hi = mid;
        }
        return (lo + hi) / 2;
    }

    /**
     * Full trajectory → List<TrajectoryPoint>.
     * Drop is relative to horizontal LOS at y = hob_m.
     * Output points spaced stepSize_m apart.
     */
    public List<TrajectoryPoint> computeTrajectory(double theta_rad, double mv_ms,
            double bc_g1, double hob_m, double maxRange_m, double stepSize_m) {
        double bc_si = bc_g1 * BC_CONV;
        List<TrajectoryPoint> pts = new ArrayList<>();
        double vx = mv_ms * Math.cos(theta_rad), vy = mv_ms * Math.sin(theta_rad);
        double x = 0, y = 0, tof = 0, nextOut = 0;
        while (x <= maxRange_m + stepSize_m) {
            if (x >= nextOut - 1e-6) {
                TrajectoryPoint pt = new TrajectoryPoint();
                pt.distance_m     = x;
                pt.distance_yd    = x * 1.09361;
                pt.dropFromLOS_in = (y - hob_m) * 39.3701;
                pt.velocity_fps   = Math.sqrt(vx*vx + vy*vy) * 3.28084;
                pt.timeOfFlight_s = tof;
                pts.add(pt);
                nextOut += stepSize_m;
                if (x >= maxRange_m) break;
            }
            double v = Math.sqrt(vx*vx + vy*vy);
            if (v < 10) break;
            double mach = v / C_SOUND, cd = getCdG1(mach);
            double drag = 0.5 * v * v * cd / bc_si;
            vx += (-drag*(vx/v))         * DT;
            vy += (-drag*(vy/v) - G)     * DT;
            x  += vx * DT;  y += vy * DT;  tof += DT;
        }
        return pts;
    }

    /**
     * Fast MPBR computation — no object allocation, called ~190x per solver run.
     * Returns farthest continuous distance where |y_bullet - hob_m| <= vzRadius_m.
     * Stops at first exit from vital zone (top or bottom).
     */
    public double computeMpbr(double theta_rad, double mv_ms, double bc_g1,
                               double hob_m, double vzRadius_m, double maxRange_m) {
        double bc_si = bc_g1 * BC_CONV;
        double vx = mv_ms * Math.cos(theta_rad), vy = mv_ms * Math.sin(theta_rad);
        double x = 0, y = 0;
        boolean inZone = false;
        double  farEdge = 0;
        while (x <= maxRange_m) {
            boolean ok = Math.abs(y - hob_m) <= vzRadius_m;
            if (!inZone) { if (ok) { inZone = true; farEdge = x; } }
            else         { if (ok) farEdge = x; else break; }
            double v = Math.sqrt(vx*vx + vy*vy);
            if (v < 10) break;
            double mach = v / C_SOUND, cd = getCdG1(mach);
            double drag = 0.5 * v * v * cd / bc_si;
            vx += (-drag*(vx/v))        * DT;
            vy += (-drag*(vy/v) - G)    * DT;
            x  += vx * DT;  y += vy * DT;
        }
        return inZone ? farEdge : 0;
    }
}
