package com.optimalzero.ballistics;

public class TrajectoryPoint {
    public double distance_m;
    public double distance_yd;
    /** Positive = above LOS, Negative = below LOS, inches */
    public double dropFromLOS_in;
    public double velocity_fps;
    public double timeOfFlight_s;
}
