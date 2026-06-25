package com.sangeeta.chronomind.ui.model

import androidx.compose.ui.graphics.vector.ImageVector
import java.util.Locale

/**
 * Activity UI Model representing a focus activity in the UI layer.
 */
data class ActivityUiModel(
    val id: Int,
    val name: String,
    val icon: ImageVector,           // Changed from String to ImageVector
    val colorHex: String,
    val elapsedSeconds: Long,
    val targetSeconds: Long,        // 0 = stopwatch (no target)
    val isRunning: Boolean,
    val streakDays: Int,
    val lastActiveDate: String,
    val continueOnMiss: Boolean,
    val targetType: String,         // "TIMER" or "STOPWATCH"
    val completionStyle: String,    // "MANUAL" or "TIMER_END"
    val reminderEnabled: Boolean,
    val reminderTime: String
) {
    val isStopwatch: Boolean get() = targetType == "STOPWATCH"
    val isTimerEnd: Boolean get() = completionStyle == "TIMER_END"

    /** For TIMER mode: seconds remaining (counts down). */
    val remainingSeconds: Long get() = (targetSeconds - elapsedSeconds).coerceAtLeast(0L)

    /** For TIMER mode: progress 0f→1f. For STOPWATCH: always 0f. */
    val progress: Float
        get() = if (isStopwatch || targetSeconds == 0L) 0f
        else (elapsedSeconds.toFloat() / targetSeconds.toFloat()).coerceIn(0f, 1f)

    /** Formats elapsed time as HH:MM:SS or MM:SS */
    val elapsedFormatted: String
        get() = formatSeconds(elapsedSeconds)

    /** Formats remaining time (for TIMER mode) */
    val remainingFormatted: String
        get() = formatSeconds(remainingSeconds)

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
