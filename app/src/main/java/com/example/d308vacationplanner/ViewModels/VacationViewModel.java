package com.example.d308vacationplanner.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.Vacation;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<Vacation>> allVacations;

    public VacationViewModel(Application application) {
        super(application);
        repository = new Repository(application);
        allVacations = repository.getAllVacations();
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return allVacations;
    }

    public LiveData<Vacation> getVacationByID(int vacationID) {
        return repository.getVacationByID(vacationID);
    }

    public void insert(Vacation vacation) {
        repository.insert(vacation);
    }

    public void update(Vacation vacation) {
        repository.update(vacation);
    }

    public void delete(Vacation vacation) {
        repository.delete(vacation);
    }
}
