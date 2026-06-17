package com.sangeeta.chronomind.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val targetMinutes: Int,
    val elapsedSeconds: Long = 0L,
    val isRunning: Boolean = false,
    val streakDays: Int = 0,
    val lastActiveDate: String = "",
    val continueStreakOnMiss: Boolean = true,
    val orderIndex: Int = 0,
    val icon: String = "timer",
    val colorHex: String = "#6C63FF"
)