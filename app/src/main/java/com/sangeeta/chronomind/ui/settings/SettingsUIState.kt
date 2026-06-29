package com.sangeeta.chronomind.ui.settings

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsUiState(
    val isLoading: Boolean = true,
    val notificationPermissionState: NotificationPermissionState = NotificationPermissionState.Unknown,
    val isDailyReminderEnabled: Boolean = false,
    val reminderTime: String = "07:00 AM",
    val checkInStyle: String = "MANUAL",
    val streakOnMiss: String = "CONTINUE",
    val generalItems: List<SettingsRowUiModel> = emptyList(),
    val focusDefaultsItems: List<SettingsRowUiModel> = emptyList(),
    val widgetItems: List<SettingsRowUiModel> = emptyList(),
    val helpItems: List<SettingsRowUiModel> = emptyList(),
    val trustItems: List<SettingsRowUiModel> = emptyList(),
    val aboutItems: List<SettingsRowUiModel> = emptyList(),
    val showClearDataConfirm: Boolean = false,
    val showResetConfirm: Boolean = false
)


data class SettingsRowUiModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val value: String? = null,
    val isExternal: Boolean = false,
    val isValueOnly: Boolean = false
)