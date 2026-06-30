package com.sangeeta.chronomind.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.datastore.SettingsDataStore
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//@HiltViewModel
//class SettingsViewModel @Inject constructor(
//    private val settingsDataStore: SettingsDataStore,
//    private val onboardingRepository: OnboardingRepository,
//    private val activityRepo: ActivityRepository
//) : ViewModel() {
//
//    private val localUiState = MutableStateFlow(SettingsUiState())
//
//    val uiState = combine(
//        localUiState,
//        settingsDataStore.isDailyReminderEnabled,
//        settingsDataStore.reminderTime,
//        onboardingRepository.checkInStyle,
//        onboardingRepository.streakOnMiss
//    ) { local, isDailyReminderEnabled, reminderTime, checkInStyle, streakOnMiss ->
//        local.copy(
//            isDailyReminderEnabled = isDailyReminderEnabled,
//            reminderTime = reminderTime,
//            checkInStyle = checkInStyle,
//            streakOnMiss = streakOnMiss,
//            widgetItems = defaultWidgetItems(),
//            helpItems = defaultHelpItems(),
//            trustItems = defaultTrustItems(),
//            aboutItems = defaultAboutItems()
//        )
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = SettingsUiState()
//    )
//
//    private val _events = Channel<SettingsEvent>(Channel.BUFFERED)
//    val events = _events.receiveAsFlow()
//
//    fun onNotificationsToggle(enabled: Boolean) {
//        viewModelScope.launch {
//            val isGranted = localUiState.value.notificationPermissionState ==
//                    NotificationPermissionState.GRANTED
//
//            when {
//                enabled && !isGranted -> {
//                    _events.send(SettingsEvent.RequestNotificationPermission)
//                }
//                !enabled && isGranted -> {
//                    _events.send(SettingsEvent.OpenNotificationSettings)
//                }
//            }
//        }
//    }
//
//    fun updateNotificationPermissionState(state: NotificationPermissionState) {
//        localUiState.update { it.copy(notificationPermissionState = state) }
//    }
//
//    fun setDailyReminder(value: Boolean) = viewModelScope.launch {
//        settingsDataStore.setDailyReminderEnabled(value)
//    }
//
//    fun setReminderTime(value: String) = viewModelScope.launch {
//        settingsDataStore.setReminderTime(value)
//    }
//
//    fun setCheckInStyle(value: String) = viewModelScope.launch {
//        onboardingRepository.setCheckInStyle(value)
//    }
//
//    fun setStreakOnMiss(value: String) = viewModelScope.launch {
//        onboardingRepository.setStreakOnMiss(value)
//    }
//
//    fun showClearDataConfirm(show: Boolean) {
//        localUiState.update { it.copy(showClearDataConfirm = show) }
//    }
//
//    fun showResetConfirm(show: Boolean) {
//        localUiState.update { it.copy(showResetConfirm = show) }
//    }
//
//    fun clearAllData() = viewModelScope.launch {
//        activityRepo.clearAllAppActivitiesAndSessions()
//        localUiState.update { it.copy(showClearDataConfirm = false) }
//    }
//
//    fun resetOnboarding(onDone: () -> Unit) = viewModelScope.launch {
//        onboardingRepository.resetOnboarding()
//        localUiState.update { it.copy(showResetConfirm = false) }
//        onDone()
//    }
//
//    private fun defaultWidgetItems(): List<SettingsRowUiModel> = listOf(
//        SettingsRowUiModel(
//            id = "widgetsetup",
//            title = "Widget setup",
//            subtitle = "Configure home screen widgets",
//            icon = Icons.Rounded.Widgets
//        )
//    )
//
//    private fun defaultHelpItems(): List<SettingsRowUiModel> = listOf(
//        SettingsRowUiModel(
//            id = "helpcenter",
//            title = "Help center",
//            subtitle = "Get support and guidance",
//            icon = Icons.Rounded.Help
//        ),
//        SettingsRowUiModel(
//            id = "shareapp",
//            title = "Share app",
//            subtitle = "Send ChronoMind to a friend",
//            icon = Icons.Rounded.Share
//        ),
//        SettingsRowUiModel(
//            id = "rateapp",
//            title = "Rate app",
//            subtitle = "Support us on Play Store",
//            icon = Icons.Rounded.StarRate,
//            isExternal = true
//        )
//    )
//
//    private fun defaultTrustItems(): List<SettingsRowUiModel> = listOf(
//        SettingsRowUiModel(
//            id = "privacy",
//            title = "Privacy policy",
//            subtitle = "Read how your data is handled",
//            icon = Icons.Rounded.PrivacyTip,
//            isExternal = true
//        ),
//        SettingsRowUiModel(
//            id = "terms",
//            title = "Terms and conditions",
//            subtitle = "Usage rules and legal details",
//            icon = Icons.Rounded.Policy,
//            isExternal = true
//        ),
//        SettingsRowUiModel(
//            id = "permissions",
//            title = "Permissions",
//            subtitle = "Understand app access",
//            icon = Icons.Rounded.Shield
//        )
//    )
//
//    private fun defaultAboutItems(): List<SettingsRowUiModel> = listOf(
//        SettingsRowUiModel(
//            id = "version",
//            title = "App version",
//            subtitle = "Current installed build",
//            icon = Icons.Rounded.Info,
//            value = "v1.0.0",
//            isValueOnly = true
//        ),
//        SettingsRowUiModel(
//            id = "developer",
//            title = "Developer",
//            subtitle = "Made with focus for better habits",
//            icon = Icons.Rounded.WorkspacePremium
//        )
//    )
//}
//
//sealed interface SettingsEvent {
//    data object RequestNotificationPermission : SettingsEvent
//    data object OpenNotificationSettings : SettingsEvent
//}
//
//fun NotificationPermissionState.toDisplayLabel(): String = when (this) {
//    NotificationPermissionState.GRANTED -> "Allowed"
//    NotificationPermissionState.DENIED -> "Not allowed"
//    NotificationPermissionState.NOT_REQUIRED -> "Not required"
//}





