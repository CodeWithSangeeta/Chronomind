package com.sangeeta.chronomind.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.sangeeta.chronomind.ui.create_activity.CompletionStyle
import com.sangeeta.chronomind.ui.create_activity.StreakBehavior


data class SettingsRowUiModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector = Icons.Rounded.Info,
    val value: String? = null,
    val isExternal: Boolean = false,
    val isValueOnly: Boolean = false
)

data class SettingsUiState(
    val notificationsEnabled: Boolean = false,
    val isDailyReminderEnabled: Boolean = false,
    val reminderTime: String = "07:00 AM",
    val reminderHour: Int = 7,
    val reminderMinute: Int = 0,
    val reminderAmPm: String = "AM",

    val defaultCompletionStyle: CompletionStyle = CompletionStyle.MANUAL_CHECK,
    val defaultStreakOnMiss: StreakBehavior = StreakBehavior.CONTINUE_STREAK,

    val helpItems: List<SettingsRowUiModel> = emptyList(),
    val trustItems: List<SettingsRowUiModel> = emptyList(),
    val aboutItems: List<SettingsRowUiModel> = emptyList(),
    val showClearDataConfirm: Boolean = false,
    val showResetConfirm: Boolean = false,
    val isClearingData: Boolean = false,
)

