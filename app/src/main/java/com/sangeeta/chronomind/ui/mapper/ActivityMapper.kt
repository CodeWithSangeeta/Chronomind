package com.sangeeta.chronomind.ui.mapper

import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.ui.model.ActivitySessionState
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun ActivityEntity.toUiModel(nowMillis: Long = System.currentTimeMillis()): ActivityUiModel {
    val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    val isCompletedToday = completedDate == today

    val computedElapsed = if (isRunning && sessionStartedAtEpochMillis != null) {
        val delta = ((nowMillis - sessionStartedAtEpochMillis) / 1000L).coerceAtLeast(0L)
        accumulatedElapsedBeforeStartSeconds + delta
    } else {
        maxOf(elapsedSeconds, accumulatedElapsedBeforeStartSeconds)
    }

    val computedRemaining = if (targetType == "STOPWATCH") {
        0L
    } else if (sessionEndsAtEpochMillis != null && isRunning) {
        ((sessionEndsAtEpochMillis - nowMillis) / 1000L).coerceAtLeast(0L)
    } else {
        (targetMinutes * 60L - computedElapsed).coerceAtLeast(0L)
    }

    val sessionState = when {
        isCompletedToday -> ActivitySessionState.COMPLETED_TODAY
        isRunning -> ActivitySessionState.RUNNING
        hasPendingSession && pendingSessionDate == today -> ActivitySessionState.PENDING
        else -> ActivitySessionState.IDLE
    }

    val displayTime = when {
        isCompletedToday -> formatDuration(computedElapsed)
        targetType == "STOPWATCH" -> formatDuration(computedElapsed)
        isRunning -> formatDuration(computedRemaining)
        sessionState == ActivitySessionState.PENDING -> formatDuration(computedRemaining)
        else -> formatDuration(targetMinutes * 60L)
    }

    return ActivityUiModel(
        id = id,
        name = name,
        icon = icon,
        colorHex = colorHex,
        elapsedSeconds = computedElapsed,
        targetSeconds = targetMinutes * 60L,
        isRunning = isRunning,
        streakDays = streakDays,
        lastActiveDate = formatLastUsed(lastActiveDate),
        continueOnMiss = continueStreakOnMiss,
        targetType = targetType,
        completionStyle = completionStyle,
        reminderEnabled = reminderEnabled,
        reminderTime = reminderTime,
        sessionState = sessionState,
        canStart = !isCompletedToday,
        displayTime = displayTime
    )
}

private fun formatLastUsed(dateStr: String): String {
    if (dateStr.isBlank()) return "Never"
    return try {
        val stored = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        val today = LocalDate.now()
        when (val days = ChronoUnit.DAYS.between(stored, today)) {
            0L -> "Today"
            1L -> "Yesterday"
            else -> "$days days ago"
        }
    } catch (_: Exception) {
        "Never"
    }
}

private fun formatDuration(totalSeconds: Long): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return if (h > 0) {
        String.format("%02d:%02d:%02d", h, m, s)
    } else {
        String.format("%02d:%02d", m, s)
    }
}