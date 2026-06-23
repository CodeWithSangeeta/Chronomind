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





//package com.sangeeta.chronomind.ui.home
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.sangeeta.chronomind.repository.ActivityRepository
//import com.sangeeta.chronomind.repository.OnboardingRepository
//import com.sangeeta.chronomind.ui.mapper.toUiModel
//import com.sangeeta.chronomind.ui.model.ActivityUiModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//import javax.inject.Inject
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val activityRepo: ActivityRepository,
//    private val onboardingRepo: OnboardingRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(HomeUiState())
//    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
//
//    init {
//        observeActivities()
//        observeUserName()
//    }
//
//    // ── Observe real data ───────────────────────────────────────────────
//
//    private fun observeActivities() {
//        combine(
//            activityRepo.observeAll(),
//            activityRepo.observeRunning()
//        ) { all, running ->
//            val uiModels = all.map { it.toUiModel() }
//            val runningUi = running?.toUiModel()
//
//            // Recent = sorted by last used date desc, take 5
//            val recent = uiModels
//                .filter { it.lastActiveDate != "Never" }
//                .sortedByDescending { lastUsedWeight(it.lastActiveDate) }
//                .take(5)
//                .ifEmpty { uiModels.take(5) }
//
//            _uiState.update { current ->
//                current.copy(
//                    recentActivities = recent,
//                    runningActivity  = runningUi,
//                    selectedActivity = runningUi ?: recent.firstOrNull()
//                )
//            }
//        }.launchIn(viewModelScope)
//    }
//
//    private fun observeUserName() {
//        onboardingRepo.userName
//            .onEach { name ->
//                _uiState.update { it.copy(
//                    appName  = "ChronoMind",
//                    subtitle = if (name.isNotBlank()) "Welcome back, $name 👋" else "Your focus companion"
//                )}
//            }
//            .launchIn(viewModelScope)
//    }
//
//    // ── Timer actions ───────────────────────────────────────────────────
//
//    fun startFocus() {
//        val target = _uiState.value.selectedActivity ?: return
//        viewModelScope.launch {
//            activityRepo.stopAll()
//            activityRepo.updateTimer(target.id, target.elapsedSeconds, running = true)
//        }
//    }
//
//    fun pauseSession() {
//        val running = _uiState.value.runningActivity ?: return
//        viewModelScope.launch {
//            activityRepo.updateTimer(running.id, running.elapsedSeconds, running = false)
//        }
//    }
//
//    fun finishSession() {
//        val running = _uiState.value.runningActivity ?: return
//        viewModelScope.launch {
//            activityRepo.updateTimer(running.id, 0L, running = false)
//            activityRepo.updateStreak(
//                id     = running.id,
//                streak = running.streakDays + 1,
//                date   = LocalDate.now().toString()
//            )
//        }
//    }
//
//    fun switchActivity() {
//        // Pause current, let user pick from AllActivities
//        pauseSession()
//    }
//
//    // ── Helpers ─────────────────────────────────────────────────────────
//
//    private fun lastUsedWeight(label: String): Int = when (label.lowercase()) {
//        "today"     -> 4
//        "yesterday" -> 3
//        else        -> if (label.contains("days ago")) {
//            val n = label.filter { it.isDigit() }.toIntOrNull() ?: 99
//            maxOf(0, 99 - n)
//        } else 0
//    }
//}



package com.sangeeta.chronomind.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRepo: ActivityRepository,
    private val onboardingRepo: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        observeHomeData()
    }

    private fun observeHomeData() {
        viewModelScope.launch {
            combine(
                onboardingRepo.userName,
                activityRepo.observeAll(),
                activityRepo.observeRunning()
            ) { userName, activities, running ->
                Triple(userName, activities, running)
            }.collect { (userName, activities, running) ->

                val mapped = activities.map { it.toUiModel() }
                val runningUi = running?.toUiModel()
                val selected = runningUi ?: mapped.firstOrNull()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        appName = if (userName.isBlank()) "ChronoMind" else "$userName's ChronoMind",
                        subtitle = if (runningUi != null) "Stay locked in" else "Focus • Track • Grow",
                        selectedActivity = selected,
                        runningActivity = runningUi,
                        recentActivities = mapped.take(8)
                    )
                }

                if (runningUi != null) startTicker() else stopTicker()
            }
        }
    }

    fun startFocus() {
        val selected = _uiState.value.selectedActivity ?: return
        viewModelScope.launch {
            val entity = activityRepo.observeById(selected.id)
            var latest: ActivityEntity? = null
            entity.collect {
                latest = it
                return@collect
            }
            latest?.let { activityRepo.startActivity(it) }
        }
    }

    fun pauseSession() {
        val running = _uiState.value.runningActivity ?: return
        viewModelScope.launch {
            val entity = findEntity(running.id) ?: return@launch
            activityRepo.pauseActivity(entity.copy(elapsedSeconds = running.elapsedSeconds))
        }
    }

    fun finishSession() {
        val running = _uiState.value.runningActivity ?: return
        viewModelScope.launch {
            val entity = findEntity(running.id) ?: return@launch
            activityRepo.finishActivity(
                activity = entity.copy(elapsedSeconds = running.elapsedSeconds),
                todayLabel = todayLabel()
            )
        }
    }

    fun switchActivity() {
        viewModelScope.launch {
            val current = _uiState.value.runningActivity
            val next = _uiState.value.recentActivities.firstOrNull { it.id != current?.id } ?: return@launch
            val entity = findEntity(next.id) ?: return@launch
            activityRepo.switchToActivity(entity)
        }
    }

    private fun startTicker() {
        if (timerJob?.isActive == true) return

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val running = _uiState.value.runningActivity ?: break
                val updatedElapsed = running.elapsedSeconds + 1

                _uiState.update { state ->
                    val updatedRunning = running.copy(elapsedSeconds = updatedElapsed)
                    state.copy(
                        runningActivity = updatedRunning,
                        selectedActivity = updatedRunning,
                        recentActivities = state.recentActivities.map {
                            if (it.id == updatedRunning.id) updatedRunning else it
                        }
                    )
                }

                activityRepo.updateTimer(
                    id = running.id,
                    elapsed = updatedElapsed,
                    running = true
                )
            }
        }
    }

    private fun stopTicker() {
        timerJob?.cancel()
        timerJob = null
    }

    private suspend fun findEntity(id: Int): ActivityEntity? {
        var found: ActivityEntity? = null
        activityRepo.observeById(id).collect {
            found = it
            return@collect
        }
        return found
    }

    private fun ActivityEntity.toUiModel(): ActivityUiModel {
        return ActivityUiModel(
            id = id,
            name = name,
            icon = icon,
            colorHex = colorHex,
            elapsedSeconds = elapsedSeconds,
            targetSeconds = targetMinutes * 60L,
            isRunning = isRunning,
            streakDays = streakDays,
            lastActiveDate = lastActiveDate,
            continueOnMiss = continueStreakOnMiss
        )
    }

    private fun todayLabel(): String = "Today"
}
