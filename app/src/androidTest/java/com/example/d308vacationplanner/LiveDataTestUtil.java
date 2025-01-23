package com.example.d308vacationplanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil {

    /**
     * Gets the value of LiveData, blocking the test thread until the value is available.
     * @param liveData The LiveData instance to observe.
     * @param <T> The type of data held by the LiveData.
     * @return The value of the LiveData, or null if it wasn't set.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public static <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        final Object[] value = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                value[0] = t;
                latch.countDown(); // Signal that we have a new value
            }
        };

        // Observe the LiveData until the value is set
        liveData.observeForever(observer);

        // Wait for the value to be updated (a max of 2 seconds)
        latch.await(2, TimeUnit.SECONDS);

        // Return the value or null if the LiveData didn't provide a value
        return (T) value[0];
    }
}
