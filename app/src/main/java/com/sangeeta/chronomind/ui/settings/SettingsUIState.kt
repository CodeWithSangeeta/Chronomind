package com.sangeeta.chronomind.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.ui.graphics.vector.ImageVector

enum class NotificationPermissionState {
    GRANTED,
    DENIED,
    NOT_REQUIRED
}

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
    val checkInStyle: String = "MANUAL",
    val streakOnMiss: String = "CONTINUE",
    val widgetItems: List<SettingsRowUiModel> = emptyList(),
    val helpItems: List<SettingsRowUiModel> = emptyList(),
    val trustItems: List<SettingsRowUiModel> = emptyList(),
    val aboutItems: List<SettingsRowUiModel> = emptyList(),
    val showClearDataConfirm: Boolean = false,
    val showResetConfirm: Boolean = false
)