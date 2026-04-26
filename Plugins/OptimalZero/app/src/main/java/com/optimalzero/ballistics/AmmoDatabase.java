package com.optimalzero.ballistics;
import java.util.ArrayList;
import java.util.List;

public class AmmoDatabase {
    private final List<Cartridge> cartridges = new ArrayList<>();

    public AmmoDatabase() {
        // 5.56x45 NATO
        cartridges.add(new Cartridge("5.56x45 / 55gr FMJ (M193)",   "5.56x45 NATO", 0.243, 55,  "Standard ball"));
        cartridges.add(new Cartridge("5.56x45 / 62gr FMJ (M855)",   "5.56x45 NATO", 0.307, 62,  "Green tip"));
        cartridges.add(new Cartridge("5.56x45 / 77gr OTM (Mk262)",  "5.56x45 NATO", 0.372, 77,  "Open tip match"));
        // 7.62x51 NATO
        cartridges.add(new Cartridge("7.62x51 / 147gr FMJ (M80)",   "7.62x51 NATO", 0.408, 147, "Standard ball"));
        cartridges.add(new Cartridge("7.62x51 / 168gr HPBT (M118LR)","7.62x51 NATO",0.447, 168, "Long-range match"));
        cartridges.add(new Cartridge(".308 Win / 175gr HPBT",        "7.62x51 NATO", 0.505, 175, "Match/precision"));
        // 7.62x39
        cartridges.add(new Cartridge("7.62x39 / 123gr FMJ",         "7.62x39",      0.275, 123, "Standard AK ball"));
        // 300 BLK
        cartridges.add(new Cartridge("300 BLK / 110gr Lehigh Defense",  "300 BLK", 0.289, 110, "Supersonic expanding"));
        cartridges.add(new Cartridge("300 BLK / 110gr V-Max",           "300 BLK", 0.289, 110, "Supersonic varmint"));
        cartridges.add(new Cartridge("300 BLK / 125gr Supersonic",      "300 BLK", 0.232, 125, "Standard supersonic"));
        cartridges.add(new Cartridge("300 BLK / 150gr TTSX",            "300 BLK", 0.376, 150, "Supersonic hunting"));
        cartridges.add(new Cartridge("300 BLK / 190gr Subsonic",        "300 BLK", 0.274, 190, "Subsonic suppressed"));
        cartridges.add(new Cartridge("300 BLK / 220gr Subsonic",        "300 BLK", 0.320, 220, "Heavy subsonic"));
        // 6.5 Creedmoor
        cartridges.add(new Cartridge("6.5 CM / 140gr ELD-M",         "6.5 Creedmoor",0.530, 140, "Match/precision"));
        // 9mm
        cartridges.add(new Cartridge("9mm / 115gr FMJ",              "9mm",           0.140, 115, "Standard pistol"));
        cartridges.add(new Cartridge("9mm / 124gr FMJ",              "9mm",           0.158, 124, "NATO ball"));
        cartridges.add(new Cartridge("9mm / 147gr Subsonic",         "9mm",           0.185, 147, "Subsonic suppressed"));
        // 5.56x45 additional grain weights
        cartridges.add(new Cartridge("5.56x45 / 70gr TSX",          "5.56x45 NATO",  0.338, 70,  "Barnes TAC-TX"));
        // 5.7x28
        cartridges.add(new Cartridge("5.7x28 / 27gr HP",            "5.7x28",        0.100, 27,  "Lightweight high velocity"));
        cartridges.add(new Cartridge("5.7x28 / 40gr FMJ (SS190)",   "5.7x28",        0.143, 40,  "Standard ball"));
        // 7.62x51 additional grain weights
        cartridges.add(new Cartridge("7.62x51 / 175gr SMK",             "7.62x51 NATO", 0.505, 175, "Sierra MatchKing"));
        cartridges.add(new Cartridge("7.62x51 / 185gr Berger",          "7.62x51 NATO", 0.541, 185, "Juggernaut match"));
        // 5.56x45 additional
        cartridges.add(new Cartridge("5.56x45 / 50gr V-Max",            "5.56x45 NATO", 0.242, 50, "Varmint/lightweight"));
        cartridges.add(new Cartridge("5.56x45 / 69gr SMK",              "5.56x45 NATO", 0.355, 69, "Sierra MatchKing"));
        cartridges.add(new Cartridge("5.56x45 / 75gr BTHP",             "5.56x45 NATO", 0.395, 75, "Hornady match"));
        // 9mm additional
        cartridges.add(new Cartridge("9mm / 147gr FMJ",                 "9mm", 0.185, 147, "Standard subsonic"));
        // 6.5 Creedmoor additional
        cartridges.add(new Cartridge("6.5 CM / 120gr Nosler BT",        "6.5 Creedmoor", 0.458, 120, "Hunting"));
        cartridges.add(new Cartridge("6.5 CM / 130gr Berger Hybrid",    "6.5 Creedmoor", 0.510, 130, "Long range match"));
        cartridges.add(new Cartridge("6.5 CM / 143gr ELD-X",            "6.5 Creedmoor", 0.625, 143, "Hunting/precision"));
        // .308 Win hunting loads
        cartridges.add(new Cartridge(".308 Win / 150gr SP",              "7.62x51 NATO", 0.338, 150, "Hunting soft point"));
        cartridges.add(new Cartridge(".308 Win / 165gr BTSP",            "7.62x51 NATO", 0.447, 165, "Hunting boat tail"));
        // .338 Lapua
        cartridges.add(new Cartridge(".338 Lapua / 250gr HPBT",          "338 Lapua", 0.587, 250, "Long range precision"));
        cartridges.add(new Cartridge(".338 Lapua / 285gr HPBT",          "338 Lapua", 0.780, 285, "Extreme long range"));
        // .50 BMG
        cartridges.add(new Cartridge(".50 BMG / 660gr FMJ M33",          "50 BMG", 0.670, 660, "Standard ball"));
        cartridges.add(new Cartridge(".50 BMG / 750gr A-Max",            "50 BMG", 1.050, 750, "Long range match"));
    }

    public List<Cartridge> getAll() { return cartridges; }

    public String[] getDisplayNames() {
        String[] names = new String[cartridges.size()];
        for (int i = 0; i < cartridges.size(); i++) names[i] = cartridges.get(i).displayName;
        return names;
    }

    public Cartridge getAt(int index) {
        if (index < 0 || index >= cartridges.size()) return cartridges.get(0);
        return cartridges.get(index);
    }
}
