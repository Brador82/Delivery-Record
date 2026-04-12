# Daily Record

## Project Overview
- **App Name**: Daily Record
- **Application ID**: com.brador.dailyrecord
- **Internal Package**: com.mobileinvoice.ocr
- **Version**: 2.0.0 (versionCode 200)
- **Origin**: Forked from Mobile Invoice Assistant v1.3.4
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 35 (Android 15)
- **Language**: Java 17

## What It Does
Daily Record is an Android app for delivery crews. It captures invoices via camera, extracts customer and item data using OCR, manages delivery routes with GPS optimization, collects proof-of-delivery photos and signatures, and sends follow-up SMS to customers.

## Key Activities
- **MainActivity** — Dashboard with drag-reorder invoice list
- **CameraActivity** — Camera capture (landscape, document detection)
- **InvoiceDetailActivity** — Customer/item editing, POD photos, signature
- **InvoiceLibraryActivity** — Invoice history browser
- **ManualExtractionActivity** — Draw-selection OCR fallback
- **SignatureActivity** — Fullscreen signature capture
- **BroadcastMessageActivity** — Bulk SMS messaging
- **SettingsActivity** — 20+ app preferences
- **RouteMapActivity** — Google Maps route optimization

## Source Structure
```
app-src/
├── ocr/          — Core invoice & routing system
│   └── database/ — Room DB (Invoice entity, v10)
└── delivery/     — MVVM delivery module
    ├── data/     — DAO, entities, repository
    ├── models/   — DeliveryStatus, Priority enums
    ├── ui/       — Activities, adapters
    ├── utils/    — RouteOptimizer, DeliveryHelper
    └── viewmodel/
```

## Tech Stack
| Layer | Technology |
|-------|-----------|
| OCR | Google ML Kit 16.0.1, PaddleOCR via ONNX Runtime 1.19 |
| Camera | CameraX 1.4.1 |
| Maps | Google Maps SDK 19.0, Play Services Location 21.3 |
| Database | Room 2.6.1 (SQLite) |
| UI | Material Design 3, ViewBinding |
| Async | RxJava 3, Kotlin Coroutines |
| Export | Apache POI 5.2.5 (Excel), Google Drive API |

## Permissions
Camera, Internet, Storage, SMS, Location, Contacts, Network State

## Build
```
./gradlew assembleDebug
./gradlew assembleRelease   # requires keystore.properties
```
