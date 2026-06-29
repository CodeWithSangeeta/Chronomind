package com.sangeeta.chronomind.ui.model

import androidx.compose.ui.graphics.vector.ImageVector

enum class ActivitySessionState { IDLE, RUNNING, PENDING, COMPLETED_TODAY }

data class ActivityDisplayState(
    val activityId: Int,
    val name: String,
    val icon: ImageVector,
    val colorHex: String,
    val sessionState: ActivitySessionState,
    val isStopwatch: Boolean,
    val displayTime: String,
    val progress: Float,
    val streakDays: Int,
    val targetSeconds: Long,
    val elapsedSeconds: Long,
    val remainingSeconds: Long,
    val canStart: Boolean,
    val isRunning: Boolean
)