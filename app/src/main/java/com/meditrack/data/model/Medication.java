package com.meditrack.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDateTime;

@Entity(tableName = "medications")
public class Medication {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String dosage;
    private String frequency;
    private LocalDateTime reminderTime;
    private String userId;
    private boolean isEnabled;
    private LocalDateTime lastSyncTime;

    public Medication(String name, String dosage, String frequency, LocalDateTime reminderTime, String userId) {
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.reminderTime = reminderTime;
        this.userId = userId;
        this.isEnabled = true;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public LocalDateTime getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(LocalDateTime lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }
}
