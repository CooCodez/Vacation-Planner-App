package com.example.d308vacationplanner.UI;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.ViewModels.VacationViewModel;
import com.example.d308vacationplanner.ViewModels.ExcursionViewModel;
import com.example.d308vacationplanner.entities.Vacation;
import com.example.d308vacationplanner.entities.Excursion;

public class ReportActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ReportAdapter mAdapter;
    private VacationViewModel mVacationViewModel;
    private ExcursionViewModel mExcursionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report); // Use the updated layout with RecyclerView

        // Set up RecyclerView and Adapter
        mRecyclerView = findViewById(R.id.recyclerViewReport);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReportAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // Initialize ViewModels
        mVacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        mExcursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);

        // Observe the data and pass it to the adapter
        mVacationViewModel.getAllVacations().observe(this, vacations -> {
            mExcursionViewModel.getAllExcursions().observe(this, excursions -> {
                // Populate the RecyclerView with vacation and excursion data
                mAdapter.setVacations(vacations, excursions);
            });
        });

        // Set the title and enable back button
        setTitle("Vacation Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Handle the back button action (Up button) in the ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go back to the previous activity
        return true;
    }
}
