package com.example.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.ViewModels.VacationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;

public class Vacations extends AppCompatActivity {

    private VacationViewModel vacationViewModel;
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

        // Add animation for "Click here to add a vacation" message
        animateAddVacationMessage();

        // Handle SearchView filtering
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                vacationAdapter.getFilter().filter(newText);
                return true;
            }
        });

        // Handle window insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensure the message and animation appear every time the user comes back to this page
        addVacationMessage.setVisibility(View.VISIBLE);  // Make the message visible
        animateAddVacationMessage();  // Start the animation

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

    private void animateAddVacationMessage() {
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
}
