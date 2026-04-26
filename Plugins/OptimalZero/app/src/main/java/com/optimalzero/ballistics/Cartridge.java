package com.optimalzero.ballistics;

public class Cartridge {
    public final String displayName;
    public final String caliberKey;   // must match MuzzleVelocityTable keys
    public final double bcG1;         // G1 BC in imperial (lb/in²)
    public final int    grainWeight;
    public final String loadDescription;

    public Cartridge(String displayName, String caliberKey,
                     double bcG1, int grainWeight, String loadDescription) {
        this.displayName     = displayName;
        this.caliberKey      = caliberKey;
        this.bcG1            = bcG1;
        this.grainWeight     = grainWeight;
        this.loadDescription = loadDescription;
    }
}
