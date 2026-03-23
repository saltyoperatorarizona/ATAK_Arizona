# AIS-Direct Plugin for ATAK
**Version:** 1.0 — ATAK-CIV | Free & Open Source

## Download

| Version | Type | Status |
|---------|------|--------|
| v1.0 | Debug Signed | Available |
| v1.0 | TAK.GOV Signed | Submitted — Pending Approval |

> ⚠️ **BETA — Requires SDK ATAK (Developer Build)**
> This beta APK is debug-signed and will **NOT** work with Play Store ATAK. You must have the SDK developer build of ATAK installed on your device.
> AIS-Direct v1.0 has been submitted to TAK.GOV for official certificate signing — pending approval for Play Store ATAK compatibility.

---

## About This Plugin

AIS-Direct is a free, open source vessel tracking plugin for ATAK. Designed to function like ADSB-Direct (the popular aircraft tracking plugin), AIS-Direct brings real-time Automatic Identification System (AIS) data directly into the ATAK interface — allowing operators to monitor maritime vessel traffic worldwide without ever leaving the app.

Users can connect to live AIS data streams and display vessel positions, headings, speeds, and call signs directly on the ATAK map. Stream data can be filtered by geographic range or reference location, giving operators the ability to focus on vessels within a defined operational area. A dedicated **Military quick filter** allows users to instantly isolate military vessels from the broader traffic picture.

AIS-Direct supports multiple data source backends including AISStream (WebSocket), MarineTraffic, and direct NMEA/TCP connections, making it compatible with a wide range of AIS receivers and online services.

![About This Plugin in-app screen](images/03_about_plugin.png)

---

## How to Access

Once installed, open the ATAK Tools menu and tap **AIS-Direct** to launch the plugin. It sits alongside your other ATAK tools — including [OSINT](../OSINT) — for quick access during maritime operations.

![ATAK Tools Menu showing AIS-Direct](images/01_atak_tools_menu.png)

---

## AIS-Direct Main Panel

The main panel displays the current connection status and stream configuration. Toggle AIS-Direct ON or OFF directly from the panel header. Three data source tabs are available at the top:

- **AISStream** — WebSocket-based live global AIS feed (wss://stream.aisstream.io/v0/stream)
- **MarineTraffic** — Integration with MarineTraffic API
- **NMEA/TCP** — Direct connection to a local AIS receiver or NMEA TCP feed

![AIS-Direct Main Panel with stream tabs and filter options](images/02_ais_direct_panel.png)

---

## Vessel Filters

### All Vessels vs. Military Quick Filter

The FILTER section provides two radio options:

- **All Vessels** — Displays every AIS-broadcasting vessel within your configured range and location
- **Military** — Instantly filters the map to show only military vessels, enabling rapid maritime domain awareness in operational environments

The Military quick filter is one of AIS-Direct's standout features, allowing warfighters and maritime operators to declutter the map and focus exclusively on relevant traffic.

---

## Reference Location & Query Range

AIS-Direct allows users to define exactly where they want to monitor vessel traffic:

**Reference Location:**
- **Map Center** — Stream vessels centered on your current map view
- **My Location** — Stream vessels based on your device GPS position

**Query Range (NM):** Set the nautical mile radius for vessel queries (e.g., 120 NM)

**Update Interval (S):** Configure how frequently the plugin polls for new vessel data (e.g., every 10 seconds)

This range and location system mirrors the functionality of ADSB-Direct, bringing the same intuitive geographic filtering to the maritime domain.

---

## AISStream Configuration

Tapping the AISStream tab opens the stream configuration dialog:

**API Key** — Enter your AISStream API key to authenticate with the live feed

**API URL** — Default: `wss://stream.aisstream.io/v0/stream`

**Broadcast CoT** — When enabled, vessel positions are broadcast as Cursor on Target (CoT) events to your ATAK team network, enabling shared maritime awareness across all connected devices

![AISStream Config — API Key, URL, and Broadcast CoT](images/04_aisstream_config_api.png)

---

## AIS Stream Filters

Advanced filtering options are available to narrow down vessel data:

**MMSI Filter (CSV)** — Enter specific vessel MMSI numbers separated by commas to track only those specific vessels (e.g., 368207620,367719770)

**Vessel Type Filter (AIS Codes)** — Filter by AIS vessel type codes to display only specific vessel categories (e.g., 30,60,70 for fishing, passenger, and cargo vessels)

**Reset Defaults** — Clears all filter settings back to default values

![AISStream Config — MMSI Filter and Vessel Type Filter](images/05_aisstream_config_filters.png)

---

## Live Vessel Tracking on Map

When active, AIS-Direct populates the ATAK map with vessel icons showing real-time position, heading, speed, and call sign. Each vessel displays a data callout including:

- **Call Sign**
- **MMSI / CoT ID**
- **Altitude (MSL)**
- **Heading (degrees)**
- **Speed (MPH)**
- **Accuracy (+/- meters)**

Vessels are rendered with directional heading indicators, allowing operators to quickly assess vessel movement and intent across large maritime areas — from coastal waters to open ocean.

---

## TAK.GOV Certificate Status

AIS-Direct v1.0 has been **submitted to TAK.GOV** for official certificate signing. Once approved, the TAK.GOV-signed APK will be compatible with the Play Store version of ATAK and will be made available here for download.

| Milestone | Status |
|-----------|--------|
| Plugin Development | ✅ Complete |
| Debug APK (Developer Build) | ✅ Available |
| TAK.GOV Submission | ✅ Submitted |
| TAK.GOV Certificate Approval | ⏳ Pending |
| Play Store ATAK Compatible Release | ⏳ Awaiting Approval |

---

## About the Developer

Stephan Pellegrini is a military defense professional with extensive experience in ISR systems, situational awareness platforms, and tactical operations. Passionate about ATAK and the broader TAK ecosystem, he developed this plugin as a free resource for the operator community.

AIS-Direct is a companion plugin to [OSINT](../OSINT) — the open source intelligence feed aggregator for ATAK — with additional plugins continuing in development.

---

## Contact & License

**Contact:** saltyoperatorarizona@gmail.com

This plugin is provided free of charge with no warranty. Use at your own discretion. Not affiliated with or endorsed by the TAK Product Center.
