package com.meditrack.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val dosage: String,
    val frequency: String,
    val reminderTime: LocalDateTime,
    val userId: String,
    val isEnabled: Boolean = true,
    val lastSyncTime: LocalDateTime? = null
)
