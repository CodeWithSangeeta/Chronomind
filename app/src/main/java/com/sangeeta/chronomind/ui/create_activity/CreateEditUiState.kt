package com.sangeeta.chronomind.ui.create_activity

import com.sangeeta.chronomind.ui.model.ActivityColorOption
import com.sangeeta.chronomind.ui.model.ActivityIconOption

enum class CreateEditMode { CREATE, EDIT }

enum class TargetType(val label: String) {
    TIMER("Timer"),
    STOPWATCH("Stopwatch")
}

enum class StreakBehavior(val label: String) {
    CONTINUE_STREAK("Continue streak"),
    RESET_TO_ZERO("Reset to zero")
}

enum class CompletionStyle(val label: String) {
    MANUAL_CHECK("Manual check"),
    AUTO_CHECK("Auto check")
}

data class CreateEditUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val mode: CreateEditMode = CreateEditMode.CREATE,
    val editingActivityId: Int? = null,
    val activityName: String = "",
    val selectedIcon: ActivityIconOption = ActivityIconOption.GROWTH,
    val selectedColor: ActivityColorOption = ActivityColorOption.AMBER,
    val targetType: TargetType = TargetType.TIMER,
    val targetHours: Int = 0,
    val targetMinutes: Int = 45,
    val reminderEnabled: Boolean = false,
    val reminderTime: String = "07:00 PM",
    val isAdvancedExpanded: Boolean = false,
    val streakBehavior: StreakBehavior = StreakBehavior.CONTINUE_STREAK,
    val completionStyle: CompletionStyle = CompletionStyle.MANUAL_CHECK,
    val showDeleteConfirm: Boolean = false,

    val existingElapsedSeconds: Long = 0L,
    val existingStreakDays: Int = 0,
    val existingLastActiveDate: String = "",
    val existingOrderIndex: Int = 0,
    val existingHasPendingSession: Boolean = false,
    val existingPendingSessionDate: String = "",
    val existingSessionStartedAtEpochMillis: Long? = null,
    val existingSessionEndsAtEpochMillis: Long? = null,
    val existingAccumulatedElapsedBeforeStartSeconds: Long = 0L,
    val existingCompletedDate: String = ""
) {
    val isValid: Boolean
        get() = activityName.trim().isNotEmpty() &&
                (targetType == TargetType.STOPWATCH || (targetHours * 60 + targetMinutes) > 0)

    val screenTitle: String
        get() = if (mode == CreateEditMode.CREATE) "New Activity" else "Edit Activity"
}
