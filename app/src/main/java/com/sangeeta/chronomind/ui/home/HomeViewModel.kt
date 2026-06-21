package com.sangeeta.chronomind.ui.home


import androidx.lifecycle.ViewModel
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRepo: ActivityRepository,
    private val onboardingRepo: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(mockHomeState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun startFocus() {
        // UI-only phase: no-op for now
    }

    fun pauseSession() {
        // UI-only phase: no-op for now
    }

    fun finishSession() {
        // UI-only phase: no-op for now
    }

    fun switchActivity() {
        // UI-only phase: no-op for now
    }

    private fun mockHomeState(): HomeUiState {
        val creativeProjects = ActivityUiModel(
            id = 1,
            name = "Creative Projects",
            icon = "🎨",
            colorHex = "#FFCC00",
            elapsedSeconds = 0L,
            targetSeconds = 60 * 60L,
            isRunning = false,
            streakDays = 4,
            lastActiveDate = "Today",
            continueOnMiss = true
        )

        val recent = listOf(
            creativeProjects.copy(
                id = 1,
                elapsedSeconds = 48 * 60L,
                targetSeconds = 60 * 60L,
                lastActiveDate = "Today"
            ),
            ActivityUiModel(
                id = 2,
                name = "Study & Learning",
                icon = "📘",
                colorHex = "#4DA3FF",
                elapsedSeconds = 27 * 60L,
                targetSeconds = 45 * 60L,
                isRunning = false,
                streakDays = 7,
                lastActiveDate = "Today",
                continueOnMiss = true
            ),
            ActivityUiModel(
                id = 3,
                name = "Coding Practice",
                icon = "💻",
                colorHex = "#52C26D",
                elapsedSeconds = 90 * 60L,
                targetSeconds = 90 * 60L,
                isRunning = false,
                streakDays = 12,
                lastActiveDate = "Yesterday",
                continueOnMiss = true
            ),
            ActivityUiModel(
                id = 4,
                name = "Workout",
                icon = "🏋️",
                colorHex = "#8B5CF6",
                elapsedSeconds = 24 * 60L,
                targetSeconds = 60 * 60L,
                isRunning = false,
                streakDays = 5,
                lastActiveDate = "Yesterday",
                continueOnMiss = true
            ),
            ActivityUiModel(
                id = 5,
                name = "Meditation",
                icon = "🧘",
                colorHex = "#F97316",
                elapsedSeconds = 20 * 60L,
                targetSeconds = 20 * 60L,
                isRunning = false,
                streakDays = 9,
                lastActiveDate = "2 days ago",
                continueOnMiss = true
            )
        )

        return HomeUiState(
            isLoading = false,
            selectedActivity = creativeProjects,
            runningActivity = null,
            recentActivities = recent
        )
    }
}