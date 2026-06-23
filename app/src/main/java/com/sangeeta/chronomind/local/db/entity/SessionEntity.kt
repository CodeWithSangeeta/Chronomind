package com.sangeeta.chronomind.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// local/db/entity/SessionEntity.kt
@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val activityId: Int,
    val activityName: String,
    val activityIcon: String,
    val activityColorHex: String,
    val elapsedSeconds: Long,   // how long they actually ran
    val targetSeconds: Long,    // the goal
    val isCompleted: Boolean,   // elapsed >= target
    val dateLabel: String       // "2026-06-23" ISO date string
)