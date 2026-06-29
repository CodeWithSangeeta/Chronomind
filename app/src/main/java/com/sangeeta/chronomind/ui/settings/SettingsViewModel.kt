package com.sangeeta.chronomind.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Gavel
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.datastore.SettingsDataStore
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val onboardingRepo: OnboardingRepository,
    private val settingsDataStore: SettingsDataStore,
    private val activityRepo: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState(isLoading = true))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            combine(
                onboardingRepo.checkInStyle,
                onboardingRepo.streakOnMiss,
                settingsDataStore.isSoundEnabled,
                settingsDataStore.isVibrationEnabled,
                settingsDataStore.isKeepScreenOn,
                settingsDataStore.isDailyReminderEnabled,
                settingsDataStore.reminderTime,
                settingsDataStore.theme,
                activityRepo.observeAll()
            ) { checkInStyle,
                streakOnMiss,
                soundEnabled,
                vibrationEnabled,
                keepScreenOn,
                dailyReminderEnabled,
                reminderTime,
                theme,
                activities ->

                SettingsUiState(
                    isLoading = false,
                    checkInStyle = checkInStyle,
                    streakOnMiss = streakOnMiss,
                    isSoundEnabled = soundEnabled,
                    isVibrationEnabled = vibrationEnabled,
                    isKeepScreenOn = keepScreenOn,
                    isDailyReminderEnabled = dailyReminderEnabled,
                    reminderTime = reminderTime,
                    theme = theme,
                    totalActivities = activities.size,
                    showResetConfirm = _uiState.value.showResetConfirm,
                    showClearDataConfirm = _uiState.value.showClearDataConfirm,

                    generalItems = listOf(
                        SettingsRowUiModel(
                            id = "notifications",
                            title = "Notifications",
                            subtitle = "Reminders and alerts",
                            icon = Icons.Rounded.Notifications
                        ),
                        SettingsRowUiModel(
                            id = "appreminders",
                            title = "Reminder Schedule",
                            subtitle = "Default reminder timing",
                            icon = Icons.Rounded.Schedule
                        ),
                        SettingsRowUiModel(
                            id = "generalpreferences",
                            title = "App Preferences",
                            subtitle = "Haptics, behavior, and more",
                            icon = Icons.Rounded.Tune
                        )
                    ),

                    focusDefaultsItems = listOf(
                        SettingsRowUiModel(
                            id = "missedstreakbehavior",
                            title = "Missed Streak",
                            subtitle = "What happens after a missed day",
                            icon = Icons.Rounded.Whatshot,
                            value = streakOnMiss.toStreakDisplayLabel()
                        ),
                        SettingsRowUiModel(
                            id = "defaultcheckinstyle",
                            title = "Check-in Style",
                            subtitle = "Default way to check in",
                            icon = Icons.Rounded.CheckCircle,
                            value = checkInStyle.toCheckInDisplayLabel()
                        )
                    ),

                    widgetItems = listOf(
                        SettingsRowUiModel(
                            id = "openwidgetsetup",
                            title = "Widget Setup",
                            subtitle = "Choose activities and sizes",
                            icon = Icons.Rounded.Widgets
                        ),
                        SettingsRowUiModel(
                            id = "widgethelp",
                            title = "Widget Help",
                            subtitle = "How home screen widgets work",
                            icon = Icons.Rounded.Help
                        )
                    ),

                    helpItems = listOf(
                        SettingsRowUiModel(
                            id = "faq",
                            title = "Help Center",
                            subtitle = "Answers to common questions",
                            icon = Icons.Rounded.Help
                        ),
                        SettingsRowUiModel(
                            id = "sendfeedback",
                            title = "Send Feedback",
                            subtitle = "Share ideas and suggestions",
                            icon = Icons.Rounded.Feedback
                        ),
                        SettingsRowUiModel(
                            id = "reportissue",
                            title = "Report a Bug",
                            subtitle = "Something not working?",
                            icon = Icons.Rounded.Info
                        )
                    ),

                    trustItems = listOf(
                        SettingsRowUiModel(
                            id = "privacypolicy",
                            title = "Privacy Policy",
                            subtitle = "How your data is handled",
                            icon = Icons.Rounded.Policy,
                            isExternal = true
                        ),
                        SettingsRowUiModel(
                            id = "rateapp",
                            title = "Rate the App",
                            subtitle = "Support ChronoMind on Play Store",
                            icon = Icons.Rounded.Star,
                            isExternal = true
                        ),
                        SettingsRowUiModel(
                            id = "shareapp",
                            title = "Share the App",
                            subtitle = "Invite friends to try it",
                            icon = Icons.Rounded.Share,
                            isExternal = true
                        ),
                        SettingsRowUiModel(
                            id = "terms",
                            title = "Terms of Service",
                            subtitle = "App rules and usage terms",
                            icon = Icons.Rounded.Gavel,
                            isExternal = true
                        )
                    ),

                    aboutItems = listOf(
                        SettingsRowUiModel(
                            id = "aboutdeveloper",
                            title = "About the Developer",
                            subtitle = "Meet the creator of ChronoMind",
                            icon = Icons.Rounded.Person,
                            isExternal = true
                        ),
                        SettingsRowUiModel(
                            id = "appversion",
                            title = "App Version",
                            subtitle = "You're on the latest build",
                            icon = Icons.Rounded.Info,
                            value = "v1.2.0 (120)",
                            isValueOnly = true
                        ),
                        SettingsRowUiModel(
                            id = "licenses",
                            title = "Open Source Licenses",
                            subtitle = "Libraries and credits",
                            icon = Icons.Rounded.Code
                        )
                    )
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun setCheckInStyle(value: String) {
        viewModelScope.launch {
            onboardingRepo.setCheckInStyle(value)
        }
    }

    fun setStreakOnMiss(value: String) {
        viewModelScope.launch {
            onboardingRepo.setStreakOnMiss(value)
        }
    }

    fun setSoundEnabled(value: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setSoundEnabled(value)
        }
    }

    fun setVibrationEnabled(value: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setVibrationEnabled(value)
        }
    }

    fun setKeepScreenOn(value: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setKeepScreenOn(value)
        }
    }

    fun setDailyReminder(value: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDailyReminder(value)
        }
    }

    fun setReminderTime(value: String) {
        viewModelScope.launch {
            settingsDataStore.setReminderTime(value)
        }
    }

    fun setTheme(value: String) {
        viewModelScope.launch {
            settingsDataStore.setTheme(value)
        }
    }

    fun showResetConfirm(show: Boolean) {
        _uiState.update { it.copy(showResetConfirm = show) }
    }

    fun showClearDataConfirm(show: Boolean) {
        _uiState.update { it.copy(showClearDataConfirm = show) }
    }

    fun clearAllData() {
        viewModelScope.launch {
            activityRepo.clearAll()
            _uiState.update { it.copy(showClearDataConfirm = false) }
        }
    }

    fun resetOnboarding(onResetOnboarding: () -> Unit) {
        viewModelScope.launch {
            onboardingRepo.resetOnboarding()
            _uiState.update { it.copy(showResetConfirm = false) }
            onResetOnboarding()
        }
    }
}

private fun String.toCheckInDisplayLabel(): String {
    return when (uppercase()) {
        "MANUAL" -> "Manual"
        "AUTO_CHECK" -> "Auto Check"
        else -> replace("_", " ")
            .lowercase()
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
    }
}

private fun String.toStreakDisplayLabel(): String {
    return when (uppercase()) {
        "CONTINUE" -> "Continue"
        "RESET" -> "Reset"
        else -> replace("_", " ")
            .lowercase()
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
    }
}

