package com.sangeeta.chronomind.ui.mapper

import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Maps database entity to UI model.
 */
fun ActivityEntity.toUiModel(nowMillis: Long = System.currentTimeMillis()): ActivityUiModel {
    val lastUsedLabel = formatLastUsed(lastActiveDate)

    val computedElapsed = if (isRunning && sessionStartedAtEpochMillis != null) {
        val delta = ((nowMillis - sessionStartedAtEpochMillis) / 1000L).coerceAtLeast(0L)
        accumulatedElapsedBeforeStartSeconds + delta
    } else {
        maxOf(elapsedSeconds, accumulatedElapsedBeforeStartSeconds)
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
        lastActiveDate = lastUsedLabel,
        continueOnMiss = continueStreakOnMiss,
        targetType = targetType,
        completionStyle = completionStyle,
        reminderEnabled = reminderEnabled,
        reminderTime = reminderTime
    )
}

private fun formatLastUsed(dateStr: String): String {
    if (dateStr.isBlank()) return "Never"
    return try {
        val stored = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        val today  = LocalDate.now()
        when (val days = java.time.temporal.ChronoUnit.DAYS.between(stored, today)) {
            0L   -> "Today"
            1L   -> "Yesterday"
            else -> "$days days ago"
        }
    } catch (e: Exception) {
        dateStr
    }
}
