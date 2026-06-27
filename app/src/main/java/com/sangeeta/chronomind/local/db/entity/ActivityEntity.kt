package com.sangeeta.chronomind.local.db.entity

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val targetMinutes: Int,
    val elapsedSeconds: Long = 0L,
    val isRunning: Boolean = false,
    val streakDays: Int = 0,
    val lastActiveDate: String = "",
    val continueStreakOnMiss: Boolean = true,
    val orderIndex: Int = 0,
    val icon: ImageVector,
    val colorHex: String = "FFC328",
    val targetType: String = "TIMER",
    val completionStyle: String = "MANUAL",
    val reminderEnabled: Boolean = false,
    val reminderTime: String = "07:00 PM",
    val hasPendingSession: Boolean = false,
    val pendingSessionDate: String = "",
    val sessionStartedAtEpochMillis: Long? = null,
    val sessionEndsAtEpochMillis: Long? = null,
    val accumulatedElapsedBeforeStartSeconds: Long = 0L,
    val completedDate: String = ""
)
