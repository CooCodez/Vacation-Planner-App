package com.example.d308vacationplanner.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.d308vacationplanner.dao.ExcursionDAO;
import com.example.d308vacationplanner.dao.VacationDAO;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final ExcursionDAO mExcursionDAO;
    private final VacationDAO mVacationDAO;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
    }

    // Retrieve all vacations as LiveData
    public LiveData<List<Vacation>> getAllVacations() {
        return mVacationDAO.getAllVacations();
    }

    // Retrieve all excursions as LiveData
    public LiveData<List<Excursion>> getAllExcursions() {
        return mExcursionDAO.getAllExcursions();
    }

    // Retrieve excursions by vacationID as LiveData
    public LiveData<List<Excursion>> getAssociatedExcursions(int vacationID) {
        return mExcursionDAO.getAssociatedExcursions(vacationID);
    }

    // Retrieve vacation by ID as LiveData
    public LiveData<Vacation> getVacationByID(int vacationID) {
        return mVacationDAO.getVacationByID(vacationID);
    }

    // Retrieve excursion by ID as LiveData
    public LiveData<Excursion> getExcursionByID(int excursionID) {
        return mExcursionDAO.getExcursionByID(excursionID);
    }

    // Insert a vacation
    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.insert(vacation));
    }

    // Update a vacation
    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.update(vacation));
    }

    // Delete a vacation
    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.delete(vacation));
    }

    // Insert an excursion
    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.insert(excursion));
    }

    // Update an excursion
    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.update(excursion));
    }

    // Delete an excursion
    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.delete(excursion));
    }
}
