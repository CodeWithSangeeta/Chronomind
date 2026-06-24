//package com.sangeeta.chronomind.local.db.entity
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "activities")
//data class ActivityEntity(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val name: String,
//    val targetMinutes: Int,
//    val elapsedSeconds: Long = 0L,
//    val isRunning: Boolean = false,
//    val streakDays: Int = 0,
//    val lastActiveDate: String = "",
//    val continueStreakOnMiss: Boolean = true,
//    val orderIndex: Int = 0,
//    val icon: String = "timer",
//    val colorHex: String = "#6C63FF"
//)



package com.sangeeta.chronomind.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * VERSION 2 — Added: targetType, completionStyle, reminderEnabled, reminderTime
 * DB version must be bumped to 2 in ChronoDatabase
 */
@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val targetMinutes: Int,
    val elapsedSeconds: Long = 0L,
    val isRunning: Boolean = false,
    val streakDays: Int = 0,
    val lastActiveDate: String = "",           // ISO date "2026-06-25"
    val continueStreakOnMiss: Boolean = true,   // StreakBehavior setting
    val orderIndex: Int = 0,
    val icon: String = "⏱",
    val colorHex: String = "6C63FF",
    // --- NEW FIELDS (needs DB migration v1 → v2) ---
    val targetType: String = "TIME",           // "TIME" | "COUNT"
    val targetCount: Int = 0,                  // only used when targetType = COUNT
    val targetUnit: String = "",               // e.g. "pages", "reps"
    val completionStyle: String = "MANUAL",    // "MANUAL" | "TIMER_END" | "COUNT_CHECK"
    val reminderEnabled: Boolean = false,
    val reminderTime: String = "07:00 PM"
)