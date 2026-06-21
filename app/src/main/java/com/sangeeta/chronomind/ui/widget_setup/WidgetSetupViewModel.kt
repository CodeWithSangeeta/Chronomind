package com.sangeeta.chronomind.ui.widget_setup

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class WidgetSetupViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        WidgetSetupUiState(
            isLoading = false,
            activities = mockActivities(),
            selectedActivityId = 1,
            selectedSize = WidgetSizeOption.MEDIUM
        )
    )
    val uiState: StateFlow<WidgetSetupUiState> = _uiState.asStateFlow()

    fun selectActivity(activityId: Int) {
        _uiState.update { it.copy(selectedActivityId = activityId) }
    }

    fun selectWidgetSize(size: WidgetSizeOption) {
        _uiState.update { it.copy(selectedSize = size) }
    }

    fun showHelp(show: Boolean) {
        _uiState.update { it.copy(showHelpDialog = show) }
    }

    fun setWidget() {
        // UI-only phase. Real widget pin/setup flow will be wired later.
    }

    private fun mockActivities(): List<WidgetActivityUiModel> {
        return listOf(
            WidgetActivityUiModel(
                id = 1,
                name = "Creative Projects",
                iconEmoji = "🎨",
                accentColor = Color(0xFFF6C445),
                targetText = "1h 30m",
                streakDays = 7,
                progressPercent = 80,
                progressText = "1h 12m / 1h 30m",
                isRunning = false
            ),
            WidgetActivityUiModel(
                id = 2,
                name = "Study & Learning",
                iconEmoji = "📘",
                accentColor = Color(0xFF2F7DE1),
                targetText = "2h 00m",
                streakDays = 12,
                progressPercent = 62,
                progressText = "1h 15m / 2h 00m",
                isRunning = true
            ),
            WidgetActivityUiModel(
                id = 3,
                name = "Workout",
                iconEmoji = "🏋️",
                accentColor = Color(0xFF8D49D7),
                targetText = "45m",
                streakDays = 5,
                progressPercent = 54,
                progressText = "24m / 45m",
                isRunning = false
            )
        )
    }
}