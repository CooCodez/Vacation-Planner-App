package com.example.d308vacationplanner;

import static junit.framework.TestCase.assertNull;

import android.content.Intent;
import android.widget.Toast;
import android.util.Log;

import com.example.d308vacationplanner.UI.LoginActivity;
import com.example.d308vacationplanner.UI.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class AuthenticationTest {

    private FirebaseAuth mAuth;
    private LoginActivity loginActivity;

    @Before
    public void setup() {
        // Reset FirebaseAuth before each test to ensure no state is carried over
        FirebaseAuth.getInstance().signOut();  // Ensure the user is logged out

        // Use the Firebase Auth Emulator
        FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099); // Adjust IP to your emulator's IP if needed

        // Launch the activity and initialize FirebaseAuth
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(activity -> {
            loginActivity = activity;
            mAuth = FirebaseAuth.getInstance();
        });
    }

    @Test
    public void testLoginSuccess() {
        String testEmail = "loyamark@gmail.com";
        String testPassword = "12345678";

        // Use ActivityScenario to simulate login
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(activity -> {
            // Simulate successful login using Firebase Emulator
            mAuth.signInWithEmailAndPassword(testEmail, testPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Simulate the UI behavior after successful login
                            activity.startActivity(new Intent(activity, MainActivity.class));

                            // Verify MainActivity is started
                            verify(activity).startActivity(any(Intent.class));
                        } else {
                            // Simulate Toast message for failure
                            activity.runOnUiThread(() -> {
                                Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
        });
    }

    @Test
    public void testLoginFailure() {
        String testEmail = "loyamark@gmail.com";
        String testPassword = "wrongpassword";

        // Create a CountDownLatch to wait for Firebase to complete its task
        final CountDownLatch latch = new CountDownLatch(1);

        // Use ActivityScenario to simulate login
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(activity -> {
            // Simulate failed login using Firebase Emulator
            mAuth.signInWithEmailAndPassword(testEmail, testPassword)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            // Simulate Toast message for failure
                            activity.runOnUiThread(() -> {
                                Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            });

                            // Signal that the task is complete
                            latch.countDown();
                        }
                    });
        });

        // Wait for the asynchronous Firebase task to complete
        try {
            latch.await(15, TimeUnit.SECONDS);  // Adjust timeout if necessary
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that Firebase Auth task completed as expected
        assertNull(mAuth.getCurrentUser());  // Ensure the user is not signed in with wrong credentials
    }


    @Test
    public void testLogout() {
        // First, log the user in
        ActivityScenario<LoginActivity> loginScenario = ActivityScenario.launch(LoginActivity.class);
        loginScenario.onActivity(activity -> {
            // Simulate successful login
            mAuth.signInWithEmailAndPassword("loyamark@gmail.com", "12345678")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Launch MainActivity after successful login
                            activity.startActivity(new Intent(activity, MainActivity.class));

                            // Now, test the logout functionality
                            ActivityScenario<MainActivity> mainActivityScenario = ActivityScenario.launch(MainActivity.class);
                            mainActivityScenario.onActivity(mainActivity -> {
                                // Using runOnUiThread to simulate UI interaction (clicking the sign-out button)
                                mainActivity.runOnUiThread(() -> {
                                    // Find and click the sign-out button
                                    mainActivity.findViewById(R.id.signOutButton).performClick();

                                    // Use CountDownLatch to wait for Firebase auth state change
                                    final CountDownLatch latch = new CountDownLatch(1);

                                    // Listen for the sign-out completion
                                    FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
                                        if (firebaseAuth.getCurrentUser() == null) {
                                            // Assert that the user is signed out
                                            assertNull(FirebaseAuth.getInstance().getCurrentUser());
                                            latch.countDown();  // Signal that the sign-out is completed
                                        }
                                    });

                                    // Wait for the sign-out to complete
                                    try {
                                        latch.await(15, TimeUnit.SECONDS);  // Increase timeout if necessary
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    // After waiting for sign-out to finish, verify the user is signed out
                                    assertNull(FirebaseAuth.getInstance().getCurrentUser());

                                    // Verify that the signOut method was called
                                    verify(mAuth).signOut();
                                });
                            });
                        }
                    });
        });
    }
}
