//package com.sangeeta.chronomind.ui.settings
//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.CheckCircle
//import androidx.compose.material.icons.rounded.Code
//import androidx.compose.material.icons.rounded.Feedback
//import androidx.compose.material.icons.rounded.Flag
//import androidx.compose.material.icons.rounded.Gavel
//import androidx.compose.material.icons.rounded.Help
//import androidx.compose.material.icons.rounded.Info
//import androidx.compose.material.icons.rounded.Notifications
//import androidx.compose.material.icons.rounded.Person
//import androidx.compose.material.icons.rounded.Policy
//import androidx.compose.material.icons.rounded.Schedule
//import androidx.compose.material.icons.rounded.Share
//import androidx.compose.material.icons.rounded.Star
//import androidx.compose.material.icons.rounded.Tune
//import androidx.compose.material.icons.rounded.Whatshot
//import androidx.compose.material.icons.rounded.Widgets
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.lifecycle.ViewModel
//import com.sangeeta.chronomind.repository.OnboardingRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//
//@HiltViewModel
//class SettingsViewModel @Inject constructor(
//    private val onboardingRepository: OnboardingRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(
//        SettingsUiState(
//            generalItems = listOf(
//                SettingsRowUiModel(
//                    id = "notifications",
//                    title = "Notifications",
//                    subtitle = "Reminders and alerts",
//                    icon = Icons.Rounded.Notifications
//                ),
//                SettingsRowUiModel(
//                    id = "app_reminders",
//                    title = "Reminder Schedule",
//                    subtitle = "Default reminder timing",
//                    icon = Icons.Rounded.Schedule
//                ),
//                SettingsRowUiModel(
//                    id = "general_preferences",
//                    title = "App Preferences",
//                    subtitle = "Haptics, behavior, and more",
//                    icon = Icons.Rounded.Tune
//                )
//            ),
//            focusDefaultsItems = listOf(
//                SettingsRowUiModel(
//                    id = "missed_streak_behavior",
//                    title = "Missed Streak",
//                    subtitle = "What happens after a missed day",
//                    icon = Icons.Rounded.Whatshot,
//                    value = "Continue"
//                ),
//                SettingsRowUiModel(
//                    id = "default_check_in_style",
//                    title = "Check-in Style",
//                    subtitle = "Default way to check in",
//                    icon = Icons.Rounded.CheckCircle,
//                    value = "Manual"
//                ),
//                SettingsRowUiModel(
//                    id = "default_completion_behavior",
//                    title = "Completion Style",
//                    subtitle = "Default way to mark complete",
//                    icon = Icons.Rounded.Flag,
//                    value = "Manual"
//                )
//            ),
//            widgetItems = listOf(
//                SettingsRowUiModel(
//                    id = "open_widget_setup",
//                    title = "Widget Setup",
//                    subtitle = "Choose activities and sizes",
//                    icon = Icons.Rounded.Widgets
//                ),
//                SettingsRowUiModel(
//                    id = "widget_help",
//                    title = "Widget Help",
//                    subtitle = "How home screen widgets work",
//                    icon = Icons.Rounded.Help
//                )
//            ),
//            helpItems = listOf(
//                SettingsRowUiModel(
//                    id = "faq",
//                    title = "Help Center",
//                    subtitle = "Answers to common questions",
//                    icon = Icons.Rounded.Help
//                ),
//                SettingsRowUiModel(
//                    id = "send_feedback",
//                    title = "Send Feedback",
//                    subtitle = "Share ideas and suggestions",
//                    icon = Icons.Rounded.Feedback
//                ),
//                SettingsRowUiModel(
//                    id = "report_issue",
//                    title = "Report a Bug",
//                    subtitle = "Something not working?",
//                    icon = Icons.Rounded.Info
//                )
//            ),
//            trustItems = listOf(
//                SettingsRowUiModel(
//                    id = "privacy_policy",
//                    title = "Privacy Policy",
//                    subtitle = "How your data is handled",
//                    icon = Icons.Rounded.Policy,
//                    isExternal = true
//                ),
//                SettingsRowUiModel(
//                    id = "rate_app",
//                    title = "Rate the App",
//                    subtitle = "Support ChronoMind on Play Store",
//                    icon = Icons.Rounded.Star,
//                    isExternal = true
//                ),
//                SettingsRowUiModel(
//                    id = "share_app",
//                    title = "Share the App",
//                    subtitle = "Invite friends to try it",
//                    icon = Icons.Rounded.Share,
//                    isExternal = true
//                ),
//                SettingsRowUiModel(
//                    id = "terms",
//                    title = "Terms of Service",
//                    subtitle = "App rules and usage terms",
//                    icon = Icons.Rounded.Gavel,
//                    isExternal = true
//                )
//            ),
//            aboutItems = listOf(
//                SettingsRowUiModel(
//                    id = "about_developer",
//                    title = "About the Developer",
//                    subtitle = "Meet the creator of ChronoMind",
//                    icon = Icons.Rounded.Person,
//                    isExternal = true
//                ),
//                SettingsRowUiModel(
//                    id = "app_version",
//                    title = "App Version",
//                    subtitle = "You’re on the latest build",
//                    icon = Icons.Rounded.Info,
//                    value = "v1.2.0 (120)",
//                    isValueOnly = true
//                ),
//                SettingsRowUiModel(
//                    id = "licenses",
//                    title = "Open Source Licenses",
//                    subtitle = "Libraries and credits",
//                    icon = Icons.Rounded.Code
//                )
//            )
//        )
//    )
//
//    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
//}
//
//data class SettingsUiState(
//    val generalItems: List<SettingsRowUiModel> = emptyList(),
//    val focusDefaultsItems: List<SettingsRowUiModel> = emptyList(),
//    val widgetItems: List<SettingsRowUiModel> = emptyList(),
//    val helpItems: List<SettingsRowUiModel> = emptyList(),
//    val trustItems: List<SettingsRowUiModel> = emptyList(),
//    val aboutItems: List<SettingsRowUiModel> = emptyList()
//)
//
//data class SettingsRowUiModel(
//    val id: String,
//    val title: String,
//    val subtitle: String,
//    val icon: ImageVector,
//    val value: String? = null,
//    val isExternal: Boolean = false,
//    val isValueOnly: Boolean = false
//)




