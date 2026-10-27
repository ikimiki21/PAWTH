# PAWTH — Foundational UI

PAWTH is a Java Swing application for stray-animal rescue reporting and adoption. This branch adds the foundational community and shelter interfaces using FlatLaf and the official Figma reference.

## Current implementation

- Role-based Community and Staff sign-in.
- Community dashboard, animal browsing, animal details, and adoption submission.
- Rescue reporting, priority scoring, report progress, and staff case updates.
- Shelter overview, read-only adoption review, animal availability controls, and barangay report counts.
- User notifications with category filters and mark-all-as-read.
- Read-only profiles and logout.
- Conventional multi-line Java formatting.

The interface currently shows **Buddy only** as placeholder content. The other seeded animals remain in the database. The temporary display filters are in view/components/WorkspaceData.java.

Enabled submit and save actions persist real changes to MySQL. The application is a foundation rather than a completed production system; the remaining-work report is included below.

## Requirements

- JDK 17 or newer.
- IntelliJ IDEA with a configured Java SDK, or a terminal with java and javac on PATH.
- A running MySQL server. The local development setup was verified with MySQL 8.4.13.
- No Maven or Gradle setup is required.

Local JAR dependencies are included in lib/ and attached to the IntelliJ module:

| Library | Version |
|---|---|
| FlatLaf | 3.7.2 |
| FlatLaf Extras | 3.7.2 |
| JSVG | 2.1.0 |
| MySQL Connector/J | 26.7.0 |

The font and its license are in assets/fonts/. Figma assets and the seeded animal photos are bundled locally.

## Set up the database

1. Start MySQL.
2. Import PAWTHDB.sql using MySQL Workbench or the MySQL command-line client.

   **The initialization script drops an existing pawth_db before recreating it. Run it only against a development database you intend to initialize.**

3. Create a local application account if needed, using a MySQL administrator connection:

   ~~~sql
   CREATE USER 'pawth_app'@'127.0.0.1'
       IDENTIFIED BY 'REPLACE_WITH_YOUR_LOCAL_PASSWORD';

   GRANT SELECT, INSERT, UPDATE, DELETE
       ON pawth_db.*
       TO 'pawth_app'@'127.0.0.1';
   ~~~

4. Copy config/database.properties.example to config/database.properties.
5. Replace the sample password and adjust the connection settings to match your local server.

The actual properties file, local MySQL runtime, and database files are excluded from Git. No database password is supplied by this repository.

### Optional local-server scripts

Start-MySQL.cmd and Stop-MySQL.cmd support the previously prepared project-local MySQL runtime. The server itself is **not** included in Git.

Those scripts expect the MySQL 8.4.13 binaries under runtime/mysql/mysql-8.4.13-winx64/, plus local my.ini, admin.ini, and initialized data. If you installed MySQL separately, start that installation normally instead.

## Run in IntelliJ

1. Open the repository folder as a project.
2. Choose your installed JDK under **File → Project Structure → Project SDK**.
3. The included PAWTH.iml maps the root source directory to the com.pawth package prefix and includes the local JAR libraries.
4. Select the **PAWTH** run configuration, whose main class is com.pawth.Main.
5. Use the repository root as the working directory so local configuration, fonts, and images resolve correctly.
6. Run the project.

If the project was already open before pulling this branch, reload or reopen it to pick up the module libraries.

### Terminal build on Windows

From the repository root:

~~~powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Build.ps1
java -cp "lib/*;out/production/PAWTH" com.pawth.Main
~~~

## Demo accounts

These accounts are created by PAWTHDB.sql. Select the matching workspace on the login screen.

| Workspace | Email | Password |
|---|---|---|
| Community | user@gmail.com | password123 |
| Staff | shelter.staff@gmail.com | staff123 |

## Reports

- [Interactive controls, read-only elements, and unavailable features](docs/interactivity-report.md)
- [Remaining work and recommended priorities](docs/remaining-work.md)

The remaining-work Markdown report is also embedded below for convenient review on the repository homepage. Its assessment date is 24 October 2026; provider quotas are historical observations and should be checked before integration.

---

## Remaining-work report

Assessment date: 24 October 2026

The system has a working UI and several database actions, but the main workflows still need completion. Notifications, adoption processing, and location data should come before adding more screens.

### Remaining work

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

### Working notifications

Notifications need event generation first. The current code never inserts new notifications during normal application actions; the existing entries come from seed data.

Expected events:

- A new rescue report notifies the appropriate shelter staff.
- A staff status update notifies the reporter.
- A new adoption application notifies staff.
- An application decision notifies the applicant.

Save the business change and its notification together in a database transaction. Then add an unread badge and periodic refresh.

No external notification API is needed for basic in-app notifications. Email or SMS can come later.

### Stray hotspot mapping

The missing foundation is accurate location data. Reports currently contain barangay, street, and landmark text, but no latitude or longitude.

A mapping API provides the map and address lookup. PAWTH must calculate hotspots from its own rescue reports.

#### Suggested combination to evaluate

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

### Existing logic issues

#### Compatibility matching

The matching engine compares an animal's species against "Small". Size needs its own animal field; otherwise that rule cannot work correctly.

Define explicit criteria based on the profile and animal information actually stored. Avoid presenting the current basic score as a comprehensive compatibility assessment.

#### Password handling

Password handling currently accepts plaintext seed passwords and uses unsalted SHA-256.

Before introducing real accounts:

- Replace this with a dedicated password-hashing scheme.
- Migrate the demo credentials.
- Add role and ownership checks to sensitive operations.
- If the application is deployed across several computers, review whether clients should use an authenticated backend service rather than direct shared database credentials.

### Recommended implementation sequence

1. Notification generation.
2. Complete adoption processing.
3. Rescue history and validation.
4. Coordinate capture.
5. Hotspot map.

This sequence gives the map meaningful data and makes the current UI behave like a complete system.

### Scope of this document

This is an assessment and proposed roadmap, not a record of newly implemented features. The Buddy-only catalogue remains a temporary display filter, and unsupported workflows still require backend work.
