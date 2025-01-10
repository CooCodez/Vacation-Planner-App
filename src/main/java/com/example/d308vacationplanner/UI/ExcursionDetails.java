package com.example.d308vacationplanner.UI;

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

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.ViewModels.ExcursionViewModel;
import com.example.d308vacationplanner.ViewModels.VacationViewModel;
import com.example.d308vacationplanner.entities.Excursion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    private ExcursionViewModel excursionViewModel;
    private VacationViewModel vacationViewModel;
    private String name, hotel;
    private int excursionID = -1, vacationID = -1;
    private EditText editName, editHotel;
    private TextView editDate, errorText;
    private final Calendar myCalendarStart = Calendar.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        // Initialize ViewModels
        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        // Initialize Views
        editName = findViewById(R.id.excursionID);
        editHotel = findViewById(R.id.excursionHotel);
        editDate = findViewById(R.id.date);
        errorText = findViewById(R.id.errorText);
        Button saveButton = findViewById(R.id.saveButton);

        // Retrieve data from Intent
        name = getIntent().getStringExtra("name");
        hotel = getIntent().getStringExtra("hotel");
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        long dateMillis = getIntent().getLongExtra("date", -1);

        // Populate existing data
        editName.setText(name != null ? name : "");
        editHotel.setText(hotel != null ? hotel : "");
        if (dateMillis != -1) {
            Date excursionDate = new Date(dateMillis);
            editDate.setText(sdf.format(excursionDate));
            myCalendarStart.setTime(excursionDate);
        } else {
            editDate.setText(getString(R.string.click_to_select_date));
        }

        // Handle date selection
        editDate.setOnClickListener(v -> showDatePickerDialog());

        // Set the button text to either "Save Excursion" or "Update Excursion"
        if (excursionID == -1) {
            saveButton.setText(getString(R.string.save_excursion));
        } else {
            saveButton.setText(getString(R.string.update_excursion));
        }

        // Set up Save Button OnClickListener
        saveButton.setOnClickListener(v -> saveExcursion());

        // Enable the back button in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle back button with OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish(); // Close the activity
            }
        });
    }

    private Date normalizeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private void showDatePickerDialog() {
        vacationViewModel.getVacationByID(vacationID).observe(this, currentVacation -> {
            if (currentVacation == null) {
                Toast.makeText(this, getString(R.string.vacation_not_found), Toast.LENGTH_SHORT).show();
                return;
            }
            // Normalize vacation start and end dates
            Date vacationStartDate = normalizeDate(currentVacation.getStartDate());
            Date vacationEndDate = normalizeDate(currentVacation.getEndDate());

            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        myCalendarStart.set(Calendar.YEAR, year);
                        myCalendarStart.set(Calendar.MONTH, month);
                        myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        Date selectedDate = normalizeDate(myCalendarStart.getTime());
                        // Updated condition to allow start and end dates
                        if (selectedDate.compareTo(vacationStartDate) < 0 || selectedDate.compareTo(vacationEndDate) > 0) {
                            showError(getString(R.string.date_within_vacation));
                        } else {
                            clearError();
                            editDate.setText(sdf.format(selectedDate));
                        }
                    },
                    myCalendarStart.get(Calendar.YEAR),
                    myCalendarStart.get(Calendar.MONTH),
                    myCalendarStart.get(Calendar.DAY_OF_MONTH)
            );

            // Set min and max date to allow selecting start and end dates
            datePicker.getDatePicker().setMinDate(vacationStartDate.getTime());
            datePicker.getDatePicker().setMaxDate(vacationEndDate.getTime());
            datePicker.show();
        });
    }





    private void showError(String errorMessage) {
        errorText.setText(errorMessage);
        errorText.setVisibility(View.VISIBLE);
    }

    private void clearError() {
        errorText.setText("");
        errorText.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        menu.findItem(R.id.partsave).setVisible(false);
        if (excursionID == -1) {
            menu.findItem(R.id.partdelete).setVisible(false);
            menu.findItem(R.id.share).setVisible(false);
            menu.findItem(R.id.notify).setVisible(false);
        } else {
            menu.findItem(R.id.partsave).setTitle(getString(R.string.update_excursion));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.partsave) {
            saveExcursion();
            return true;
        } else if (item.getItemId() == R.id.partdelete) {
            deleteExcursion();
            return true;
        } else if (item.getItemId() == R.id.notify) {
            scheduleNotification();
            return true;
        } else if (item.getItemId() == R.id.share) {
            shareExcursionDetails();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveExcursion() {
        String excursionName = editName.getText().toString().trim();
        String excursionHotel = editHotel.getText().toString().trim();
        String excursionDateString = editDate.getText().toString().trim();

        if (excursionName.isEmpty()) {
            showError(getString(R.string.excursion_name_empty));
            return;
        }

        if (excursionDateString.equals(getString(R.string.click_to_select_date))) {
            showError(getString(R.string.please_select_date));
            return;
        }

        try {
            Date excursionDate = sdf.parse(excursionDateString);
            if (excursionDate == null) {
                showError(getString(R.string.invalid_date_format));
                return;
            }

            Excursion excursion = new Excursion(excursionName, excursionHotel, vacationID, excursionDate);
            if (excursionID == -1) {
                excursionViewModel.insert(excursion);
                Toast.makeText(this, getString(R.string.excursion_saved), Toast.LENGTH_SHORT).show();
            } else {
                excursion.setExcursionID(excursionID);
                excursionViewModel.update(excursion);
                Toast.makeText(this, getString(R.string.excursion_updated), Toast.LENGTH_SHORT).show();
            }
            finish();
        } catch (ParseException e) {
            showError(getString(R.string.invalid_date_format));
        }
    }

    private void deleteExcursion() {
        if (excursionID == -1) {
            Toast.makeText(this, getString(R.string.no_excursion_to_delete), Toast.LENGTH_SHORT).show();
            return;
        }

        excursionViewModel.getExcursionByID(excursionID).observe(this, excursion -> {
            if (excursion != null) {
                excursionViewModel.delete(excursion);
                Toast.makeText(this, getString(R.string.excursion_deleted), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, getString(R.string.excursion_not_found), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scheduleNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("key", getString(R.string.notification_set, editDate.getText().toString()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, excursionID, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, myCalendarStart.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, getString(R.string.notification_set, editDate.getText().toString()), Toast.LENGTH_SHORT).show();
    }

    private void shareExcursionDetails() {
        if (name == null || hotel == null || editDate.getText() == null) {
            Toast.makeText(this, getString(R.string.excursion_details_incomplete), Toast.LENGTH_SHORT).show();
            return;
        }

        String excursionDetails = getString(R.string.excursion_details_template, name, hotel, editDate.getText().toString());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, excursionDetails);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
    }

}
