package com.example.d308vacationplanner.UI;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.d308vacationplanner.R;

public class ReportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);  // Create this layout next

        // Set up UI (for now just a basic message)
        setTitle("Vacation Report");
    }
}
