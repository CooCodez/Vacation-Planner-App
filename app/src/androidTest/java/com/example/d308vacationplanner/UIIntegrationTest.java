package com.example.d308vacationplanner;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.rule.ActivityTestRule;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.Espresso;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.Espresso.onView;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.widget.DatePicker;

import com.example.d308vacationplanner.UI.Vacations;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

public class UIIntegrationTest {

    @Rule
    public ActivityTestRule<Vacations> activityRule = new ActivityTestRule<>(Vacations.class);

    @Test
    public void testAddVacationNavigatesToDetails() {
        // Click on the FloatingActionButton
        onView(withId(R.id.floatingActionButton)).perform(ViewActions.click());

        // Verify the vacation details layout appears
        onView(withId(R.id.scrollView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testSaveVacation() {
        // Click the FloatingActionButton to navigate to the VacationDetails page
        onView(withId(R.id.floatingActionButton)).perform(ViewActions.click());

        // Input vacation location (Name) in the input field
        onView(withId(R.id.titletext)).perform(typeText("Hawaii"));

        // Input hotel name in the hotel input field
        onView(withId(R.id.hoteltext)).perform(typeText("Hilton"));

        // Set the start date (click on Start Date Button)
        onView(withId(R.id.buttonStartDate)).perform(ViewActions.click());

        // Simulate selecting a start date (January 1, 2026)
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, 2026);
        startCalendar.set(Calendar.MONTH, 0);  // January
        startCalendar.set(Calendar.DAY_OF_MONTH, 1); // January 1st

        // Interact with the DatePickerDialog (date picker) and select the date
        onView(withClassName(is("android.widget.DatePicker"))).perform(setDateOnDatePicker(startCalendar));
        onView(withText("OK")).perform(ViewActions.click());  // Simulate clicking the "OK" button

        // Set the end date (click on End Date Button)
        onView(withId(R.id.buttonEndDate)).perform(ViewActions.click());

        // Simulate selecting an end date (January 2, 2026)
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, 2026);
        endCalendar.set(Calendar.MONTH, 0);  // January
        endCalendar.set(Calendar.DAY_OF_MONTH, 2); // January 2nd

        // Interact with the DatePickerDialog (date picker) and select the date
        onView(withClassName(is("android.widget.DatePicker"))).perform(setDateOnDatePicker(endCalendar));
        onView(withText("OK")).perform(ViewActions.click());  // Simulate clicking the "OK" button

        // Click the save button
        onView(withId(R.id.buttonSaveVacation)).perform(ViewActions.click());

        // Verify the new vacation is added to the RecyclerView (ensure the vacation name appears)
        onView(withId(R.id.recyclerview)).perform(RecyclerViewActions.scrollToPosition(0));

        // Check if "Hawaii" appears in the RecyclerView
        onView(withText("Hawaii")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));  // Check if "Hawaii" appears

    }

    // Custom helper method to simulate date selection in the DatePicker
    public static ViewAction setDateOnDatePicker(final Calendar calendar) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DatePicker.class);
            }

            @Override
            public String getDescription() {
                return "set date on DatePicker";
            }

            @Override
            public void perform(UiController uiController, View view) {
                DatePicker datePicker = (DatePicker) view;
                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
        };
    }

    @Test
    public void testDeleteVacation() {
        // Click the FloatingActionButton to navigate to the VacationDetails page
        onView(withId(R.id.floatingActionButton)).perform(ViewActions.click());

        // Input vacation location (Name) in the input field
        onView(withId(R.id.titletext)).perform(typeText("Hawaii"));

        // Input hotel name in the hotel input field
        onView(withId(R.id.hoteltext)).perform(typeText("Hilton"));

        // Set the start date (click on Start Date Button)
        onView(withId(R.id.buttonStartDate)).perform(ViewActions.click());

        // Simulate selecting a start date (January 1, 2026)
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, 2026);
        startCalendar.set(Calendar.MONTH, 0);  // January
        startCalendar.set(Calendar.DAY_OF_MONTH, 1); // January 1st

        // Interact with the DatePickerDialog (date picker) and select the date
        onView(withClassName(is("android.widget.DatePicker"))).perform(setDateOnDatePicker(startCalendar));
        onView(withText("OK")).perform(ViewActions.click());  // Simulate clicking the "OK" button

        // Set the end date (click on End Date Button)
        onView(withId(R.id.buttonEndDate)).perform(ViewActions.click());

        // Simulate selecting an end date (January 2, 2026)
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, 2026);
        endCalendar.set(Calendar.MONTH, 0);  // January
        endCalendar.set(Calendar.DAY_OF_MONTH, 2); // January 2nd

        // Interact with the DatePickerDialog (date picker) and select the date
        onView(withClassName(is("android.widget.DatePicker"))).perform(setDateOnDatePicker(endCalendar));
        onView(withText("OK")).perform(ViewActions.click());  // Simulate clicking the "OK" button

        // Click the save button to save the vacation
        onView(withId(R.id.buttonSaveVacation)).perform(ViewActions.click());

        // Verify the new vacation is added to the RecyclerView (ensure the vacation name appears)
        onView(withId(R.id.recyclerview)).perform(RecyclerViewActions.scrollToPosition(0));

        // Check if "Hawaii" appears in the RecyclerView
        onView(withText("Hawaii")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));  // Check if "Hawaii" appears

        // Now click on the saved vacation to open the VacationDetails page
        onView(withText("Hawaii")).perform(ViewActions.click());

        // Open the options menu (usually by pressing the overflow menu button)
        onView(withContentDescription("More options")).perform(ViewActions.click());

        // Ensure that the menu has been opened before performing the delete action
        onView(withText("Delete Vacation")).perform(ViewActions.click()); // Clicking the "Delete" option directly

        // After clicking delete, confirm the deletion if a confirmation dialog exists (uncomment this if necessary):
        // onView(withText("Confirm")).perform(ViewActions.click());

        // Verify that the vacation is removed from the RecyclerView
        onView(withId(R.id.recyclerview)).perform(RecyclerViewActions.scrollToPosition(0));

        // Assert that "Hawaii" no longer exists in the RecyclerView
        onView(withText("Hawaii")).check(ViewAssertions.doesNotExist());  // Assert that "Hawaii" no longer exists
    }




}