@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val onboardingRepository: OnboardingRepository,
    private val activityRepo: ActivityRepository
) : ViewModel() {

    private val localUiState = MutableStateFlow(SettingsUiState())

    val uiState = combine(
        localUiState,
        settingsDataStore.isDailyReminderEnabled,
        settingsDataStore.reminderTime,
        onboardingRepository.checkInStyle,
        onboardingRepository.streakOnMiss
    ) { local, isDailyReminderEnabled, reminderTime, checkInStyle, streakOnMiss ->

        val parsed = parseReminderTime(reminderTime)

        local.copy(
            isDailyReminderEnabled = isDailyReminderEnabled,
            reminderTime = reminderTime,
            reminderHour = parsed.hour,
            reminderMinute = parsed.minute,
            reminderAmPm = parsed.amPm,
            checkInStyle = checkInStyle,
            streakOnMiss = streakOnMiss,
            widgetItems = defaultWidgetItems(),
            helpItems = defaultHelpItems(),
            trustItems = defaultTrustItems(),
            aboutItems = defaultAboutItems()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    private val _events = Channel<SettingsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onNotificationsToggle(enabled: Boolean) {
        viewModelScope.launch {
            val isGranted =
                localUiState.value.notificationPermissionState == NotificationPermissionState.GRANTED

            when {
                enabled && !isGranted -> _events.send(SettingsEvent.RequestNotificationPermission)
                !enabled && isGranted -> _events.send(SettingsEvent.OpenNotificationSettings)
            }
        }
    }

    fun updateNotificationPermissionState(state: NotificationPermissionState) {
        localUiState.update { it.copy(notificationPermissionState = state) }
    }

    fun onReminderHourChange(hour: Int) {
        val current = uiState.value
        saveReminderTime(hour, current.reminderMinute, current.reminderAmPm)
    }

    fun onReminderMinuteChange(minute: Int) {
        val current = uiState.value
        saveReminderTime(current.reminderHour, minute, current.reminderAmPm)
    }

    fun onReminderAmPmChange(amPm: String) {
        val current = uiState.value
        saveReminderTime(current.reminderHour, current.reminderMinute, amPm)
    }

    fun setDailyReminder(value: Boolean) = viewModelScope.launch {
        settingsDataStore.setDailyReminderEnabled(value)
    }

    fun setCheckInStyle(value: String) = viewModelScope.launch {
        onboardingRepository.setCheckInStyle(value)
    }

    fun setStreakOnMiss(value: String) = viewModelScope.launch {
        onboardingRepository.setStreakOnMiss(value)
    }

    fun showClearDataConfirm(show: Boolean) {
        localUiState.update { it.copy(showClearDataConfirm = show) }
    }

    fun showResetConfirm(show: Boolean) {
        localUiState.update { it.copy(showResetConfirm = show) }
    }

    fun clearAllData() = viewModelScope.launch {
        activityRepo.clearAllAppActivitiesAndSessions()
        localUiState.update { it.copy(showClearDataConfirm = false) }
    }

    fun resetOnboarding(onDone: () -> Unit) = viewModelScope.launch {
        onboardingRepository.resetOnboarding()
        localUiState.update { it.copy(showResetConfirm = false) }
        onDone()
    }

    private data class ReminderParts(
        val hour: Int,
        val minute: Int,
        val amPm: String
    )

    private fun parseReminderTime(value: String): ReminderParts {
        return try {
            val parts = value.trim().split(" ")
            val timePart = parts.getOrNull(0) ?: "07:00"
            val amPmPart = parts.getOrNull(1) ?: "AM"
            val timePieces = timePart.split(":")
            val hour = timePieces.getOrNull(0)?.toIntOrNull()?.coerceIn(1, 12) ?: 7
            val minute = timePieces.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0

            ReminderParts(
                hour = hour,
                minute = minute,
                amPm = if (amPmPart.uppercase() == "PM") "PM" else "AM"
            )
        } catch (_: Exception) {
            ReminderParts(7, 0, "AM")
        }
    }

    private fun formatReminderTime(hour: Int, minute: Int, amPm: String): String {
        val safeHour = hour.coerceIn(1, 12)
        val safeMinute = minute.coerceIn(0, 59)
        val safeAmPm = if (amPm.uppercase() == "PM") "PM" else "AM"
        return "%02d:%02d %s".format(safeHour, safeMinute, safeAmPm)
    }

    private fun saveReminderTime(hour: Int, minute: Int, amPm: String) {
        viewModelScope.launch {
            settingsDataStore.setReminderTime(formatReminderTime(hour, minute, amPm))
        }
    }

    private fun defaultWidgetItems(): List<SettingsRowUiModel> = listOf(
        SettingsRowUiModel(
            id = "widgetsetup",
            title = "Widget setup",
            subtitle = "Configure home screen widgets",
            icon = Icons.Rounded.Widgets
        )
    )

    private fun defaultHelpItems(): List<SettingsRowUiModel> = listOf(
        SettingsRowUiModel(
            id = "helpcenter",
            title = "Help center",
            subtitle = "Get support and guidance",
            icon = Icons.Rounded.Help
        ),
        SettingsRowUiModel(
            id = "shareapp",
            title = "Share app",
            subtitle = "Send ChronoMind to a friend",
            icon = Icons.Rounded.Share
        ),
        SettingsRowUiModel(
            id = "rateapp",
            title = "Rate app",
            subtitle = "Support us on Play Store",
            icon = Icons.Rounded.StarRate,
            isExternal = true
        )
    )

    private fun defaultTrustItems(): List<SettingsRowUiModel> = listOf(
        SettingsRowUiModel(
            id = "privacy",
            title = "Privacy policy",
            subtitle = "Read how your data is handled",
            icon = Icons.Rounded.PrivacyTip,
            isExternal = true
        ),
        SettingsRowUiModel(
            id = "terms",
            title = "Terms and conditions",
            subtitle = "Usage rules and legal details",
            icon = Icons.Rounded.Policy,
            isExternal = true
        ),
        SettingsRowUiModel(
            id = "permissions",
            title = "Permissions",
            subtitle = "Understand app access",
            icon = Icons.Rounded.Shield
        )
    )

    private fun defaultAboutItems(): List<SettingsRowUiModel> = listOf(
        SettingsRowUiModel(
            id = "version",
            title = "App version",
            subtitle = "Current installed build",
            icon = Icons.Rounded.Info,
            value = "v1.0.0",
            isValueOnly = true
        ),
        SettingsRowUiModel(
            id = "developer",
            title = "Developer",
            subtitle = "Made with focus for better habits",
            icon = Icons.Rounded.WorkspacePremium
        )
    )
}



sealed interface SettingsEvent {
    data object RequestNotificationPermission : SettingsEvent
    data object OpenNotificationSettings : SettingsEvent
}



