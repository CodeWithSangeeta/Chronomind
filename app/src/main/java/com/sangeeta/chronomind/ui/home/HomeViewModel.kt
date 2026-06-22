//package com.sangeeta.chronomind.ui.home
//
//
//import androidx.lifecycle.ViewModel
//import com.sangeeta.chronomind.repository.ActivityRepository
//import com.sangeeta.chronomind.repository.OnboardingRepository
//import com.sangeeta.chronomind.ui.model.ActivityUiModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val activityRepo: ActivityRepository,
//    private val onboardingRepo: OnboardingRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(mockHomeState())
//    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
//
//    fun startFocus() {
//        // UI-only phase: no-op for now
//    }
//
//    fun pauseSession() {
//        // UI-only phase: no-op for now
//    }
//
//    fun finishSession() {
//        // UI-only phase: no-op for now
//    }
//
//    fun switchActivity() {
//        // UI-only phase: no-op for now
//    }
//
//    private fun mockHomeState(): HomeUiState {
//        val creativeProjects = ActivityUiModel(
//            id = 1,
//            name = "Creative Projects",
//            icon = "🎨",
//            colorHex = "#FFCC00",
//            elapsedSeconds = 0L,
//            targetSeconds = 60 * 60L,
//            isRunning = false,
//            streakDays = 4,
//            lastActiveDate = "Today",
//            continueOnMiss = true
//        )
//
//        val recent = listOf(
//            creativeProjects.copy(
//                id = 1,
//                elapsedSeconds = 48 * 60L,
//                targetSeconds = 60 * 60L,
//                lastActiveDate = "Today"
//            ),
//            ActivityUiModel(
//                id = 2,
//                name = "Study & Learning",
//                icon = "📘",
//                colorHex = "#4DA3FF",
//                elapsedSeconds = 27 * 60L,
//                targetSeconds = 45 * 60L,
//                isRunning = false,
//                streakDays = 7,
//                lastActiveDate = "Today",
//                continueOnMiss = true
//            ),
//            ActivityUiModel(
//                id = 3,
//                name = "Coding Practice",
//                icon = "💻",
//                colorHex = "#52C26D",
//                elapsedSeconds = 90 * 60L,
//                targetSeconds = 90 * 60L,
//                isRunning = false,
//                streakDays = 12,
//                lastActiveDate = "Yesterday",
//                continueOnMiss = true
//            ),
//            ActivityUiModel(
//                id = 4,
//                name = "Workout",
//                icon = "🏋️",
//                colorHex = "#8B5CF6",
//                elapsedSeconds = 24 * 60L,
//                targetSeconds = 60 * 60L,
//                isRunning = false,
//                streakDays = 5,
//                lastActiveDate = "Yesterday",
//                continueOnMiss = true
//            ),
//            ActivityUiModel(
//                id = 5,
//                name = "Meditation",
//                icon = "🧘",
//                colorHex = "#F97316",
//                elapsedSeconds = 20 * 60L,
//                targetSeconds = 20 * 60L,
//                isRunning = false,
//                streakDays = 9,
//                lastActiveDate = "2 days ago",
//                continueOnMiss = true
//            )
//        )
//
//        return HomeUiState(
//            isLoading = false,
//            selectedActivity = creativeProjects,
//            runningActivity = null,
//            recentActivities = recent
//        )
//    }
//}





package com.sangeeta.chronomind.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import com.sangeeta.chronomind.ui.mapper.toUiModel
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRepo: ActivityRepository,
    private val onboardingRepo: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeActivities()
        observeUserName()
    }

    // ── Observe real data ───────────────────────────────────────────────

    private fun observeActivities() {
        combine(
            activityRepo.observeAll(),
            activityRepo.observeRunning()
        ) { all, running ->
            val uiModels = all.map { it.toUiModel() }
            val runningUi = running?.toUiModel()

            // Recent = sorted by last used date desc, take 5
            val recent = uiModels
                .filter { it.lastActiveDate != "Never" }
                .sortedByDescending { lastUsedWeight(it.lastActiveDate) }
                .take(5)
                .ifEmpty { uiModels.take(5) }

            _uiState.update { current ->
                current.copy(
                    recentActivities = recent,
                    runningActivity  = runningUi,
                    selectedActivity = runningUi ?: recent.firstOrNull()
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun observeUserName() {
        onboardingRepo.userName
            .onEach { name ->
                _uiState.update { it.copy(
                    appName  = "ChronoMind",
                    subtitle = if (name.isNotBlank()) "Welcome back, $name 👋" else "Your focus companion"
                )}
            }
            .launchIn(viewModelScope)
    }

    // ── Timer actions ───────────────────────────────────────────────────

    fun startFocus() {
        val target = _uiState.value.selectedActivity ?: return
        viewModelScope.launch {
            activityRepo.stopAll()
            activityRepo.updateTimer(target.id, target.elapsedSeconds, running = true)
        }
    }

    fun pauseSession() {
        val running = _uiState.value.runningActivity ?: return
        viewModelScope.launch {
            activityRepo.updateTimer(running.id, running.elapsedSeconds, running = false)
        }
    }

    fun finishSession() {
        val running = _uiState.value.runningActivity ?: return
        viewModelScope.launch {
            activityRepo.updateTimer(running.id, 0L, running = false)
            activityRepo.updateStreak(
                id     = running.id,
                streak = running.streakDays + 1,
                date   = LocalDate.now().toString()
            )
        }
    }

    fun switchActivity() {
        // Pause current, let user pick from AllActivities
        pauseSession()
    }

    // ── Helpers ─────────────────────────────────────────────────────────

    private fun lastUsedWeight(label: String): Int = when (label.lowercase()) {
        "today"     -> 4
        "yesterday" -> 3
        else        -> if (label.contains("days ago")) {
            val n = label.filter { it.isDigit() }.toIntOrNull() ?: 99
            maxOf(0, 99 - n)
        } else 0
    }
}
