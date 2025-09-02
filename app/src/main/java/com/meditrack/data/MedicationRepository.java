package com.meditrack.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.meditrack.auth.AuthManager;
import com.meditrack.data.model.Medication;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedicationRepository {
    private final MedicationDao medicationDao;
    private final AuthManager authManager;
    private final ExecutorService executorService;

    public MedicationRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        medicationDao = db.medicationDao();
        authManager = AuthManager.getInstance();
        executorService = Executors.newFixedThreadPool(4);
    }

    public LiveData<List<Medication>> getAllMedications() {
        String userId = authManager.getCurrentUserId();
        return medicationDao.getAllMedications(userId);
    }

    public void addMedication(Medication medication, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                medication.setUserId(authManager.getCurrentUserId());
                medicationDao.insert(medication);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void updateMedication(Medication medication, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                medicationDao.update(medication);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void deleteMedication(Medication medication, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                medicationDao.delete(medication);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getActiveMedications(RepositoryCallback<List<Medication>> callback) {
        executorService.execute(() -> {
            try {
                String userId = authManager.getCurrentUserId();
                List<Medication> medications = medicationDao.getActiveMedications(userId);
                callback.onSuccess(medications);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}
