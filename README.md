# Vacation Scheduler Mobile Application

## Purpose
The Vacation Scheduler app is designed to help users organize and track vacations and associated excursions. It allows users to add, update, and delete vacation and excursion data, set alerts for important dates, and share vacation details with others. The app is a practical solution for travelers to manage their trips conveniently using a mobile device.

---

## Application Features and User Flow

### Main Menu
- The app begins with a **Main Menu**, where users can select the **Start Planning** option. This navigates them to the **Vacations Page**, the central hub for managing vacation entries.

---

### Vacations Page
- Users can view a list of all saved vacations.
- **Add New Vacation**:
    - Click the floating action button in the bottom-right corner to navigate to the **Vacation Details Page**.
    - For testing purposes, the menu includes an "Add Seal Code" option.
- Once a vacation is created, it appears in the list. Selecting a vacation displays its details on the **Vacation Details Page**.

---

### Vacation Details Page
- Users can input vacation details such as:
    - **Title**
    - **Hotel** or accommodation
    - **Start Date**
    - **End Date**
- A **Save** button stores the vacation details. Users must save the vacation before adding excursions or editing details further.
- After saving, users are redirected back to the **Vacations Page**, where the new vacation is listed.
- Clicking a saved vacation opens its details page, where users can:
    - Edit vacation details (title, hotel, dates).
    - Delete the vacation (deletion is blocked if excursions are associated).
    - Share vacation details via email, SMS, or clipboard.
    - Set alarms for the **start date** and **end date**.
    - Add excursions using the floating action button in the bottom-right corner.

---

### Excursion Details Page
- From the **Vacation Details Page**, users can click the floating action button to navigate to the **Excursion Details Page**.
- Users can input:
    - **Title**
    - **Hotel**
    - **Date**
- After saving, users are redirected back to the **Vacation Details Page**, where the excursion is listed at the bottom.
- Clicking a saved excursion opens its details page, where users can:
    - Edit excursion details (title, hotel, date) and save updates.
    - Delete the excursion.
    - Share excursion details via email, SMS, or clipboard.
    - Set an alarm for the excursion date.

---

## Validation and User Experience Enhancements
The app includes built-in validation to enhance the user experience and ensure accurate data entry:
- **Vacation Date Validation**:
    - When selecting a start date, only valid end dates (after the start date) are selectable.
    - End dates cannot be set earlier than the start date.
- **Excursion Date Validation**:
    - Excursion dates are restricted to fall within the start and end dates of the associated vacation.

These features ensure data integrity and provide a smooth user experience.

---

## Android Version
The app is deployed to **Android 8.0 (Oreo)** and higher.

---

## Repository Link
[GitLab Repository](https://gitlab.com/wgu-gitlab-environment/student-repos/mloya23/d308-mobile-application-development-android.git)
