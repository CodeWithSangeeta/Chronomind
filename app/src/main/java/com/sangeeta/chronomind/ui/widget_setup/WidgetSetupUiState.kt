package com.sangeeta.chronomind.ui.widget_setup

import androidx.compose.ui.graphics.Color

data class WidgetSetupUiState(
    val isLoading: Boolean = false,
    val activities: List<WidgetActivityUiModel> = emptyList(),
    val selectedActivityId: Int? = null,
    val selectedSize: WidgetSizeOption = WidgetSizeOption.MEDIUM,
    val showHelpDialog: Boolean = false
) {
    val selectedActivity: WidgetActivityUiModel?
        get() = activities.firstOrNull { it.id == selectedActivityId }

    val canSetWidget: Boolean
        get() = selectedActivity != null
}

data class WidgetActivityUiModel(
    val id: Int,
    val name: String,
    val iconEmoji: String,
    val accentColor: Color,
    val targetText: String,
    val streakDays: Int,
    val progressPercent: Int,
    val progressText: String,
    val isRunning: Boolean
)

enum class WidgetSizeOption(
    val label: String,
    val sizeLabel: String
) {
    SMALL("Small", "2 × 1"),
    MEDIUM("Medium", "4 × 2"),
    LARGE("Large", "4 × 3")
}