# Mobile Invoice Assistant — Release Notes

## Trunk — 2026-04-04

### Bug Fixes

#### OCR Extraction
- **Company address/phone no longer captured as customer's** — The OCR parser now properly distinguishes company letterhead from the customer section. Three targeted fixes:
  - Section detection expanded beyond "BILL TO" to also recognize "SHIP TO", "DELIVER TO", "SOLD TO", "CUSTOMER:", and "DELIVERY ADDRESS"
  - Unlabeled customer fields (no `Name:` / `Address:` prefix) are now parsed from lines directly after the section marker
  - Fallback raw-address scan skips the first 8 lines (company header zone) instead of scanning from line 0

---

## Version 1.3.4 (Pixel Build)
**Release Date:** March 9, 2026
**Branch:** `pixel-v1.3.3`

---

### Highlights

This release delivers major OCR accuracy improvements, a complete customer messaging system with Google review integration, and several quality-of-life enhancements for delivery crews in the field.

---

### New Features

#### Enhanced OCR Extraction Engine
- **Comprehensive item parsing** — OCR now extracts serial numbers, model numbers, make/brand, item types, and service flags (Delivery, Install, Haul-Away) from scanned invoices.
- **Brand detection** — Recognizes 30+ appliance brands (Whirlpool, Samsung, LG, GE, Maytag, Frigidaire, Bosch, etc.) and maps them to each delivery item.
- **Alternate phone number support** — Extracts multiple phone numbers while intelligently suppressing company/store header numbers to avoid false positives.
- **Per-item service flags** — Each delivery item carries its own service indicators (delivery, install, haul-away) parsed from OCR text.
- **Rotated/sideways invoice support** — Improved text extraction order handles invoices scanned at any orientation.

#### Customer Messaging System
- **Follow-up messages on delivery completion** — When a delivery is marked complete, the app prompts the driver to send an SMS to the customer.
- **Three message options** — Choose from the main Follow-Up Message, Quick Message 1, or Quick Message 2 — all fully customizable in Settings.
- **Smart placeholders** — Message templates support `{name}`, `{company}`, `{items}`, `{team}`, `{review_url}`, and `{review_text}` for dynamic personalization.
- **Delivery Team branding** — Set a custom delivery team name (e.g., "A4L Delivery Crew") that gets tagged in every customer-facing message and suggested review.
- **Pre-written Google review** — Compose a suggested 5-star review template that gets sent to the customer, pre-filled with their items and team name. Customers just copy, paste, and submit.
- **Auto URL shortening** — Google review URLs are automatically shortened via TinyURL for cleaner SMS messages, with graceful fallback to the full URL.
- **Editable in Settings** — All messages, the delivery team name, review URL, and suggested review text are fully editable from the Customer Messaging section in Settings.

#### Visual Delivery Completion
- **Greyed-out completed deliveries** — Cards for completed deliveries fade to 45% opacity as a visual aid. All card functions (call, navigate, view details, delete) remain fully operational.

#### Service Type Badges
- **Visual service badges** — Each invoice card now displays colored badges for Delivery, Install, Haul-Away, and Service Call, making it easy to see the scope of work at a glance.
- **Delivery/Haul-Away option** — Added as a new service type alongside the existing Delivery, Delivery and Install, Delivery/Install/Haul-Away, and Service Call options.

---

### Improvements

#### OCR & Data Extraction
- Improved Bill-To section parsing for customer name, address, and phone number accuracy.
- Store/company phone number suppression — header phone numbers from the store are identified and excluded from customer phone assignment.
- Phone number formatting standardized to `(XXX) XXX-XXXX`.
- Global service flag extraction from full OCR text applied to all items when per-item flags aren't found.

#### Manual Extraction
- **Crash fixes** — Null guards in `updateChipState` and region processing prevent crashes when processing selected text.
- **OCR fallback for draw-selection** — When draw-selection yields no pre-scanned text, OCR runs directly on the selected region.
- Thread safety improvements for `charRegions` / `textRegions` in `SelectionOverlayView`.

#### Signature Capture
- Signature div rendering fix.

#### Database
- Room database migrated to version 10 with `altPhone` column support (`MIGRATION_9_10`).

---

### Settings — New Fields

| Setting | Location | Description |
|---|---|---|
| Send follow-up on delivery complete | Customer Messaging | Toggle to enable/disable the SMS prompt |
| Delivery Team Name | Customer Messaging | e.g., "A4L Delivery Crew" |
| Google Review URL | Customer Messaging | Your Google Maps review link (auto-shortened) |
| Suggested Review | Customer Messaging | Pre-written 5-star review template for customers |
| Follow-Up Message | Customer Messaging | Main SMS template sent on delivery completion |
| Quick Message 1 | Customer Messaging | Alternate message option |
| Quick Message 2 | Customer Messaging | Alternate message option |

### Message Placeholders Reference

| Placeholder | Expands To |
|---|---|
| `{name}` | Customer's name from invoice |
| `{company}` | Company name from Settings |
| `{items}` | Delivered items list |
| `{team}` | Delivery team name (or "[Company] delivery team" if not set) |
| `{review_url}` | Shortened Google review URL (falls back to full URL) |
| `{review_text}` | Expanded suggested review with all placeholders filled |

---

### Files Changed (from base v1.3.3)

**Core Logic:**
- `OCRProcessorMLKit.java` — OCR extraction engine overhaul
- `MainActivity.java` — Follow-up message flow, alt phone/services wiring
- `DeliveryItem.java` — Added `make` and `services` fields
- `ItemsHelper.java` — JSON serialization for new fields
- `InvoiceAdapter.java` — Service badges, completed overlay, abbreviations

**Settings & Config:**
- `AppSettings.java` — 9 new preference keys, `expandMessage()` with 6 placeholders
- `SettingsActivity.java` — Customer Messaging UI bindings, URL shortening
- `activity_settings.xml` — Customer Messaging card with 7 new input fields

**Manual Extraction & Selection:**
- `ManualExtractionActivity.java` — Crash fixes, OCR region fallback
- `SelectionOverlayView.java` — Thread safety, null guards

**Database:**
- `Invoice.java` — `altPhone` field
- `InvoiceDatabase.java` — Migration 9→10

**UI Resources:**
- `item_invoice.xml` — Service badge layout
- `badge_service_dim.xml` — Dimmed badge drawable

---

### Technical Notes

- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **OCR Engine:** Google ML Kit Text Recognition
- **URL Shortening:** TinyURL free API (no API key required, 5s timeout)
- **Database:** Room (SQLite) with auto-migrations
- **Build:** Gradle 8.x, AGP, Java 8 source compatibility
