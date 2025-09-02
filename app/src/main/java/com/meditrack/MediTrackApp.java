package com.meditrack;

import android.app.Application;
import android.util.Log;
import androidx.work.Configuration;
import androidx.work.WorkManager;

public class MediTrackApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize WorkManager for background tasks
        WorkManager.initialize(
            this,
            new Configuration.Builder()
                .setMinimumLoggingLevel(Log.INFO)
                .build()
        );
    }
}
