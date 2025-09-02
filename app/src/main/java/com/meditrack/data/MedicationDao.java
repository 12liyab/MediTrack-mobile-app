package com.meditrack.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.meditrack.data.model.Medication;
import java.util.List;

@Dao
public interface MedicationDao {
    @Query("SELECT * FROM medications WHERE userId = :userId")
    LiveData<List<Medication>> getAllMedications(String userId);

    @Insert
    void insert(Medication medication);

    @Update
    void update(Medication medication);

    @Delete
    void delete(Medication medication);

    @Query("SELECT * FROM medications WHERE userId = :userId AND isEnabled = 1")
    List<Medication> getActiveMedications(String userId);
}
