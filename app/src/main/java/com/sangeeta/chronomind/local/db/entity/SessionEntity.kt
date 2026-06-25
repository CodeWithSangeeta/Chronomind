package com.sangeeta.chronomind.local.db.entity

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val activityId: Int,
    val activityName: String,
    val activityIcon: ImageVector,
    val activityColorHex: String,
    val elapsedSeconds: Long,
    val targetSeconds: Long,
    val isCompleted: Boolean,
    val dateLabel: String
)