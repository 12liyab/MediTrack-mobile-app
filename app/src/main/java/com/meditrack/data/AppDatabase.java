package com.meditrack.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.meditrack.data.model.Medication;
import com.meditrack.util.Converters;

@Database(entities = {Medication.class, User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    
    public abstract MedicationDao medicationDao();
    public abstract UserDao userDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "meditrack_db"
            ).build();
        }
        return instance;
    }
}
