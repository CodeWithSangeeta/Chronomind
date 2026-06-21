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
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.sangeeta.chronomind.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            generalItems = listOf(
                SettingsRowUiModel(
                    id = "notifications",
                    title = "Notifications & Reminders",
                    subtitle = "Manage focus reminders and alerts",
                    icon = Icons.Rounded.Notifications
                ),
                SettingsRowUiModel(
                    id = "app_reminders",
                    title = "App-wide Reminders",
                    subtitle = "Set default reminder times and behavior",
                    icon = Icons.Rounded.Schedule
                ),
                SettingsRowUiModel(
                    id = "general_preferences",
                    title = "General Preferences",
                    subtitle = "App behavior, haptics, and more",
                    icon = Icons.Rounded.Tune
                )
            ),
            focusDefaultsItems = listOf(
                SettingsRowUiModel(
                    id = "missed_streak_behavior",
                    title = "Missed Streak Behavior",
                    subtitle = "Choose what happens when you miss a day",
                    icon = Icons.Rounded.Whatshot,
                    value = "Continue Streak"
                ),
                SettingsRowUiModel(
                    id = "default_check_in_style",
                    title = "Default Check-in Style",
                    subtitle = "How you check in on activities",
                    icon = Icons.Rounded.CheckCircle,
                    value = "Manual"
                ),
                SettingsRowUiModel(
                    id = "default_completion_behavior",
                    title = "Default Completion Behavior",
                    subtitle = "How activities are marked complete",
                    icon = Icons.Rounded.Flag,
                    value = "Manual Complete"
                )
            ),
            widgetItems = listOf(
                SettingsRowUiModel(
                    id = "open_widget_setup",
                    title = "Open Widget Setup",
                    subtitle = "Choose activities and widget sizes",
                    icon = Icons.Rounded.Widgets
                ),
                SettingsRowUiModel(
                    id = "widget_help",
                    title = "Widget Help",
                    subtitle = "How to add and use widgets",
                    icon = Icons.Rounded.Help
                )
            ),
            helpItems = listOf(
                SettingsRowUiModel(
                    id = "faq",
                    title = "FAQ",
                    subtitle = "Common questions and answers",
                    icon = Icons.Rounded.Help
                ),
                SettingsRowUiModel(
                    id = "send_feedback",
                    title = "Send Feedback",
                    subtitle = "Share your thoughts and suggestions",
                    icon = Icons.Rounded.Feedback
                ),
                SettingsRowUiModel(
                    id = "report_issue",
                    title = "Report an Issue",
                    subtitle = "Found a bug or something not working?",
                    icon = Icons.Rounded.Info
                )
            ),
            trustItems = listOf(
                SettingsRowUiModel(
                    id = "privacy_policy",
                    title = "Privacy Policy",
                    subtitle = "How we handle your data",
                    icon = Icons.Rounded.Policy,
                    isExternal = true
                ),
                SettingsRowUiModel(
                    id = "rate_app",
                    title = "Rate ChronoMind",
                    subtitle = "If you enjoy using the app",
                    icon = Icons.Rounded.Star,
                    isExternal = true
                ),
                SettingsRowUiModel(
                    id = "share_app",
                    title = "Share ChronoMind",
                    subtitle = "Share the app with friends",
                    icon = Icons.Rounded.Share,
                    isExternal = true
                ),
                SettingsRowUiModel(
                    id = "terms",
                    title = "Terms of Service",
                    subtitle = "Rules and guidelines",
                    icon = Icons.Rounded.Gavel,
                    isExternal = true
                )
            ),
            aboutItems = listOf(
                SettingsRowUiModel(
                    id = "about_developer",
                    title = "About Developer",
                    subtitle = "Learn more about the developer",
                    icon = Icons.Rounded.Person,
                    isExternal = true
                ),
                SettingsRowUiModel(
                    id = "app_version",
                    title = "App Version",
                    subtitle = "You’re using the latest version",
                    icon = Icons.Rounded.Info,
                    value = "v1.2.0 (120)",
                    isValueOnly = true
                ),
                SettingsRowUiModel(
                    id = "licenses",
                    title = "Open Source Licenses",
                    subtitle = "Third-party libraries and licenses",
                    icon = Icons.Rounded.Code
                )
            )
        )
    )

    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
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