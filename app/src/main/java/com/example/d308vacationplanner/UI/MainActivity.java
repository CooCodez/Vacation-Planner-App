package com.example.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.database.VacationDatabaseBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is logged in, if not, navigate to LoginActivity
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            // If no user is logged in, navigate to LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();  // Close MainActivity so the user can't go back to it
            return;
        }

        // Hide the ActionBar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        // Enable edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Initialize window insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the Room database
        VacationDatabaseBuilder.getDatabase(this);

        // Button to navigate to the Vacations screen
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Vacations.class);
            intent.putExtra("test", "Information sent");
            startActivity(intent);
        });

        // Button to navigate to the Report screen
        Button reportButton = findViewById(R.id.buttonViewReport);
        reportButton.setOnClickListener(v -> {
            // Navigate to ReportActivity when clicked
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
        });

        // Add Sign Out Button functionality
        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();  // Sign out the user
            startActivity(new Intent(MainActivity.this, LoginActivity.class)); // Redirect to LoginActivity
            finish();  // Close MainActivity so the user can't go back
        });
    }
}
