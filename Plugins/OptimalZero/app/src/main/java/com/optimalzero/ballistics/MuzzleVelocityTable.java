package com.optimalzero.ballistics;
import java.util.HashMap;
import java.util.Map;

public class MuzzleVelocityTable {
    private final Map<String, Object[]> table = new HashMap<>();

    public MuzzleVelocityTable() {

        table.put("5.56x45 NATO", new Object[]{
            new float[]{3.5f,4f,  4.5f,5f,  5.5f,6f,  6.5f,7f,  7.5f,8f,  8.5f,9f,  9.5f,10f, 10.3f,10.5f,11f, 11.2f,11.5f,12f, 12.5f,13f, 13.5f,14f, 14.2f,14.5f,16f, 18f, 20f, 22f, 24f},
            new int[] {1650,1750,1850,1950,2000,2050,2100,2150,2200,2250,2300,2350,2430,2500,2550, 2600, 2680,2720, 2750, 2800,2840, 2880,2920, 2950,2960, 2980, 3020,3100,3240,3300,3350}
        });

        table.put("7.62x51 NATO", new Object[]{
            new float[]{16f, 17f, 18f, 20f, 22f, 24f},
            new int[] {2620,2660,2700,2780,2830,2900}
        });

        table.put("7.62x39", new Object[]{
            new float[]{10f, 12f, 14f, 16f, 16.25f,18f, 20f},
            new int[] {2050,2150,2250,2350,2370,   2400,2430}
        });

        table.put("300 BLK", new Object[]{
            new float[]{3.5f,4f,  4.5f,5f,  5.5f,6f,  6.5f,7f,  7.5f,8f,  9f,  10f, 10.5f,12f, 16f},
            new int[] {1450,1550,1650,1700,1750,1800,1850,1900,1950,2000,2100,2130,2150, 2220,2350}
        });

        table.put("6.5 Creedmoor", new Object[]{
            new float[]{18f, 20f, 22f, 24f, 26f},
            new int[] {2580,2650,2710,2750,2810}
        });

        table.put("9mm", new Object[]{
            new float[]{2f,  2.5f,3f,  3.5f,4f,  4.5f,5f,  6f,  16f},
            new int[] {900, 950, 1050,1100,1150,1180,1200,1220,1450}
        });

        table.put("5.7x28", new Object[]{
            new float[]{4f,  4.8f,5f,  6f,  10f, 16f},
            new int[] {1450,1550,1600,1700,1950,2100}
        });

        table.put("45 ACP", new Object[]{
            new float[]{2f,  2.5f,3f,  3.5f,4f,  4.5f,5f},
            new int[] {750, 780, 820, 850, 880, 900, 920}
        });

        table.put("338 Lapua", new Object[]{
            new float[]{24f, 26f, 27f, 28f},
            new int[] {2950,3000,3050,3100}
        });

        table.put("50 BMG", new Object[]{
            new float[]{29f, 36f, 45f},
            new int[] {2900,3050,3200}
        });
    }

    public int getMuzzleVelocity(String caliberKey, float barrelIn) {
        Object[] data = table.get(caliberKey);
        if (data == null) return 2800;
        float[] barrels = (float[]) data[0];
        int[]   mvs     = (int[])   data[1];
        if (barrelIn <= barrels[0])                  return mvs[0];
        if (barrelIn >= barrels[barrels.length - 1]) return mvs[mvs.length - 1];
        for (int i = 0; i < barrels.length - 1; i++) {
            if (barrelIn >= barrels[i] && barrelIn <= barrels[i + 1]) {
                double t = (barrelIn - barrels[i]) / (barrels[i + 1] - barrels[i]);
                return (int)(mvs[i] + t * (mvs[i + 1] - mvs[i]));
            }
        }
        return mvs[mvs.length - 1];
    }

    public static float[] getBarrelOptions() {
        return new float[]{
            3.5f, 4f, 4.5f, 5f, 5.5f, 6f, 6.5f,
            7f, 7.5f, 8f, 8.5f, 9f, 9.5f,
            10f, 10.3f, 10.5f, 11f, 11.2f, 11.5f,
            12f, 12.5f, 13f, 13.5f,
            14f, 14.2f, 14.5f, 16f,
            18f, 20f, 22f, 24f, 26f
        };
    }
}
