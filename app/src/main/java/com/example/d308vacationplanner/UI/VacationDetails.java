package com.example.d308vacationplanner.UI;

import android.animation.Animator;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.ViewModels.ExcursionViewModel;
import com.example.d308vacationplanner.ViewModels.VacationViewModel;
import com.example.d308vacationplanner.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.view.animation.LinearInterpolator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    private VacationViewModel vacationViewModel;
    private ExcursionViewModel excursionViewModel;
    private String name, hotel;
    private Date startDate, endDate;
    private int vacationID;
    private EditText editName, editHotel;
    private TextView textViewStartDate, textViewEndDate, addExcursionMessage;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        // Initialize Views
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        addExcursionMessage = findViewById(R.id.addExcursionMessage);

        editName = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        Button buttonStartDate = findViewById(R.id.buttonStartDate);
        Button buttonEndDate = findViewById(R.id.buttonEndDate);
        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);

        // Initialize ViewModels
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);

        // Get Intent Extras
        vacationID = getIntent().getIntExtra("id", -1);

        // Set FAB and Add Excursion visibility based on vacationID
        if (vacationID == -1) {
            fab.setVisibility(View.GONE); // Hide the FAB for new vacations
            addExcursionMessage.setVisibility(View.GONE); // Hide the message for new vacations
        } else {
            fab.setVisibility(View.VISIBLE); // Show the FAB for saved vacations
            addExcursionMessage.setVisibility(View.VISIBLE); // Show the message for saved vacations
        }

        // Set Save/Update Button Text Based on whether it's a new or existing vacation
        Button buttonSaveVacation = findViewById(R.id.buttonSaveVacation);
        if (vacationID == -1) {
            buttonSaveVacation.setText(getString(R.string.save_vacation)); // New vacation
        } else {
            buttonSaveVacation.setText(getString(R.string.update_vacation)); // Existing vacation
        }

        // Handle save button click logic
        buttonSaveVacation.setOnClickListener(v -> saveVacation());

        // Observe vacation details
        if (vacationID != -1) {
            vacationViewModel.getVacationByID(vacationID).observe(this, vacation -> {
                if (vacation != null) {
                    name = vacation.getVacationName();
                    hotel = vacation.getHotel();
                    startDate = vacation.getStartDate();
                    endDate = vacation.getEndDate();

                    editName.setText(name);
                    editHotel.setText(hotel);
                    textViewStartDate.setText(startDate != null
                            ? String.format(getString(R.string.start_date), dateFormat.format(startDate))
                            : getString(R.string.not_selected));
                    textViewEndDate.setText(endDate != null
                            ? String.format(getString(R.string.end_date), dateFormat.format(endDate))
                            : getString(R.string.not_selected));
                }
            });
        }

        // Start Date Picker logic
        buttonStartDate.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        startDate = calendar.getTime();

                        // Update start date TextView using string resource
                        textViewStartDate.setText(String.format(getString(R.string.start_date), dateFormat.format(startDate)));

                        // Reset endDate if invalid
                        if (endDate != null && endDate.before(startDate)) {
                            endDate = null;
                            textViewEndDate.setText(getString(R.string.not_selected));
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            // Pre-select the current start date in the date picker
            if (startDate != null) {
                calendar.setTime(startDate);
                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }

            // Restrict max date to the end date if it exists
            if (endDate != null) {
                datePicker.getDatePicker().setMaxDate(endDate.getTime());
            }

            datePicker.show();
        });

        // End Date Picker logic
        buttonEndDate.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        endDate = calendar.getTime();

                        // Update end date TextView using string resource
                        textViewEndDate.setText(String.format(getString(R.string.end_date), dateFormat.format(endDate)));

                        // Reset startDate if invalid
                        if (startDate != null && startDate.after(endDate)) {
                            startDate = null;
                            textViewStartDate.setText(getString(R.string.not_selected));
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            // Pre-select the current end date in the date picker
            if (endDate != null) {
                calendar.setTime(endDate);
                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }

            // Restrict min date to the start date if it exists
            if (startDate != null) {
                datePicker.getDatePicker().setMinDate(startDate.getTime());
            }

            datePicker.show();
        });

        // Floating action button for adding excursions
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
            intent.putExtra("vacationID", vacationID);
            startActivity(intent);
        });

        // Animate FloatingActionButton and Add Excursion TextView
        animateFab(fab);
        animateTextViewMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observe associated excursions
        excursionViewModel.getAssociatedExcursions(vacationID).observe(this, excursionAdapter::setExcursions);

        // Only animate the message for existing vacations
        if (vacationID != -1) {
            addExcursionMessage.setVisibility(View.VISIBLE);  // Make the message visible
            animateTextViewMessage();  // Start the animation
        }
    }


    private void animateFab(FloatingActionButton fab) {
        // Pulse Animation for FloatingActionButton
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(fab, "scaleX", 1f, 1.3f, 1f);  // X axis
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(fab, "scaleY", 1f, 1.3f, 1f);  // Y axis

        scaleX.setDuration(1000);  // 1 second for each pulse
        scaleY.setDuration(1000);

        scaleX.setInterpolator(new LinearInterpolator());
        scaleY.setInterpolator(new LinearInterpolator());

        scaleX.setRepeatCount(3); // Repeat 3 times
        scaleY.setRepeatCount(3);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY); // Play both animations together
        animatorSet.start(); // Start the animation
    }

    private void animateTextViewMessage() {
        // Check if the message is visible before starting the animation
        if (addExcursionMessage.getVisibility() == View.VISIBLE) {
            // Pulse Animation for the Add Excursion TextView message
            ObjectAnimator textScaleX = ObjectAnimator.ofFloat(addExcursionMessage, "scaleX", 1f, 1.3f, 1f); // X axis
            ObjectAnimator textScaleY = ObjectAnimator.ofFloat(addExcursionMessage, "scaleY", 1f, 1.3f, 1f); // Y axis

            textScaleX.setDuration(1000);  // 1 second for each pulse
            textScaleY.setDuration(1000);

            textScaleX.setRepeatCount(3); // Repeat 3 times
            textScaleY.setRepeatCount(3);

            textScaleX.setInterpolator(new LinearInterpolator());
            textScaleY.setInterpolator(new LinearInterpolator());

            AnimatorSet textAnimatorSet = new AnimatorSet();
            textAnimatorSet.playTogether(textScaleX, textScaleY); // Play both animations together
            textAnimatorSet.start(); // Start the animation

            // Hide the message after animation ends
            textAnimatorSet.addListener(new AnimatorSet.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {}

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    addExcursionMessage.setVisibility(View.GONE);  // Hide the message after the animation
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animation) {}

                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {}
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);

        // Check if this is a new vacation or an existing one
        MenuItem saveItem = menu.findItem(R.id.vacationsave);
        MenuItem deleteItem = menu.findItem(R.id.vacationdelete);
        MenuItem notifyStartItem = menu.findItem(R.id.vacationnotify_start);  // Use new item id
        MenuItem notifyEndItem = menu.findItem(R.id.vacationnotify_end);      // Use new item id
        MenuItem shareItem = menu.findItem(R.id.vacationshare);

        if (vacationID == -1) { // New vacation
            saveItem.setVisible(false); // Hide save item in menu
            deleteItem.setVisible(false);
            notifyStartItem.setVisible(false);  // Hide start alert item for new vacation
            notifyEndItem.setVisible(false);    // Hide end alert item for new vacation
            shareItem.setVisible(false);
        } else { // Existing vacation
            saveItem.setVisible(false); // Show save item in menu
            saveItem.setTitle("Update"); // Existing vacation: "Update"
            deleteItem.setVisible(true);
            notifyStartItem.setVisible(true);  // Show start alert item for existing vacation
            notifyEndItem.setVisible(true);    // Show end alert item for existing vacation
            shareItem.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vacationsave) {
            saveVacation();
            return true;
        } else if (item.getItemId() == R.id.vacationdelete) {
            deleteVacation();
            return true;
        } else if (item.getItemId() == R.id.vacationnotify_start) {
            scheduleStartDateNotification();  // Handle start date notification
            return true;
        } else if (item.getItemId() == R.id.vacationnotify_end) {
            scheduleEndDateNotification();    // Handle end date notification
            return true;
        } else if (item.getItemId() == R.id.vacationshare) {
            shareVacationDetails();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void scheduleStartDateNotification() {
        if (startDate != null) {
            scheduleNotification(
                    (AlarmManager) getSystemService(Context.ALARM_SERVICE), // Pass the alarm manager
                    startDate,
                    "Reminder: Your vacation \"" + name + "\" starts today!",
                    vacationID * 2
            );
            Toast.makeText(this, "Start date notification set for: " + dateFormat.format(startDate), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select a start date.", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleEndDateNotification() {
        if (endDate != null) {
            scheduleNotification(
                    (AlarmManager) getSystemService(Context.ALARM_SERVICE), // Pass the alarm manager
                    endDate,
                    "Reminder: Your vacation \"" + name + "\" ends today!",
                    vacationID * 2 + 1
            );
            Toast.makeText(this, "End date notification set for: " + dateFormat.format(endDate), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select an end date.", Toast.LENGTH_SHORT).show();
        }
    }



    private void saveVacation() {
        String currentName = editName.getText().toString().trim();
        String currentHotel = editHotel.getText().toString().trim();

        if (currentName.isEmpty() || currentHotel.isEmpty() || startDate == null || endDate == null) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        Vacation vacation = new Vacation(vacationID == -1 ? null : vacationID, currentName, currentHotel, startDate, endDate);
        if (vacationID == -1) {
            vacationViewModel.insert(vacation);
            Toast.makeText(this, "Vacation added successfully.", Toast.LENGTH_SHORT).show();
        } else {
            vacationViewModel.update(vacation);
            Toast.makeText(this, "Vacation updated successfully.", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteVacation() {
        if (vacationID != -1) {
            // Check if there are any excursions associated with the vacation
            excursionViewModel.getAssociatedExcursions(vacationID).observe(this, excursions -> {
                if (excursions != null && !excursions.isEmpty()) {
                    // Show a message indicating the vacation cannot be deleted
                    Toast.makeText(this, "Cannot delete vacation with associated excursions.", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed with deletion if no excursions are found
                    vacationViewModel.delete(new Vacation(vacationID, name, hotel, startDate, endDate));
                    Toast.makeText(this, "Vacation deleted successfully.", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity after deletion
                }
            });
        }
    }

    private void scheduleVacationNotifications() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) {
            Toast.makeText(this, "Failed to schedule notifications. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDate != null) {
            scheduleNotification(alarmManager, startDate, "Reminder: Your vacation \"" + name + "\" starts today!", vacationID * 2);
            Toast.makeText(this, "Notification set for start date: " + dateFormat.format(startDate), Toast.LENGTH_SHORT).show();
        }

        if (endDate != null) {
            scheduleNotification(alarmManager, endDate, "Reminder: Your vacation \"" + name + "\" ends today!", vacationID * 2 + 1);
            Toast.makeText(this, "Notification set for end date: " + dateFormat.format(endDate), Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleNotification(AlarmManager alarmManager, Date date, String message, int requestCode) {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("key", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long triggerTime = date.getTime();
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }

    private void shareVacationDetails() {
        if (name == null || hotel == null || startDate == null || endDate == null) {
            Toast.makeText(this, "Vacation details are incomplete. Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create vacation details to share
        String vacationDetails = "Vacation Details:\n" +
                "Name: " + name + "\n" +
                "Hotel: " + hotel + "\n" +
                "Start Date: " + dateFormat.format(startDate) + "\n" +
                "End Date: " + dateFormat.format(endDate);

        // Create an intent to share the vacation details
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, vacationDetails);
        startActivity(Intent.createChooser(shareIntent, "Share Vacation Details via"));
    }
}
