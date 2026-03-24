# CODEC — Team Biometric Uplink Plugin

![CODEC HUD](CODEC-HUD-Preview.png)

## Overview

CODEC is a free, open-source ATAK plugin that monitors real-time heart rate data from Bluetooth Low Energy (BLE) heart rate sensors and displays it in a military video game-style HUD for the entire team. Named after the iconic codec communication screen from Metal Gear Solid, the plugin gives team leaders and operators a live view of every member's biometric status — directly inside ATAK.

Compatible with any standard BLE heart rate monitor (Polar H10, Wahoo TICKR, Garmin HRM-Pro, Coospo HW807, and others). No proprietary hardware required.

---

## Features

- Live BPM display per operator with animated EKG waveform

- Multi-operator grid — 1 column portrait, 2 column landscape, auto-switching

- Four operator states with distinct color coding:

  - 🟢 ALIVE — normal heart rate, green EKG

  - 🟡 TACHYCARDIA — HR above threshold, amber EKG and warning label

  - 🔵 BRADYCARDIA — HR below threshold, blue EKG and warning label

  - 🔴 OPERATOR DOWN — signal lost beyond KIA timeout, red banner with glitch effect

- HR trend indicator — arrow shows rising ↑, falling ↓, or stable →

- BLE signal strength (RSSI) and time since last reading per card

- Configurable KIA timeout — operator marked down after X seconds with no signal (default 60s)

- Per-operator HR thresholds — custom bradycardia and tachycardia limits per team member

- Three team setup methods — scan and pair, auto-match from ATAK contacts, or pre-configure roster

- Persistent team roster — members and thresholds saved across sessions

- CoT integration — HR broadcast as remark on existing SA contact, visible to all ATAK clients

- MGSV-inspired dark HUD — scanline overlay, monospace fonts, animated EKG, transmission bars

---

## How It Works

CODEC uses Android's Bluetooth Low Energy (BLE) GATT stack to connect directly to heart rate monitors broadcasting the standard Heart Rate Service (UUID 0x180D). This is the same open Bluetooth standard used by fitness apps — no proprietary SDK or account required.

Data flow:

1. Operator powers on their BLE HR monitor

2. CODEC scans for devices advertising the Heart Rate Service

3. On selection, CODEC connects via GATT and subscribes to HR Measurement notifications

4. Device pushes HR updates continuously (typically every 1 second)

5. CODEC updates the operator card in real time — BPM, trend, EKG animation

6. If no HR data is received within the KIA timeout window, card flips to OPERATOR DOWN

7. HR is simultaneously broadcast as a CoT remark to the TAK server for all connected clients

Compatible HR monitors (any BLE device supporting Heart Rate Service 0x180D):

- Polar H10, H9, OH1

- Wahoo TICKR, TICKR X

- Garmin HRM-Pro, HRM-Dual

- Coospo HW807, HW706

- Magene H64

- Most chest straps and armbands produced after 2018

---

## Card States

| State | Color | Trigger |
|-------|-------|---------|
| ALIVE | 🟢 Green | Active HR signal, BPM within normal range |
| TACHYCARDIA | 🟡 Amber | BPM above operator threshold (default >150) |
| BRADYCARDIA | 🔵 Blue | BPM below operator threshold (default <50) |
| OPERATOR DOWN | 🔴 Red | No HR signal received beyond KIA timeout |
| OFFLINE | ⚫ Gray | BLE connection dropped |

---

## Download

### ⚠️ BETA Release

| Version | Type | Status |
|---------|------|--------|
| v1.0.0-BETA | Debug Signed | Available |
| v1.0.0 | TAK.GOV Signed | Pending Submission |

[Download CODEC-1.0.0-debug.apk](CODEC-1.0.0-debug.apk)

> ⚠️ BETA — Requires SDK ATAK (Developer Build)

> This beta APK is debug-signed and will NOT work with Play Store ATAK.

> You must have the SDK developer build of ATAK installed on your device.

> TAK.GOV signed release pending for full Play Store compatibility.

---

## Installation

### Option 1 — Sideload APK (Recommended for Beta)

1. Download the APK above

2. Transfer to your Android device via USB or cloud storage

3. Enable Install from Unknown Sources in Android Settings → Security

4. Tap the APK to install

5. Open ATAK → Plugin Manager → locate CODEC → tap to load

6. Grant Bluetooth and Location permissions when prompted

### Option 2 — Build From Source

1. Clone this repository

2. Open the CODEC folder in Android Studio

3. Place the ATAK-CIV 5.6 SDK APK locally and update local.properties with its path

4. Set build variant to civDebug

5. Build and run on a device with SDK ATAK installed

---

## Requirements

- ATAK-CIV 5.6+ (SDK build required for beta)

- Android 5.0+ (API 21 minimum), Android 12+ recommended

- Bluetooth Low Energy capable device

- One or more BLE heart rate monitors (Heart Rate Service 0x180D)

---

## Permissions

| Permission | Purpose |
|------------|---------|
| BLUETOOTH_SCAN | Discover nearby BLE HR monitors |
| BLUETOOTH_CONNECT | Connect to and receive data from HR monitors |
| ACCESS_FINE_LOCATION | Required by Android OS for BLE scanning |

---

## About the Developer

Stephan Pellegrini is a military defense professional with 14+ years of experience supporting ISR systems, situational awareness platforms, and tactical operations across multiple OCONUS deployments. These plugins are built as free tools for the operator community.

📧 saltyoperatorarizona@gmail.com

🌐 github.com/saltyoperatorarizona

---

## Other Plugins

| Plugin | Description | Status |
|--------|-------------|--------|
| [OSINT](../OSINT/) | Open source intelligence feed aggregator, 80 preset RSS feeds across Defense, Intel, Geopolitics and Regional | Available (BETA) — TAK.GOV Submitted |
| [AIS-Direct](../AIS-Direct/) | Vessel tracking via AIS data feeds, CoT markers on the ATAK map | Available (BETA) — TAK.GOV Submitted |
| CODEC | Team biometric monitoring via BLE HR sensors | Available (BETA) — Testing In Progress |

---

## License

Free to use. No warranty. Not affiliated with or endorsed by the TAK Product Center.
