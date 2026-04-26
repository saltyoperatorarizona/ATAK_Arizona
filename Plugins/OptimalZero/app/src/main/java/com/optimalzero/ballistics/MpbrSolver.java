package com.optimalzero.ballistics;

/**
 * Iterates zero candidates 10yd–200yd (1yd steps) and returns the ZeroResult
 * that maximizes Max Point Blank Range.
 *
 * Performance (DT=0.005s, max 550m):
 *   ~190 candidates × ~130 physics steps each ≈ 25,000 total steps → <50ms on device.
 */
public class MpbrSolver {

    private static final double MIN_ZERO_M  = 9.144;   // 10 yd
    private static final double MAX_ZERO_M  = 182.88;  // 200 yd
    private static final double ZERO_STEP_M = 0.9144;  // 1 yd
    private static final double MAX_RANGE_M = 550.0;   // compute to 600 yd
    private static final double TRAJ_STEP_M = 5.0;     // output every 5 m

    private final BallisticsEngine engine = new BallisticsEngine();

    /**
     * Find the zero that maximises MPBR for the given setup.
     *
     * @param mv_ms       muzzle velocity (m/s)
     * @param bc_g1       G1 BC (imperial lb/in²)
     * @param hob_m       height over bore (m)
     * @param vitalZone_m vital zone diameter (m)
     * @return            ZeroResult, or null if no valid solution found
     */
    public ZeroResult solveMpbr(double mv_ms, double bc_g1,
                                 double hob_m, double vitalZone_m) {
        double vzRadius_m = vitalZone_m / 2.0;
        double bestZero_m = -1;
        double bestMpbr_m = 0;

        for (double z = MIN_ZERO_M; z <= MAX_ZERO_M; z += ZERO_STEP_M) {
            double theta = engine.findLaunchAngle(z, mv_ms, bc_g1, hob_m);
            double mpbr  = engine.computeMpbr(theta, mv_ms, bc_g1, hob_m, vzRadius_m, MAX_RANGE_M);
            if (mpbr > bestMpbr_m) {
                bestMpbr_m = mpbr;
                bestZero_m = z;
            }
        }

        if (bestZero_m < 0) return null;

        // Compute display trajectory for the best zero
        double bestTheta = engine.findLaunchAngle(bestZero_m, mv_ms, bc_g1, hob_m);
        java.util.List<TrajectoryPoint> traj = engine.computeTrajectory(
                bestTheta, mv_ms, bc_g1, hob_m, MAX_RANGE_M, TRAJ_STEP_M);

        // Near zero = first point where bullet enters vital zone from below
        double nearZero_m = findNearZero(traj, vitalZone_m * 39.3701 / 2.0);

        ZeroResult r = new ZeroResult();
        r.recommendedZero_m   = bestZero_m;
        r.recommendedZero_yd  = bestZero_m * 1.09361;
        r.mpbr_m              = bestMpbr_m;
        r.mpbr_yd             = bestMpbr_m * 1.09361;
        r.nearZero_m          = nearZero_m;
        r.nearZero_yd         = nearZero_m * 1.09361;
        r.trajectory          = traj;
        return r;
    }

    /** First point where bullet enters within vzRadius_in of LOS (from below). */
    private double findNearZero(java.util.List<TrajectoryPoint> traj, double vzRadius_in) {
        for (TrajectoryPoint pt : traj) {
            if (pt.dropFromLOS_in >= -vzRadius_in) return pt.distance_m;
        }
        return 0;
    }
}
