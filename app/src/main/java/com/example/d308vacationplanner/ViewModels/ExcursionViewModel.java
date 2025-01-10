package com.example.d308vacationplanner.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.Excursion;

import java.util.List;

public class ExcursionViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<Excursion>> allExcursions;

    public ExcursionViewModel(Application application) {
        super(application);
        repository = new Repository(application);
        allExcursions = repository.getAllExcursions();
    }

    public LiveData<List<Excursion>> getAllExcursions() {
        return allExcursions;
    }

    public LiveData<List<Excursion>> getAssociatedExcursions(int vacationID) {
        return repository.getAssociatedExcursions(vacationID);
    }

    public LiveData<Excursion> getExcursionByID(int excursionID) {
        return repository.getExcursionByID(excursionID);
    }

    public void insert(Excursion excursion) {
        repository.insert(excursion);
    }

    public void update(Excursion excursion) {
        repository.update(excursion);
    }

    public void delete(Excursion excursion) {
        repository.delete(excursion);
    }
}
