# Delivery Record — Release Notes

## Version 2.0.0 — "Delivery Record 2.0"
**Release Date:** April 12, 2026
**Branch:** `Trunk`

---

### Highlights

Major overhaul of the OCR extraction engine. The app no longer requires field labels like "Name:", "Address:", or "Phone:" on invoices to extract customer data. Field detection is now heuristic-based, working on any standard invoice layout. The manual extraction UI has been streamlined — the chip banner and assign dropdown are removed.

---

### OCR Engine — Label-Free Field Detection

#### Address Detection (no "Address:" label required)
- **Street address pattern matching** — Recognizes lines starting with a street number followed by common street suffixes (St, Ave, Blvd, Dr, Rd, Ln, Way, Ct, Pl, Cir, Hwy, Pkwy, Apt, Suite, Ste, Unit, and their full-word variants).
- **City/State/ZIP detection** — Matches any US city + two-letter state abbreviation + 5-digit (or 5+4) ZIP code pattern. All 50 states + DC supported.
- **Multi-line address assembly** — When a street line is followed by a city/state/zip line, they're automatically combined into one address.
- **Reverse lookup** — If only a city/state/zip line is found, checks the preceding line for a street number to build the full address.

#### Name Detection (no "Name:" label required)
- **Person name heuristic** — Identifies lines that are mostly alphabetic (1-5 words, >70% letters), not a phone number, not an address, and don't contain business terms (invoice, total, qty, description, etc.).
- **Exclusion list** — 30+ non-name keywords filtered out: invoice, order, date, total, description, qty, model, serial, phone, email, payment, tax, subtotal, etc.
- **Positional awareness** — In bill-to sections, name is extracted from the first line that isn't an address or phone number.

#### Section Header Detection
- **Expanded headers** — Now searches for: "Bill To", "Sold To", "Deliver To", "Customer", "Client", "Ship To", "Buyer" (previously only "BILL TO").
- **Inline header content** — Text on the same line as a header (e.g., "Bill To: John Smith") is now captured.
- **Section boundary detection** — Stops reading section lines when hitting keywords like "Ship To", "Description", "Qty", "Total", "Amount", "Item", "Subtotal".

#### Label Stripping
- When labels ARE present (Name:, Address:, Phone:, Tel:, etc.), they're stripped cleanly before extraction. Both labeled and unlabeled invoices work.

#### Always-Fallback Strategy
- OCR now ALWAYS runs the heuristic fallback for any fields not filled by section parsing, instead of only falling back when no section header was found.

---

### Manual Extraction UI

#### Removed
- **Chip banner** — The gray `HorizontalScrollView` with field chips (Invoice #, Name, Address, Phone, etc.) and green checkbox icons is removed.
- **Assign dropdown** — The `Assign ▼` button and inline preview `EditText` are removed.

#### Improved Auto-Detection (Bottom Sheet)
- **Address detection** — Now recognizes street address patterns and city/state/zip lines without requiring both simultaneously.
- **Name detection** — New heuristic flags text as a likely person name when it's 1-4 alphabetic words with no numbers, addresses, or business terms.
- Bottom sheet field assignment buttons still highlight auto-detected fields for one-tap assignment.

---

### Files Changed

**OCR Engine:**
- `OCRProcessorMLKit.java` — New address/name/section patterns; rewritten `extractFromBillToSection`, `extractWithFallback`; new helpers: `isStreetAddress`, `isCityStateZip`, `isLikelyPersonName`, `stripFieldLabel`, `findSectionHeader`

**Manual Extraction:**
- `ManualExtractionActivity.java` — Removed chip UI code, improved `autoDetectFields` with address and name heuristics
- `activity_manual_extraction.xml` — Removed `layoutPreview`, `chipGroupFields`, `btnAssignField`, `tvResultPreview`

**Build:**
- `app/build.gradle` — versionCode 200, versionName "2.0.0"
- `PROJECT_INFO.md` — Updated version

---

### Technical Notes
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 35 (Android 15)
- **OCR Engine:** Google ML Kit Text Recognition
- **Build:** Gradle, AGP 9.1.0, Java 17

---

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
