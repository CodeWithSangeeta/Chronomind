package com.sangeeta.chronomind.ui.model

/**
 * UI-layer representation of an activity.
 * Converted from ActivityEntity in the ViewModel.
 */
data class ActivityUiModel(
    val id:               Int,
    val name:             String,
    val icon:             String,
    val colorHex:         String,      // "#FFCC00"
    val elapsedSeconds:   Long,
    val targetSeconds:    Long,
    val isRunning:        Boolean,
    val streakDays:       Int,
    val lastActiveDate:   String,
    val continueOnMiss:   Boolean
) {
    /** 0f..1f progress for the timer ring */
    val progress: Float
        get() = if (targetSeconds <= 0L) 0f
        else (elapsedSeconds.toFloat() / targetSeconds).coerceIn(0f, 1f)

    /** "1h 15m" formatted elapsed time */
    val elapsedFormatted: String
        get() {
            val h = elapsedSeconds / 3600
            val m = (elapsedSeconds % 3600) / 60
            return if (h > 0) "${h}h ${m}m" else "${m}m"
        }

    val statusLabel: String
        get() = when {
            isRunning -> "Running"
            elapsedSeconds > 0L -> "Paused"
            else -> "Ready"
        }
}