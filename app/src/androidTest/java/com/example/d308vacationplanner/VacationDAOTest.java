package com.example.d308vacationplanner;

import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.d308vacationplanner.database.VacationDatabaseBuilder;
import com.example.d308vacationplanner.dao.VacationDAO;
import com.example.d308vacationplanner.entities.Vacation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class VacationDAOTest {

    private VacationDatabaseBuilder vacationDatabase;
    private VacationDAO vacationDAO;
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
        vacationDAO = vacationDatabase.vacationDAO();
    }

    @After
    public void tearDown() {
        vacationDatabase.close(); // Close database after tests
    }

    @Test
    public void insertVacation() throws InterruptedException {
        // Create a vacation with non-null dates
        Date startDate = new Date();  // Use the current date
        Date endDate = new Date(startDate.getTime() + 86400000);  // 1 day later
        Vacation vacation = new Vacation(null, "USA", "Hotel California", startDate, endDate);

        vacationDAO.insert(vacation);

        // Test that the vacation was inserted
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Vacation>[] vacationList = new List[1];

        LiveData<List<Vacation>> vacationListLiveData = vacationDAO.getAllVacations();
        vacationListLiveData.observeForever(new Observer<List<Vacation>>() {
            @Override
            public void onChanged(List<Vacation> vacationListValue) {
                vacationList[0] = vacationListValue;
                latch.countDown(); // signal that the live data is updated
            }
        });

        latch.await(2, TimeUnit.SECONDS); // wait for LiveData to be updated
        assertEquals(1, vacationList[0].size());  // Ensure only one vacation is inserted
    }

    @Test
    public void updateVacation() throws InterruptedException {
        //insert vacation
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 86400000);
        Vacation vacation = new Vacation(null, "USA", "Hotel California", startDate, endDate);

        //First latch for insertion
        CountDownLatch insertLatch = new CountDownLatch(1);
        final List<Vacation>[] insertedVacations = new List[1];

        vacationDAO.insert(vacation);

        LiveData<List<Vacation>> insertListLiveData = vacationDAO.getAllVacations();
        insertListLiveData.observeForever(new Observer<List<Vacation>>() {
            @Override
            public void onChanged(List<Vacation> vacations) {
                insertedVacations[0] = vacations;
                insertLatch.countDown();
            }
        });

        //wait for promise callback
        insertLatch.await(2, TimeUnit.SECONDS);
        vacation.setVacationID(insertedVacations[0].get(0).getVacationID());

        //update the vacation
        vacation.setVacationName("Updated Location");
        vacationDAO.update(vacation);

        //new latch for update
        CountDownLatch updateLatch = new CountDownLatch(1);
        final List<Vacation>[] updatedVacations = new List[1];

        LiveData<List<Vacation>> updateListLiveData = vacationDAO.getAllVacations();
        updateListLiveData.observeForever(new Observer<List<Vacation>>() {
            @Override
            public void onChanged(List<Vacation> vacations) {
                updatedVacations[0] = vacations;
                updateLatch.countDown();
            }
        });

        updateLatch.await(2, TimeUnit.SECONDS);
        assertEquals("Updated Location", updatedVacations[0].get(0).getVacationName());
    }

    @Test
    public void deleteVacation() throws InterruptedException {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 86400000);
        Vacation vacation = new Vacation(null, "USA", "Hotel California", startDate, endDate);

        CountDownLatch insertLatch = new CountDownLatch(1);
        final List<Vacation>[] insertedVacations = new List[1];

        vacationDAO.insert(vacation);

        LiveData<List<Vacation>> insertListLiveData = vacationDAO.getAllVacations();
        insertListLiveData.observeForever(new Observer<List<Vacation>>() {
            @Override
            public void onChanged(List<Vacation> vacations) {
                insertedVacations[0] = vacations;
                insertLatch.countDown();
            }
        });

        insertLatch.await(2, TimeUnit.SECONDS);
        vacation.setVacationID(insertedVacations[0].get(0).getVacationID());

        vacationDAO.delete(vacation);

        CountDownLatch deleteLatch = new CountDownLatch(1);
        final List<Vacation>[] deletedVacations = new List[1];

        LiveData<List<Vacation>> deleteListLiveData = vacationDAO.getAllVacations();
        deleteListLiveData.observeForever(new Observer<List<Vacation>>() {
            @Override
            public void onChanged(List<Vacation> vacations) {
                deletedVacations[0] = vacations;
                deleteLatch.countDown();
            }
        });

        deleteLatch.await(2, TimeUnit.SECONDS);
        assertEquals(0, deletedVacations[0].size());
    }
}
