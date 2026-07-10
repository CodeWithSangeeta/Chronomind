package com.sangeeta.chronomind.ui.settings

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.reminder.ReminderScheduler
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import com.sangeeta.chronomind.repository.SettingsRepository
import com.sangeeta.chronomind.ui.create_activity.CompletionStyle
import com.sangeeta.chronomind.ui.create_activity.StreakBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

sealed interface SettingsEvent {
    data object RequestNotificationPermission : SettingsEvent
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val activityRepository: ActivityRepository,
    private val onboardingRepository: OnboardingRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val localUiState = MutableStateFlow(SettingsUiState())

    val uiState: StateFlow<SettingsUiState> = combine(
        localUiState,
        settingsRepository.notificationsEnabled,
        settingsRepository.isDailyReminderEnabled,
        settingsRepository.reminderTime,
        combine(
            settingsRepository.defaultCompletionStyle,
            settingsRepository.defaultStreakBehavior
        ) { completion, streak -> completion to streak }
    ) { local, notifications, reminderEnabled, reminderTime, compStreak ->
        val (completionStyle, streakBehavior) = compStreak
        val parsed = parseReminderTime(reminderTime)
        local.copy(
            notificationsEnabled = notifications,
            isDailyReminderEnabled = reminderEnabled,
            reminderTime = reminderTime,
            reminderHour = parsed.hour,
            reminderMinute = parsed.minute,
            reminderAmPm = parsed.amPm,
            defaultCompletionStyle = completionStyle,
            defaultStreakOnMiss = streakBehavior,
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
            if (enabled) {
                _events.send(SettingsEvent.RequestNotificationPermission)
            } else {
                settingsRepository.setNotificationsEnabled(false)
            }
        }
    }

    fun onNotificationPermissionResult(granted: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(granted)
        }
    }

    fun onDailyReminderToggle(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDailyReminderEnabled(enabled)
            val time = uiState.value.reminderTime
            if (enabled) {
                ReminderScheduler.schedule(context, time)
            } else {
                ReminderScheduler.cancel(context)
            }
        }
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

    fun setDefaultCompletionStyle(value: CompletionStyle) {
        viewModelScope.launch {
            settingsRepository.setDefaultCompletionStyle(value)
        }
    }

    fun setDefaultStreakOnMiss(value: StreakBehavior) {
        viewModelScope.launch {
            settingsRepository.setDefaultStreakBehavior(value)
        }
    }

    fun showClearDataConfirm(show: Boolean) {
        localUiState.update { it.copy(showClearDataConfirm = show) }
    }

    fun showResetConfirm(show: Boolean) {
        localUiState.update { it.copy(showResetConfirm = show) }
    }

    fun clearAllData() = viewModelScope.launch {
        activityRepository.clearAll()
        localUiState.update { it.copy(showClearDataConfirm = false) }
    }

    fun resetOnboarding(onDone: () -> Unit) = viewModelScope.launch {
        onboardingRepository.resetOnboarding()
        localUiState.update { it.copy(showResetConfirm = false) }
        onDone()
    }

    private fun saveReminderTime(hour: Int, minute: Int, amPm: String) {
        val formatted = String.format(Locale.US, "%02d:%02d %s", hour, minute, amPm)
        viewModelScope.launch {
            settingsRepository.setReminderTime(formatted)
            if (uiState.value.isDailyReminderEnabled) {
                ReminderScheduler.schedule(context, formatted)
            }
        }
    }

    private fun parseReminderTime(value: String): ReminderParts {
        return try {
            val parts = value.trim().split(" ")
            val timePart = parts.getOrNull(0) ?: "07:00"
            val amPmPart = parts.getOrNull(1) ?: "AM"
            val timePieces = timePart.split(":")
            val hour = timePieces.getOrNull(0)?.toIntOrNull()?.coerceIn(1, 12) ?: 7
            val minute = timePieces.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
            ReminderParts(hour, minute, if (amPmPart.uppercase() == "PM") "PM" else "AM")
        } catch (_: Exception) {
            ReminderParts(7, 0, "AM")
        }
    }

    private data class ReminderParts(val hour: Int, val minute: Int, val amPm: String)

    private fun defaultHelpItems(): List<SettingsRowUiModel> = listOf(
        SettingsRowUiModel(
            id = "faq",
            title = "FAQ",
            subtitle = "Common questions and answers",
            icon = Icons.Rounded.Help
        ),
            SettingsRowUiModel(
                id = "support_portal",
                title = "Report an issue / Feedback",
                subtitle = "Bugs, feature requests, and support",
                icon = Icons.Rounded.Feedback,
                isExternal = true
            )
    )

    private fun defaultTrustItems(): List<SettingsRowUiModel> = listOf(
        SettingsRowUiModel(
            id = "privacy",
            title = "Privacy policy",
            subtitle = "How your data is handled",
            icon = Icons.Rounded.Policy,
            value = context.getString(R.string.url_privacy_policy),
            isExternal = true
        ),
        SettingsRowUiModel(
            id = "terms",
            title = "Terms of service",
            subtitle = "Rules and usage guidelines",
            icon = Icons.Rounded.Gavel,
            value = context.getString(R.string.url_terms_of_service),
            isExternal = true
        ),
        SettingsRowUiModel(
            id = "rateapp",
            title = "Rate app",
            subtitle = "Support ChronoMind on Play Store",
            icon = Icons.Rounded.Star,
            isExternal = true
        )
    )

    private fun defaultAboutItems(): List<SettingsRowUiModel> = listOf(
        SettingsRowUiModel(
            id = "version",
            title = "App version",
            subtitle = "Current installed build",
            icon = Icons.Rounded.Info,
            value = "v${com.sangeeta.chronomind.BuildConfig.VERSION_NAME}",
            isValueOnly = true
        ),
        SettingsRowUiModel(
            id = "developer",
            title = "Developer",
            subtitle = "Built by Sangeeta Yadav",
            icon = Icons.Rounded.Code,
            isExternal = true
        ),
        SettingsRowUiModel(
            id = "licenses",
            title = "Open source licenses",
            subtitle = "Third-party libraries and licenses",
            icon = Icons.Rounded.Article
        )
    )
}
