# OptimalZero — ATAK MPBR Ballistics Plugin

Version 18 | April 25, 2026 | TAK.gov Approved

Weapons zero calculator for ATAK. Finds the optimal zero distance to maximize Max Point Blank Range (MPBR) for your specific rifle setup.

## Compatibility

- ATAK CIV 5.5+
- ATAK CIV 5.6.0.x (Play Store and TAK.gov builds)
- TAK.gov approved and signed

## Features

- G1 ballistic drag model (JBM/Ingalls standard)
- 12 preset cartridges:
  - 5.56x45: M193 55gr, M855 62gr, Mk262 77gr
  - 7.62x51: M80 147gr, M118LR 168gr, 175gr HPBT
  - 7.62x39: 123gr FMJ
  - 300 BLK: 125gr Supersonic, 220gr Subsonic
  - 6.5 Creedmoor: 140gr ELD-M
  - 9mm: 115gr and 124gr FMJ
- Barrel length to muzzle velocity lookup with manual override
- Height over bore and riser height inputs
- Vital zone input (default 6")
- Trajectory chart with vital zone band overlay
- Drop table at standard distances with in-zone indicator

## Installation

1. Download OptimalZero_v18.zip from the release below
2. Transfer to your Android device
3. Open Files app on device
4. Tap the zip file directly — ATAK will import automatically
5. Open ATAK → TAK Package Mgmt → find Optimal Zero → tap Load

## Validation Test

- Barrel: 10.5"
- Ammo: 5.56x45 / 55gr M193
- HOB: 3.475"
- Vital Zone: 6"
- Expected: ~50yd zero, ~250-260yd MPBR

## Changelog

### v18 (April 25, 2026)

- Fixed compatibility with Play Store ATAK 5.5+ and 5.6.0.x
- Added -applymapping ProGuard for correct runtime obfuscation
- Fixed icon drawable (resolves InflateException on Play Store ATAK)
- Fixed PluginLayoutInflater import path
- Fixed SDK bundling — compileOnly only
- TAK.gov approved and signed

### v3.0

- Previous release — ATAK 5.6.0 compatible

## Developer

Stephan Pellegrini