package com.sangeeta.chronomind.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Flag
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
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            combine(
                onboardingRepository.checkInStyle,
                onboardingRepository.streakOnMiss
            ) { checkInStyle, streakOnMiss ->
                Pair(checkInStyle, streakOnMiss)
            }.collect { (checkInStyle, streakOnMiss) ->
                _uiState.update {
                    buildSettingsUiState(
                        checkInStyle = checkInStyle.toDisplayLabel(),
                        streakOnMiss = streakOnMiss.toDisplayLabel()
                    )
                }
            }
        }
    }

    private fun buildSettingsUiState(
        checkInStyle: String,
        streakOnMiss: String
    ): SettingsUiState {
        return SettingsUiState(
            generalItems = listOf(
                SettingsRowUiModel(
                    id = "notifications",
                    title = "Notifications",
                    subtitle = "Reminders and alerts",
                    icon = Icons.Rounded.Notifications
                ),
                SettingsRowUiModel(
                    id = "app_reminders",
                    title = "Reminder Schedule",
                    subtitle = "Default reminder timing",
                    icon = Icons.Rounded.Schedule
                ),
                SettingsRowUiModel(
                    id = "general_preferences",
                    title = "App Preferences",
                    subtitle = "Haptics, behavior, and more",
                    icon = Icons.Rounded.Tune
                )
            ),
            focusDefaultsItems = listOf(
                SettingsRowUiModel(
                    id = "missed_streak_behavior",
                    title = "Missed Streak",
                    subtitle = "What happens after a missed day",
                    icon = Icons.Rounded.Whatshot,
                    value = streakOnMiss
                ),
                SettingsRowUiModel(
                    id = "default_check_in_style",
                    title = "Check-in Style",
                    subtitle = "Default way to check in",
                    icon = Icons.Rounded.CheckCircle,
                    value = checkInStyle
                ),
                SettingsRowUiModel(
                    id = "default_completion_behavior",
                    title = "Completion Style",
                    subtitle = "Default way to mark complete",
                    icon = Icons.Rounded.Flag,
                    value = "Manual"
                )
            ),
            widgetItems = listOf(
                SettingsRowUiModel(
                    id = "open_widget_setup",
                    title = "Widget Setup",
                    subtitle = "Choose activities and sizes",
                    icon = Icons.Rounded.Widgets
                ),
                SettingsRowUiModel(
                    id = "widget_help",
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
                    id = "send_feedback",
                    title = "Send Feedback",
                    subtitle = "Share ideas and suggestions",
                    icon = Icons.Rounded.Feedback
                ),
                SettingsRowUiModel(
                    id = "report_issue",
                    title = "Report a Bug",
                    subtitle = "Something not working?",
                    icon = Icons.Rounded.Info
                )
            ),
            trustItems = listOf(
                SettingsRowUiModel(
                    id = "privacy_policy",
                    title = "Privacy Policy",
                    subtitle = "How your data is handled",
                    icon = Icons.Rounded.Policy,
                    isExternal = true
                ),
                SettingsRowUiModel(
                    id = "rate_app",
                    title = "Rate the App",
                    subtitle = "Support ChronoMind on Play Store",
                    icon = Icons.Rounded.Star,
                    isExternal = true
                ),
                SettingsRowUiModel(
                    id = "share_app",
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
                    id = "about_developer",
                    title = "About the Developer",
                    subtitle = "Meet the creator of ChronoMind",
                    icon = Icons.Rounded.Person,
                    isExternal = true
                ),
                SettingsRowUiModel(
                    id = "app_version",
                    title = "App Version",
                    subtitle = "You’re on the latest build",
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
    }

    private fun String.toDisplayLabel(): String {
        return when (this.uppercase()) {
            "CONTINUE" -> "Continue"
            "RESET" -> "Reset"
            "MANUAL" -> "Manual"
            "REMINDER" -> "Reminder"
            else -> replace("_", " ")
                .lowercase()
                .split(" ")
                .joinToString(" ") { word ->
                    word.replaceFirstChar { ch -> ch.uppercase() }
                }
        }
    }
}

data class SettingsUiState(
    val generalItems: List<SettingsRowUiModel> = emptyList(),
    val focusDefaultsItems: List<SettingsRowUiModel> = emptyList(),
    val widgetItems: List<SettingsRowUiModel> = emptyList(),
    val helpItems: List<SettingsRowUiModel> = emptyList(),
    val trustItems: List<SettingsRowUiModel> = emptyList(),
    val aboutItems: List<SettingsRowUiModel> = emptyList()
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




//
//package com.sangeeta.chronomind.ui.settings
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.sangeeta.chronomind.local.datastore.SettingsDataStore
//import com.sangeeta.chronomind.repository.ActivityRepository
//import com.sangeeta.chronomind.repository.OnboardingRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//data class SettingsUiState(
//    val isLoading: Boolean = true,
//    // Profile
//    val userName: String = "",
//    // Behaviour (from onboarding DataStore)
//    val accountabilityType: String = "STREAKS",
//    val checkInStyle: String = "MANUAL",
//    val streakOnMiss: String = "CONTINUE",
//    // App preferences (from settings DataStore)
//    val isSoundEnabled: Boolean = true,
//    val isVibrationEnabled: Boolean = true,
//    val isKeepScreenOn: Boolean = true,
//    val isDailyReminderEnabled: Boolean = false,
//    val reminderTime: String = "07:00 AM",
//    val theme: String = "DARK",
//    // Stats
//    val totalActivities: Int = 0,
//    // Dialog
//    val showResetConfirm: Boolean = false,
//    val showClearDataConfirm: Boolean = false,
//    val saveSuccess: Boolean = false
//)
//
//@HiltViewModel
//class SettingsViewModel @Inject constructor(
//    private val onboardingRepo: OnboardingRepository,
//    private val settingsDataStore: SettingsDataStore,
//    private val activityRepo: ActivityRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(SettingsUiState())
//    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
//
//    init {
//        observeSettings()
//    }
//
//    private fun observeSettings() {
//        viewModelScope.launch {
//            combine(
//                onboardingRepo.userName,
//                onboardingRepo.accountabilityType,
//                onboardingRepo.checkInStyle,
//                onboardingRepo.streakOnMiss,
//                activityRepo.observeAll()
//            ) { name, accountability, checkIn, streakMiss, activities ->
//                SettingsPartialA(name, accountability, checkIn, streakMiss, activities.size)
//            }.collect { a ->
//                _uiState.update {
//                    it.copy(
//                        userName = a.name,
//                        accountabilityType = a.accountability,
//                        checkInStyle = a.checkIn,
//                        streakOnMiss = a.streakMiss,
//                        totalActivities = a.totalActivities
//                    )
//                }
//            }
//        }
//
//        viewModelScope.launch {
//            combine(
//                settingsDataStore.isSoundEnabled,
//                settingsDataStore.isVibrationEnabled,
//                settingsDataStore.isKeepScreenOn,
//                settingsDataStore.isDailyReminderEnabled,
//                settingsDataStore.reminderTime
//            ) { sound, vibration, screen, reminder, time ->
//                SettingsPartialB(sound, vibration, screen, reminder, time)
//            }.collect { b ->
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        isSoundEnabled = b.sound,
//                        isVibrationEnabled = b.vibration,
//                        isKeepScreenOn = b.keepScreenOn,
//                        isDailyReminderEnabled = b.dailyReminder,
//                        reminderTime = b.reminderTime
//                    )
//                }
//            }
//        }
//    }
//
//    // ── Profile ───────────────────────────────────────────────────────────────
//
//    fun updateUserName(name: String) {
//        _uiState.update { it.copy(userName = name) }
//    }
//
//    fun saveProfile() {
//        viewModelScope.launch {
//            val state = _uiState.value
//            onboardingRepo.saveOnboardingResult(
//                name = state.userName.trim(),
//                accountability = state.accountabilityType,
//                checkIn = state.checkInStyle,
//                streakMiss = state.streakOnMiss
//            )
//            _uiState.update { it.copy(saveSuccess = true) }
//        }
//    }
//
//    fun clearSaveSuccess() { _uiState.update { it.copy(saveSuccess = false) } }
//
//    // ── Behaviour preferences ────────────────────────────────────────────────
//
//    fun setAccountabilityType(value: String) {
//        _uiState.update { it.copy(accountabilityType = value) }
//    }
//
//    fun setCheckInStyle(value: String) {
//        _uiState.update { it.copy(checkInStyle = value) }
//    }
//
//    fun setStreakOnMiss(value: String) {
//        _uiState.update { it.copy(streakOnMiss = value) }
//    }
//
//    fun saveBehaviourPrefs() {
//        viewModelScope.launch {
//            val state = _uiState.value
//            onboardingRepo.saveOnboardingResult(
//                name = state.userName.trim(),
//                accountability = state.accountabilityType,
//                checkIn = state.checkInStyle,
//                streakMiss = state.streakOnMiss
//            )
//            _uiState.update { it.copy(saveSuccess = true) }
//        }
//    }
//
//    // ── App preferences ──────────────────────────────────────────────────────
//
//    fun setSoundEnabled(value: Boolean) {
//        viewModelScope.launch { settingsDataStore.setSoundEnabled(value) }
//    }
//
//    fun setVibrationEnabled(value: Boolean) {
//        viewModelScope.launch { settingsDataStore.setVibrationEnabled(value) }
//    }
//
//    fun setKeepScreenOn(value: Boolean) {
//        viewModelScope.launch { settingsDataStore.setKeepScreenOn(value) }
//    }
//
//    fun setDailyReminder(value: Boolean) {
//        viewModelScope.launch { settingsDataStore.setDailyReminder(value) }
//    }
//
//    fun setReminderTime(value: String) {
//        viewModelScope.launch { settingsDataStore.setReminderTime(value) }
//    }
//
//    // ── Danger zone ──────────────────────────────────────────────────────────
//
//    fun showResetConfirm(show: Boolean) { _uiState.update { it.copy(showResetConfirm = show) } }
//    fun showClearDataConfirm(show: Boolean) { _uiState.update { it.copy(showClearDataConfirm = show) } }
//
//    fun clearAllData() {
//        viewModelScope.launch {
//            activityRepo.clearAll()
//            _uiState.update { it.copy(showClearDataConfirm = false) }
//        }
//    }
//
//    fun resetOnboarding() {
//        viewModelScope.launch {
//            onboardingRepo.resetOnboarding()
//            _uiState.update { it.copy(showResetConfirm = false) }
//        }
//    }
//
//    // ── Private helpers ──────────────────────────────────────────────────────
//
//    private data class SettingsPartialA(
//        val name: String,
//        val accountability: String,
//        val checkIn: String,
//        val streakMiss: String,
//        val totalActivities: Int
//    )
//
//    private data class SettingsPartialB(
//        val sound: Boolean,
//        val vibration: Boolean,
//        val keepScreenOn: Boolean,
//        val dailyReminder: Boolean,
//        val reminderTime: String
//    )
//}