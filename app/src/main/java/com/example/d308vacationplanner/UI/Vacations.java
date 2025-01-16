package com.example.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.ViewModels.VacationViewModel;
import com.example.d308vacationplanner.ViewModels.ExcursionViewModel;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.SearchView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.view.animation.LinearInterpolator;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class Vacations extends AppCompatActivity {

    private VacationViewModel vacationViewModel;
    private ExcursionViewModel excursionViewModel;
    private VacationAdapter vacationAdapter;
    private TextView addVacationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacations);

        // Initialize Views
        addVacationMessage = findViewById(R.id.addVacationMessage);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        SearchView searchView = findViewById(R.id.searchView);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up ViewModel to observe vacations
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);
        vacationViewModel.getAllVacations().observe(this, vacationAdapter::setVacations);

        // Set up FloatingActionButton
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(Vacations.this, VacationDetails.class);
            startActivity(intent);
        });

        // Tooltip for FloatingActionButton
        TooltipCompat.setTooltipText(fab, "Tap to add a vacation");

        // Pulse Animation for FloatingActionButton
        animateFab(fab);

        // Handle SearchView filtering
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No action when the user submits the query
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                vacationAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensure the message and animation appear every time the user comes back to this page
        addVacationMessage.setVisibility(View.VISIBLE);  // Make the message visible
        animateTextViewMessage();  // Start the animation

        // Set up RecyclerView again in case of resume
        vacationViewModel.getAllVacations().observe(this, vacationAdapter::setVacations);
    }

    private void animateFab(FloatingActionButton fab) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(fab, "scaleX", 1f, 1.3f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(fab, "scaleY", 1f, 1.3f, 1f);
        scaleX.setDuration(1000);
        scaleY.setDuration(1000);
        scaleX.setRepeatCount(3);
        scaleY.setRepeatCount(3);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    private void animateTextViewMessage() {
        ObjectAnimator textScaleX = ObjectAnimator.ofFloat(addVacationMessage, "scaleX", 1f, 1.3f, 1f);
        ObjectAnimator textScaleY = ObjectAnimator.ofFloat(addVacationMessage, "scaleY", 1f, 1.3f, 1f);

        textScaleX.setDuration(1000);
        textScaleY.setDuration(1000);
        textScaleX.setRepeatCount(3);
        textScaleY.setRepeatCount(3);
        textScaleX.setInterpolator(new LinearInterpolator());
        textScaleY.setInterpolator(new LinearInterpolator());

        AnimatorSet textAnimatorSet = new AnimatorSet();
        textAnimatorSet.playTogether(textScaleX, textScaleY);
        textAnimatorSet.start();

        textAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {}

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                addVacationMessage.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {}

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sample) {
            // Insert sample data into the database
            insertSampleData();
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertSampleData() {
        // Sample vacation and excursion data
        Calendar calendar = Calendar.getInstance();
        AtomicReference<Date> startDate = new AtomicReference<>(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 7); // Add 7 days for the end date
        AtomicReference<Date> endDate = new AtomicReference<>(calendar.getTime());

        // Locations for variety
        String[] locations = {"USA", "Mexico", "Japan", "Italy", "France", "Brazil", "Australia", "Germany", "Spain", "South Africa"};

        // Insert 10 vacations
        for (int i = 0; i < 10; i++) {
            String vacationLocation = locations[i];
            Vacation vacation = new Vacation(null, vacationLocation, "Hotel " + (i + 1), startDate.get(), endDate.get());
            vacationViewModel.insert(vacation);
        }

        // Now, observe the vacation data to get updated vacation IDs after all vacations are inserted
        vacationViewModel.getAllVacations().observe(this, vacations -> {
            if (vacations != null && vacations.size() >= 10) {
                // Insert corresponding excursions for each vacation
                for (int i = 0; i < 10; i++) {
                    String vacationLocation = locations[i];
                    int vacationID = vacations.get(i).getVacationID();

                    for (int j = 1; j <= 2; j++) {
                        String excursionName = vacationLocation + " Excursion " + j;
                        Excursion excursion = new Excursion(excursionName, "Hotel for " + vacationLocation, vacationID, startDate.get());
                        excursionViewModel.insert(excursion);
                    }

                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    startDate.set(calendar.getTime());
                    endDate.set(calendar.getTime());
                }

                Toast.makeText(this, "Sample data inserted with 10 vacations and 20 excursions.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
