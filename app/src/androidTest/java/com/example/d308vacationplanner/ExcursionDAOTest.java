package com.example.d308vacationplanner;

import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.d308vacationplanner.database.VacationDatabaseBuilder;
import com.example.d308vacationplanner.dao.ExcursionDAO;
import com.example.d308vacationplanner.entities.Excursion;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class ExcursionDAOTest {

    private VacationDatabaseBuilder vacationDatabase;
    private ExcursionDAO excursionDAO;
    private Context context;

    // Rule to allow LiveData to work in unit tests
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        // Initialize in-memory database for testing
        context = ApplicationProvider.getApplicationContext();
        vacationDatabase = Room.inMemoryDatabaseBuilder(context, VacationDatabaseBuilder.class)
                .allowMainThreadQueries()
                .build();
        excursionDAO = vacationDatabase.excursionDAO();
    }

    @After
    public void tearDown() {
        vacationDatabase.close(); // Close database after tests
    }

    @Test
    public void insertExcursion() throws InterruptedException {
        // Create an excursion with non-null date
        Date excursionDate = new Date();  // Use the current date
        Excursion excursion = new Excursion("Snorkeling", "Beach Resort", 1, excursionDate);

        excursionDAO.insert(excursion);

        // Test that the excursion was inserted
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Excursion>[] excursionList = new List[1];

        LiveData<List<Excursion>> excursionListLiveData = excursionDAO.getAllExcursions();
        excursionListLiveData.observeForever(new Observer<List<Excursion>>() {
            @Override
            public void onChanged(List<Excursion> excursionListValue) {
                excursionList[0] = excursionListValue;
                latch.countDown(); // signal that the live data is updated
            }
        });

        latch.await(2, TimeUnit.SECONDS); // wait for LiveData to be updated
        assertEquals(1, excursionList[0].size());  // Ensure only one excursion is inserted
    }

    @Test
    public void updateExcursion() throws InterruptedException {
        // Insert excursion
        Date excursionDate = new Date();
        Excursion excursion = new Excursion("Snorkeling", "Beach Resort", 1, excursionDate);

        CountDownLatch insertLatch = new CountDownLatch(1);
        final List<Excursion>[] insertedExcursions = new List[1];

        excursionDAO.insert(excursion);

        LiveData<List<Excursion>> insertListLiveData = excursionDAO.getAllExcursions();
        insertListLiveData.observeForever(new Observer<List<Excursion>>() {
            @Override
            public void onChanged(List<Excursion> excursions) {
                insertedExcursions[0] = excursions;
                insertLatch.countDown();
            }
        });

        insertLatch.await(2, TimeUnit.SECONDS);
        excursion.setExcursionID(insertedExcursions[0].get(0).getExcursionID());

        // Update the excursion
        excursion.setExcursionName("Updated Snorkeling");
        excursionDAO.update(excursion);

        // New latch for update
        CountDownLatch updateLatch = new CountDownLatch(1);
        final List<Excursion>[] updatedExcursions = new List[1];

        LiveData<List<Excursion>> updateListLiveData = excursionDAO.getAllExcursions();
        updateListLiveData.observeForever(new Observer<List<Excursion>>() {
            @Override
            public void onChanged(List<Excursion> excursions) {
                updatedExcursions[0] = excursions;
                updateLatch.countDown();
            }
        });

        updateLatch.await(2, TimeUnit.SECONDS);
        assertEquals("Updated Snorkeling", updatedExcursions[0].get(0).getExcursionName());
    }

    @Test
    public void deleteExcursion() throws InterruptedException {
        // Insert excursion
        Date excursionDate = new Date();
        Excursion excursion = new Excursion("Snorkeling", "Beach Resort", 1, excursionDate);

        CountDownLatch insertLatch = new CountDownLatch(1);
        final List<Excursion>[] insertedExcursions = new List[1];

        excursionDAO.insert(excursion);

        LiveData<List<Excursion>> insertListLiveData = excursionDAO.getAllExcursions();
        insertListLiveData.observeForever(new Observer<List<Excursion>>() {
            @Override
            public void onChanged(List<Excursion> excursions) {
                insertedExcursions[0] = excursions;
                insertLatch.countDown();
            }
        });

        insertLatch.await(2, TimeUnit.SECONDS);
        excursion.setExcursionID(insertedExcursions[0].get(0).getExcursionID());

        // Delete the excursion
        excursionDAO.delete(excursion);

        CountDownLatch deleteLatch = new CountDownLatch(1);
        final List<Excursion>[] deletedExcursions = new List[1];

        LiveData<List<Excursion>> deleteListLiveData = excursionDAO.getAllExcursions();
        deleteListLiveData.observeForever(new Observer<List<Excursion>>() {
            @Override
            public void onChanged(List<Excursion> excursions) {
                deletedExcursions[0] = excursions;
                deleteLatch.countDown();
            }
        });

        deleteLatch.await(2, TimeUnit.SECONDS);
        assertEquals(0, deletedExcursions[0].size());  // Ensure excursion is deleted
    }
}
