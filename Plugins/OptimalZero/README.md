# OptimalZero — ATAK MPBR Ballistics Plugin

Version 20 | April 26, 2026 | TAK.gov Approved

Weapons zero calculator for ATAK. Finds the optimal zero distance to maximize Max Point Blank Range (MPBR) for your specific rifle setup.

## Compatibility

- ATAK CIV 5.5+
- ATAK CIV 5.6.0.x (Play Store and TAK.gov builds)
- TAK.gov approved and signed

## Features

- G1 ballistic drag model (JBM/Ingalls standard)
- 35 preset cartridges across 10 calibers:
  - 5.56x45: 50gr V-Max, M193 55gr, M855 62gr, 69gr SMK, 70gr TSX, Mk262 77gr, 75gr BTHP
  - 7.62x51: 147gr M80, 150gr SP, 165gr BTSP, 168gr M118LR, 175gr SMK, 175gr HPBT, 185gr Berger
  - 7.62x39: 123gr FMJ
  - 300 BLK: 110gr V-Max, 125gr Supersonic, 150gr TTSX, 190gr Subsonic, 208gr A-MAX Sub, 220gr Subsonic
  - 6.5 Creedmoor: 120gr Nosler BT, 130gr Berger Hybrid, 140gr ELD-M, 143gr ELD-X
  - 9mm: 115gr FMJ, 124gr FMJ, 147gr FMJ, 147gr Subsonic
  - 5.7x28: 27gr HP, 40gr FMJ SS190
  - .338 Lapua: 250gr HPBT, 285gr HPBT
  - .50 BMG: 660gr FMJ M33, 750gr A-Max
- 33 barrel length options from 3.5 to 26 inches including fractional sizes
- Barrel length to muzzle velocity lookup with manual override
- Height over bore and riser height inputs
- Vital zone input (default 6 inches)
- Trajectory chart with vital zone band overlay
- Drop table at standard distances with in-zone indicator

## Installation

1. Download OptimalZero_v20.zip from the release below
2. Transfer to your Android device
3. Open Files app on device
4. Tap the zip file directly — ATAK will import automatically
5. Open ATAK → TAK Package Mgmt → find Optimal Zero → tap Load

## Validation Test

- Barrel: 10.5 inch
- Ammo: 5.56x45 / 55gr M193
- HOB: 3.475 inch
- Vital Zone: 6 inch
- Expected: ~50yd zero, ~250-260yd MPBR

## Changelog

### v20 (April 26, 2026)

- Expanded to 35 cartridges across 10 calibers
- 300 BLK expanded from 2 to 6 loads
- Added .338 Lapua and .50 BMG calibers
- Expanded 6.5 CM, 7.62x51, 5.56x45, 9mm grain weight selections
- Barrel lengths expanded to 33 options from 3.5 to 26 inches
- Short barrel support added: 3.5, 4, 4.5, 5, 5.5, 6, 6.5 inch

### v19 (April 25, 2026)

- Added 9mm 147gr Subsonic, 5.56x45 70gr TSX, 5.7x28 27gr HP, 5.7x28 40gr SS190
- Expanded barrel options to 25 with fractional sizes

### v18 (April 25, 2026)

- Fixed Play Store ATAK 5.5+ compatibility
- ProGuard obfuscation mapping fix
- TAK.gov approved and signed

## Developer

Stephan Pellegrini — saltyoperatorarizona

https://github.com/saltyoperatorarizona
