package com.sangeeta.chronomind.ui.model

import androidx.compose.ui.graphics.vector.ImageVector
import java.util.Locale

data class ActivityUiModel(
    val id: Int,
    val name: String,
    val icon: ImageVector,
    val colorHex: String,
    val elapsedSeconds: Long,
    val targetSeconds: Long,
    val isRunning: Boolean,
    val streakDays: Int,
    val lastActiveDate: String,
    val continueOnMiss: Boolean,
    val targetType: String,
    val completionStyle: String,
    val reminderEnabled: Boolean,
    val reminderTime: String,
    val sessionState: ActivitySessionState,
    val canStart: Boolean,
    val displayTime: String
) {
    val isStopwatch: Boolean
        get() = targetType == "STOPWATCH"

    val isTimerEnd: Boolean
        get() = completionStyle == "TIMEREND" || completionStyle == "TIMER_END"

    val remainingSeconds: Long
        get() = (targetSeconds - elapsedSeconds).coerceAtLeast(0L)

    val progress: Float
        get() = if (isStopwatch || targetSeconds <= 0L) {
            0f
        } else {
            (elapsedSeconds.toFloat() / targetSeconds.toFloat()).coerceIn(0f, 1f)
        }

    val elapsedFormatted: String
        get() = formatSeconds(elapsedSeconds)

    val remainingFormatted: String
        get() = formatSeconds(remainingSeconds)

    val checkInStyleLabel: String
        get() = if (completionStyle == "TIMEREND") "Auto check-in" else "Manual check-in"

    private fun formatSeconds(totalSeconds: Long): String {
        val h = totalSeconds / 3600
        val m = (totalSeconds % 3600) / 60
        val s = totalSeconds % 60
        return if (h > 0) {
            String.format(Locale.US, "%02d:%02d:%02d", h, m, s)
        } else {
            String.format(Locale.US, "%02d:%02d", m, s)
        }
    }
}