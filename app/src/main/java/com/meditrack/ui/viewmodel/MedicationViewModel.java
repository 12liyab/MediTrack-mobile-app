package com.meditrack.ui.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.meditrack.data.MedicationRepository;
import com.meditrack.data.model.Medication;
import com.meditrack.worker.MedicationReminderWorker;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MedicationViewModel extends AndroidViewModel {
    private final MedicationRepository repository;
    private final LiveData<List<Medication>> medications;

    public MedicationViewModel(Application application) {
        super(application);
        repository = new MedicationRepository(application);
        medications = repository.getAllMedications();
        setupPeriodicWork();
    }

    private void setupPeriodicWork() {
        PeriodicWorkRequest reminderWork = new PeriodicWorkRequest.Builder(
            MedicationReminderWorker.class,
            15, TimeUnit.MINUTES)
            .build();

        WorkManager.getInstance(getApplication())
            .enqueueUniquePeriodicWork(
                "MedicationReminders",
                ExistingPeriodicWorkPolicy.KEEP,
                reminderWork
            );
    }

    public LiveData<List<Medication>> getMedications() {
        return medications;
    }

    public void addMedication(Medication medication) {
        repository.addMedication(medication, new MedicationRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Handle success if needed
            }

            @Override
            public void onError(Exception e) {
                // Handle error if needed
            }
        });
    }

    public void updateMedication(Medication medication) {
        repository.updateMedication(medication, new MedicationRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Handle success if needed
            }

            @Override
            public void onError(Exception e) {
                // Handle error if needed
            }
        });
    }

    public void deleteMedication(Medication medication) {
        repository.deleteMedication(medication, new MedicationRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Handle success if needed
            }

            @Override
            public void onError(Exception e) {
                // Handle error if needed
            }
        });
    }
}
