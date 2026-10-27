# PAWTH — Remaining Work and Recommended Priorities

Assessment date: 24 October 2026

The system has a working UI and several database actions, but the main workflows still need completion. Notifications, adoption processing, and location data should come before adding more screens.

## Remaining work

| Priority | Area | What exists now | What still needs work |
|---|---|---|---|
| 1 | Notifications | Reads saved notifications, filters categories, and marks all as read | Automatically create notifications when reports, assignments, case statuses, and applications change |
| 1 | Adoption workflow | Submits an application as NEW; staff can view it | Consultation → home check → approval/rejection, applicant tracking, duplicate prevention, and updating the animal's availability |
| 1 | Rescue workflow | Creates reports, calculates priority, updates status/assignment/notes | Enforce valid status transitions, record who changed what and when, and notify the reporter |
| 1 | Accounts and permissions | Login, workspace selection, read-only profiles | Registration, profile editing, password changes, and permission checks on database operations—not just screen navigation |
| 2 | Stray hotspots | Counts reports by barangay | Store coordinates, normalize location names, add date/status filters, and display an interactive map |
| 2 | Animal management | Displays Buddy and updates adoption status | Add/edit/archive animal records, manage photos and complete animal details; remove the temporary Buddy filter when appropriate |
| 2 | Matching engine | Calculates a basic compatibility score | Fix the size/species comparison bug and define meaningful matching criteria |
| 2 | Photo handling | Saves a local file path | Store a managed copy of the image so moving or deleting the original does not break the report |
| 3 | Statistics | Current counts and barangay totals | Historical rescue/adoption charts, date filters, completion rates, and reliable trend calculations |
| 3 | Visits | Displays a consultation schedule string | Actual appointment slots, booking, cancellation, capacity limits, and reminders |
| 3 | Deployment | Runs against local MySQL | Installation packaging, backups, configuration management, and a shared-server approach if several computers will use it |
| Later | Help/chatbot | No working backend | Start with useful FAQs; add a chatbot only if it serves a defined requirement |

## Working notifications

Notifications need event generation first. The current code never inserts new notifications during normal application actions; the existing entries come from seed data.

Expected events:

- A new rescue report notifies the appropriate shelter staff.
- A staff status update notifies the reporter.
- A new adoption application notifies staff.
- An application decision notifies the applicant.

Save the business change and its notification together in a database transaction. Then add an unread badge and periodic refresh.

No external notification API is needed for basic in-app notifications. Email or SMS can come later.

## Stray hotspot mapping

The missing foundation is accurate location data. Reports currently contain barangay, street, and landmark text, but no latitude or longitude.

A mapping API provides the map and address lookup. PAWTH must calculate hotspots from its own rescue reports.

### Suggested combination to evaluate

| Component | Suggested choice | Purpose |
|---|---|---|
| Swing map display | JXMapViewer2 | Renders map tiles inside the existing Java Swing application |
| Map tiles and address lookup | Geoapify | A candidate provider for the map background and converting addresses into coordinates |
| Hotspot calculations | PAWTH's own database | Groups and ranks submitted reports by location, period, priority, and status |

References:

- [JXMapViewer2 project](https://github.com/msteiger/jxmapviewer2)
- [Geoapify pricing and plan details](https://www.geoapify.com/pricing/)

Geoapify's published free plan included 3,000 credits per day when checked for this assessment. Verify current quotas and terms before implementation.

Before committing to a provider:

1. Test address lookup against the actual barangays and landmarks the system will cover.
2. Add latitude and longitude fields to rescue reports.
3. Let reporters confirm or adjust a map pin.
4. Preserve the original address and record whether coordinates were confirmed.
5. Add date and case-status filters.
6. Build markers or clusters from PAWTH's saved reports.
7. Cache geocoding results and handle unavailable map services.
8. Display the attribution required by the chosen provider.

Ambiguous addresses should not silently become precise-looking markers.

Public Nominatim is another geocoding option, but its public service limits usage to one request per second and prohibits client-side autocomplete. It should not power search-as-you-type.

- [Nominatim usage policy](https://operations.osmfoundation.org/policies/nominatim/)

## Existing logic issues

### Compatibility matching

The matching engine compares an animal's species against "Small". Size needs its own animal field; otherwise that rule cannot work correctly.

Define explicit criteria based on the profile and animal information actually stored. Avoid presenting the current basic score as a comprehensive compatibility assessment.

### Password handling

Password handling currently accepts plaintext seed passwords and uses unsalted SHA-256.

Before introducing real accounts:

- Replace this with a dedicated password-hashing scheme.
- Migrate the demo credentials.
- Add role and ownership checks to sensitive operations.
- If the application is deployed across several computers, review whether clients should use an authenticated backend service rather than direct shared database credentials.

## Recommended implementation sequence

1. Notification generation.
2. Complete adoption processing.
3. Rescue history and validation.
4. Coordinate capture.
5. Hotspot map.

This sequence gives the map meaningful data and makes the current UI behave like a complete system.

## Scope of this document

This is an assessment and proposed roadmap, not a record of newly implemented features. The Buddy-only catalogue remains a temporary display filter, and unsupported workflows still require backend work.
