# PAWTH interactivity report

Updated: 24 October 2026
Project: PAWTH (repository root)

## Requested display changes

- Removed the complete "Nori is safe" card from the login screen, including its picture and rescue-story text.
- Buddy (seeded animal ID 1) is the only animal displayed in recommendations, adoption listings, and staff inventory.
- Animal counters use that same Buddy-only list. The staff application queue is also limited to Buddy.
- Buddy retains the normal card width and image proportions.
- Other animal records and files remain stored; this is a temporary UI filter, not database deletion.
- Rescue reports, user profiles and notifications remain real account data.

**Buddy is placeholder content, but its enabled actions are connected to the database.** Confirming an adoption application, saving an animal status, saving a rescue report/case update, or marking notifications read persists real changes.

## Interactive: shared controls

| Screen/control | What happens | Saves data? |
|---|---|---|
| Community / Staff selection on login | Selects the account workspace to authenticate against | No |
| Email and password | Accepts credentials; blank values show validation | No |
| Open my account / Enter | Authenticates and opens the correct role's workspace | No |
| Sidebar navigation | Opens the selected supported screen | No |
| Header bell | Opens notifications | No |
| Header initials/avatar | Opens the signed-in user's profile | No |
| Refresh | Reloads data and reconstructs the current screen | No |
| Retry after loading failure | Attempts to load the workspace again | No |
| Log out | Closes the workspace and returns to login | No |

Login requires MySQL to be running. An incorrect password or workspace shows an authentication message; an unavailable database shows a connection message. Refresh reconstructs the page, so unsaved form entries should be completed before refreshing.

## Interactive: Community workspace

| Screen/control | What happens | Saves data? |
|---|---|---|
| Home: Animal in danger? / Report a stray | Opens the new report form | No |
| Home: Find your match / See all animals | Opens the adoption catalogue | No |
| Home: Buddy's Meet button | Opens Buddy's detail screen | No |
| Home: View notifications | Opens notifications | No |
| Adopt: name/breed search | Filters the displayed placeholder locally | No |
| Adopt: species and adoption-status selectors | Filters the list; a nonmatching choice shows an empty state | No |
| Buddy: View profile | Opens Buddy's details | No |
| Animal details: Back to all animals | Returns to Adopt | No |
| Apply to adopt Buddy | Opens the adoption review flow; enabled only while Buddy is AVAILABLE | No |
| Review compatibility | Displays the existing matching engine's score | No |
| Submit application | Opens final confirmation; does not save yet | No |
| Back to profile / Review again | Returns to the read-only saved profile information | No |
| Confirm & submit | Creates an adoption application with status NEW | Yes |
| New report / My reports | Switches between the rescue form and current user's reports | No |
| Report animal type and size | Selects values for the report | When submitted |
| Add a clear photo | Opens a file chooser and selects a local photo path | Path saved when submitted |
| Location, barangay, street and notes | Accepts report details | When submitted |
| Condition checkboxes | Collects observed conditions used by the priority engine | When submitted |
| Submit rescue report | Validates and saves the report, then shows its priority | Yes |

The photo picker stores a local path; it does not upload or copy the photo to remote storage. Compatibility uses the existing algorithm and saved profile. It is not a new questionnaire or an AI assessment.

## Interactive: Staff workspace

| Screen/control | What happens | Saves data? |
|---|---|---|
| Operations: Overview / Applications / Hotspots | Switches operational views | No |
| Overview: Manage | Opens the selected rescue case editor | No |
| Rescue cases: New report / Rescue queue | Opens the staff report form or existing case queue | No |
| Rescue queue: Manage case | Opens case status, assignment and notes | No |
| Case status dropdown | Selects REPORTED, VERIFIED, RESCUE_ASSIGNED or RESCUED | When saved |
| Assign this case to me | Selects self-assignment; there is no staff-directory picker | When saved |
| Handler notes | Accepts case notes | When saved |
| Save case update | Persists case status, assignment and notes | Yes |
| Animal records: Buddy's adoption-status dropdown | Selects AVAILABLE, ON_HOLD, MEDICAL_CARE or ADOPTED | When saved |
| Animal records: Save | Persists Buddy's selected adoption status | Yes |
| Applications: Review | Opens a read-only application details message | No |
| Sidebar View cases | Opens the rescue queue | No |

Both roles can submit rescue reports. Staff application review currently does not approve, reject, or schedule an application.

## Interactive: notifications and profiles

| Control | What happens | Saves data? |
|---|---|---|
| Notification category selector | Filters All, RESCUE, ADOPTION or GENERAL for the signed-in user | No |
| Mark all as read | Marks the signed-in user's notifications read, then refreshes | Yes |
| Mark all as read with no unread items | Disabled | No |
| Profile text fields | Can be focused/read/copied, but cannot be edited | No |
| Profile Log out | Returns to login | No |

## Visible but not interactive

These are display elements, not buttons:

- Home and operations metric cards and their counts.
- Buddy's photo, name, traits, match badge, health and consultation schedule.
- Match percentage and compatibility explanation.
- Saved profile values in the adoption flow.
- Rescue priority badges, case information and progress stages.
- Notification cards, timestamps and READ/UNREAD badges. There is no individual-notification open/read action.
- Hotspot barangay names, rankings and bars. They are all-time counts, not a clickable map.
- Profile cover, initials, account role and staff statistics.
- Loading/status messages and the login's branding text.

Use the explicit Meet, View profile, Manage or navigation buttons rather than clicking the surrounding card or image.

## Not implemented / not shown

The current backend does not support these complete flows, so the UI does not present working controls for them:

- Registration, password reset/change, or "keep me signed in."
- Editing personal, adopter or staff profiles.
- Editing the adoption questionnaire or persisting new compatibility answers.
- Saved animals/favorites.
- Booking, changing or cancelling shelter visits.
- Approving/rejecting applications or moving their review stage.
- Adding/deleting animals or editing their complete profiles.
- Selecting handlers from a directory or dispatching vehicles.
- Chatbot conversations or help-ticket submission.
- Individual notification read actions or notification-preference settings.
- Geographic map interaction, a last-30-days report filter, or monthly trend charts.
- Automatic live polling/push updates; use Refresh to reload external changes.

Legacy placeholder Java classes for some of these features remain in the repository, but are not mounted as working screens.

## Verification for this update

- Java compilation passed.
- Login and both workspaces rendered using the existing database.
- Existing interaction checks passed for navigation, filtering/empty states, animal details, the three adoption steps and 1080px layouts.
- Visually checked login without the Nori card, the single Buddy catalogue card and the single Buddy staff inventory row.
- This update did not delete or modify database records.
