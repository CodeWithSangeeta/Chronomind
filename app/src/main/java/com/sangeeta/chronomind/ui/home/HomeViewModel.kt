import com.sangeeta.chronomind.ui.model.ActivityUiModel

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



//
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.sangeeta.chronomind.local.db.entity.ActivityEntity
//import com.sangeeta.chronomind.repository.ActivityRepository
//import com.sangeeta.chronomind.repository.OnboardingRepository
//import com.sangeeta.chronomind.service.TimerForegroundService
//import com.sangeeta.chronomind.ui.home.HomeUiState
//import com.sangeeta.chronomind.ui.mapper.toUiModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import dagger.hilt.android.qualifiers.ApplicationContext
//import javax.inject.Inject
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val activityRepo: ActivityRepository,
//    private val onboardingRepo: OnboardingRepository,
//    @ApplicationContext private val context: Context
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
//    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
//
//    private var timerJob: Job? = null
//
//    init {
//        observeHomeData()
//    }
//
//    private fun observeHomeData() {
//        viewModelScope.launch {
//            combine(
//                onboardingRepo.userName,
//                activityRepo.observeAll(),
//                activityRepo.observeRunning()
//            ) { userName, activities, running ->
//                Triple(userName, activities, running)
//            }.collect { (userName, activities, running) ->
//
//                val mapped = activities.map { it.toUiModel() }
//                val runningUi = running?.toUiModel()
//                val selected = runningUi ?: mapped.firstOrNull()
//
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        appName = if (userName.isBlank()) "ChronoMind" else "$userName's ChronoMind",
//                        subtitle = if (runningUi != null) "Stay locked in" else "Focus • Track • Grow",
//                        selectedActivity = selected,
//                        runningActivity = runningUi,
//                        recentActivities = mapped.take(8)
//                    )
//                }
//
//             //   if (runningUi != null) startTicker() else stopTicker()
//            }
//        }
//    }
//
//
//
//
//    fun startFocus() {
//        val selected = uiState.value.selectedActivity ?: return
//        viewModelScope.launch {
//            activityRepo.stopAll()
//            activityRepo.updateTimer(selected.id, selected.elapsedSeconds, true)
//            // Start the foreground service — it takes over ticking
//            context.startForegroundService(TimerForegroundService.startIntent(context))
//        }
//    }
//
//    fun pauseSession() {
//        val running = uiState.value.runningActivity ?: return
//        viewModelScope.launch {
//            activityRepo.updateTimer(running.id, running.elapsedSeconds, false)
//            context.startService(TimerForegroundService.pauseIntent(context))
//        }
//    }
//
//
//    fun finishSession() {
//        val running = uiState.value.runningActivity ?: return
//        viewModelScope.launch {
//            activityRepo.updateTimer(running.id, 0L, false)
//            activityRepo.updateStreak(
//                id = running.id,
//                streak = running.streakDays + 1,
//                date = LocalDate.now().toString()
//            )
//            context.startService(TimerForegroundService.stopIntent(context))
//        }
//    }
//
//    fun switchActivity() {
//        viewModelScope.launch {
//            val current = uiState.value.runningActivity
//            val next = uiState.value.recentActivities.firstOrNull { it.id != current?.id } ?: return@launch
//            activityRepo.stopAll()
//            activityRepo.updateTimer(next.id, next.elapsedSeconds, true)
//            // Service keeps running, it'll auto-pick the new running activity next tick
//        }
//    }
//
//
//
//
//
//    private suspend fun findEntity(id: Int): ActivityEntity? {
//        var found: ActivityEntity? = null
//        activityRepo.observeById(id).collect {
//            found = it
//            return@collect
//        }
//        return found
//    }
//
//    private fun ActivityEntity.toUiModel(): ActivityUiModel {
//        return ActivityUiModel(
//            id = id,
//            name = name,
//            icon = icon,
//            colorHex = colorHex,
//            elapsedSeconds = elapsedSeconds,
//            targetSeconds = targetMinutes * 60L,
//            isRunning = isRunning,
//            streakDays = streakDays,
//            lastActiveDate = lastActiveDate,
//            continueOnMiss = continueStreakOnMiss
//        )
//    }
//
//    private fun todayLabel(): String = "Today"
//}




//package com.sangeeta.chronomind.ui.home
//
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.sangeeta.chronomind.repository.ActivityRepository
//import com.sangeeta.chronomind.repository.OnboardingRepository
//import com.sangeeta.chronomind.service.TimerForegroundService
//import com.sangeeta.chronomind.ui.model.ActivityUiModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import dagger.hilt.android.qualifiers.ApplicationContext
//import javax.inject.Inject
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val activityRepo: ActivityRepository,
//    private val onboardingRepo: OnboardingRepository,
//    @ApplicationContext private val context: Context
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
//    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
//
//    init {
//        observeHomeData()
//    }
//
//    private fun observeHomeData() {
//        viewModelScope.launch {
//            combine(
//                onboardingRepo.userName,
//                activityRepo.observeAll(),
//                activityRepo.observeRunning(),
//                activityRepo.selectedActivityId
//            ) { userName, activities, running, selectedId ->
//                HomeCombinedState(
//                    userName = userName,
//                    activities = activities.map { it.toUiModel() },
//                    runningActivity = running?.toUiModel(),
//                    selectedId = selectedId
//                )
//            }.collect { combined ->
//                val recent = combined.activities
//                    .sortedByDescending { lastUsedWeight(it.lastActiveDate) }
//                    .take(8)
//
//                val selectedUi = combined.activities.firstOrNull { it.id == combined.selectedId }
//                val heroSelected = combined.runningActivity
//                    ?: selectedUi
//                    ?: recent.firstOrNull()
//                    ?: combined.activities.firstOrNull()
//
//                if (combined.selectedId == null && heroSelected != null) {
//                    activityRepo.selectActivity(heroSelected.id)
//                }
//
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        appName = if (combined.userName.isBlank()) {
//                            "ChronoMind"
//                        } else {
//                            "${combined.userName}'s ChronoMind"
//                        },
//                        subtitle = if (combined.runningActivity != null) {
//                            "Stay locked in"
//                        } else {
//                            "Focus • Track • Grow"
//                        },
//                        selectedActivity = heroSelected,
//                        runningActivity = combined.runningActivity,
//                        recentActivities = recent
//                    )
//                }
//            }
//        }
//    }
//
//    fun onRecentActivitySelected(activityId: Int) {
//        viewModelScope.launch {
//            val running = uiState.value.runningActivity
//            if (running != null) {
//                activityRepo.updateTimer(
//                    id = running.id,
//                    elapsed = running.elapsedSeconds,
//                    running = false
//                )
//                context.startService(TimerForegroundService.pauseIntent(context))
//            }
//            activityRepo.selectActivity(activityId)
//        }
//    }
//
//    fun startFocus() {
//        val selected = uiState.value.selectedActivity ?: return
//        viewModelScope.launch {
//            activityRepo.stopAll()
//            activityRepo.updateTimer(
//                id = selected.id,
//                elapsed = selected.elapsedSeconds,
//                running = true
//            )
//            activityRepo.selectActivity(selected.id)
//            context.startForegroundService(TimerForegroundService.startIntent(context))
//        }
//    }
//
//    fun pauseSession() {
//        val running = uiState.value.runningActivity ?: return
//        viewModelScope.launch {
//            activityRepo.updateTimer(
//                id = running.id,
//                elapsed = running.elapsedSeconds,
//                running = false
//            )
//            activityRepo.selectActivity(running.id)
//            context.startService(TimerForegroundService.pauseIntent(context))
//        }
//    }
//
//    fun finishSession() {
//        val running = uiState.value.runningActivity ?: return
//        viewModelScope.launch {
//            activityRepo.logSession(running, isCompleted = true)
//            activityRepo.updateTimer(
//                id = running.id,
//                elapsed = 0L,
//                running = false
//            )
//            activityRepo.updateStreak(
//                id = running.id,
//                streak = running.streakDays + 1,
//                date = LocalDate.now().toString()
//            )
//            activityRepo.selectActivity(running.id)
//            context.startService(TimerForegroundService.stopIntent(context))
//        }
//    }
//
//    private fun lastUsedWeight(label: String): Int {
//        return when (label.lowercase()) {
//            "today" -> 100
//            "yesterday" -> 90
//            else -> {
//                if (label.contains("days ago")) {
//                    val n = label.filter { it.isDigit() }.toIntOrNull() ?: 99
//                    maxOf(0, 80 - n)
//                } else {
//                    0
//                }
//            }
//        }
//    }
//
//    private data class HomeCombinedState(
//        val userName: String,
//        val activities: List<ActivityUiModel>,
//        val runningActivity: ActivityUiModel?,
//        val selectedId: Int?
//    )
//}
//
//private fun com.sangeeta.chronomind.local.db.entity.ActivityEntity.toUiModel(): ActivityUiModel {
//    return ActivityUiModel(
//        id = id,
//        name = name,
//        icon = icon,
//        colorHex = colorHex,
//        elapsedSeconds = elapsedSeconds,
//        targetSeconds = targetMinutes * 60L,
//        isRunning = isRunning,
//        streakDays = streakDays,
//        lastActiveDate = lastActiveDate,
//        continueOnMiss = continueStreakOnMiss
//    )
//}




//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val activityRepo: ActivityRepository,
//    private val onboardingRepo: OnboardingRepository,
//    @ApplicationContext private val context: Context
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
//    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
//
//    init {
//        observeHomeData()
//    }
//
//    private fun observeHomeData() {
//        viewModelScope.launch {
//            combine(
//                onboardingRepo.userName,
//                activityRepo.observeAll(),
//                activityRepo.observeRunning(),
//                activityRepo.selectedActivityId
//            ) { userName, activities, running, selectedId ->
//                HomeCombinedState(
//                    userName = userName,
//                    activities = activities.map { it.toUiModel() },
//                    runningActivity = running?.toUiModel(),
//                    selectedId = selectedId
//                )
//            }.collect { combined ->
//
//                val recent = buildRecentActivities(combined.activities)
//
//                val selectedUi = combined.activities.firstOrNull { it.id == combined.selectedId }
//                val heroSelected = combined.runningActivity
//                    ?: selectedUi
//                    ?: recent.firstOrNull()
//                    ?: combined.activities.firstOrNull()
//
//                if (combined.selectedId == null && heroSelected != null) {
//                    activityRepo.selectActivity(heroSelected.id)
//                }
//
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        appName = if (combined.userName.isBlank()) {
//                            "ChronoMind"
//                        } else {
//                            "${combined.userName}'s ChronoMind"
//                        },
//                        subtitle = if (combined.runningActivity != null) {
//                            "Stay locked in"
//                        } else {
//                            "Focus • Track • Grow"
//                        },
//                        selectedActivity = heroSelected,
//                        runningActivity = combined.runningActivity,
//                        recentActivities = recent
//                    )
//                }
//            }
//        }
//    }
//
//    fun onRecentActivitySelected(activityId: Int) {
//        viewModelScope.launch {
//            val running = uiState.value.runningActivity
//            if (running != null) {
//                activityRepo.updateTimer(
//                    id = running.id,
//                    elapsed = running.elapsedSeconds,
//                    running = false
//                )
//                context.startService(TimerForegroundService.pauseIntent(context))
//            }
//            activityRepo.selectActivity(activityId)
//        }
//    }
//
//    fun startFocus() {
//        val selected = uiState.value.selectedActivity ?: return
//        viewModelScope.launch {
//            activityRepo.stopAll()
//            activityRepo.updateTimer(
//                id = selected.id,
//                elapsed = selected.elapsedSeconds,
//                running = true
//            )
//            activityRepo.selectActivity(selected.id)
//            context.startForegroundService(TimerForegroundService.startIntent(context))
//        }
//    }
//
//    fun pauseSession() {
//        val running = uiState.value.runningActivity ?: return
//        viewModelScope.launch {
//            activityRepo.updateTimer(
//                id = running.id,
//                elapsed = running.elapsedSeconds,
//                running = false
//            )
//            activityRepo.selectActivity(running.id)
//            context.startService(TimerForegroundService.pauseIntent(context))
//        }
//    }
//
//    fun finishSession() {
//        val running = uiState.value.runningActivity ?: return
//        viewModelScope.launch {
//            activityRepo.logSession(running, isCompleted = true)
//            activityRepo.updateTimer(
//                id = running.id,
//                elapsed = 0L,
//                running = false
//            )
//            activityRepo.updateStreak(
//                id = running.id,
//                streak = running.streakDays + 1,
//                date = LocalDate.now().toString()
//            )
//            activityRepo.selectActivity(running.id)
//            context.startService(TimerForegroundService.stopIntent(context))
//        }
//    }
//
//    private fun buildRecentActivities(
//        activities: List<ActivityUiModel>
//    ): List<ActivityUiModel> {
//        return activities
//            .filter { activity ->
//                val label = activity.lastActiveDate.trim().lowercase()
//                label == "today" || label == "yesterday"
//            }
//            .sortedByDescending { activity ->
//                lastUsedWeight(activity.lastActiveDate)
//            }
//            .take(5)
//    }
//
//    private fun lastUsedWeight(label: String): Int {
//        return when (label.lowercase()) {
//            "today" -> 100
//            "yesterday" -> 90
//            else -> {
//                if (label.contains("days ago")) {
//                    val n = label.filter { it.isDigit() }.toIntOrNull() ?: 99
//                    maxOf(0, 80 - n)
//                } else {
//                    0
//                }
//            }
//        }
//    }
//
//    private data class HomeCombinedState(
//        val userName: String,
//        val activities: List<ActivityUiModel>,
//        val runningActivity: ActivityUiModel?,
//        val selectedId: Int?
//    )
//}





package com.sangeeta.chronomind.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import com.sangeeta.chronomind.service.TimerForegroundService
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRepo: ActivityRepository,
    private val onboardingRepo: OnboardingRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeHomeData()
    }

    private fun observeHomeData() {
        viewModelScope.launch {
            combine(
                onboardingRepo.userName,
                activityRepo.observeAll(),
                activityRepo.observeRunning(),
                activityRepo.selectedActivityId
            ) { userName, activities, running, selectedId ->
                HomeCombinedState(
                    userName = userName,
                    activities = activities.map { it.toUiModel() },
                    runningActivity = running?.toUiModel(),
                    selectedId = selectedId
                )
            }.collect { combined ->

                val recent = buildRecentActivities(combined.activities)

                val selectedUi = combined.activities.firstOrNull { it.id == combined.selectedId }
                val heroSelected = combined.runningActivity
                    ?: selectedUi
                    ?: recent.firstOrNull()
                    ?: combined.activities.firstOrNull()

                if (combined.selectedId == null && heroSelected != null) {
                    activityRepo.selectActivity(heroSelected.id)
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        appName = if (combined.userName.isBlank()) {
                            "ChronoMind"
                        } else {
                            "${combined.userName}'s ChronoMind"
                        },
                        subtitle = if (combined.runningActivity != null) {
                            "Stay locked in"
                        } else {
                            "Focus • Track • Grow"
                        },
                        selectedActivity = heroSelected,
                        runningActivity = combined.runningActivity,
                        recentActivities = recent
                    )
                }
            }
        }
    }

    fun onRecentActivitySelected(activityId: Int) {
        viewModelScope.launch {
            val running = uiState.value.runningActivity
            if (running != null && running.id != activityId) {
                activityRepo.updateTimer(
                    id = running.id,
                    elapsed = running.elapsedSeconds,
                    running = false
                )
                context.startService(TimerForegroundService.pauseIntent(context))
            }
            activityRepo.selectActivity(activityId)
        }
    }

    fun startFocus() {
        val selected = uiState.value.selectedActivity ?: return
        viewModelScope.launch {
            activityRepo.stopAll()
            activityRepo.updateTimer(
                id = selected.id,
                elapsed = selected.elapsedSeconds,
                running = true
            )
            activityRepo.selectActivity(selected.id)
            context.startForegroundService(TimerForegroundService.startIntent(context))
        }
    }

    fun pauseSession() {
        val running = uiState.value.runningActivity ?: return
        viewModelScope.launch {
            activityRepo.updateTimer(
                id = running.id,
                elapsed = running.elapsedSeconds,
                running = false
            )
            activityRepo.selectActivity(running.id)
            context.startService(TimerForegroundService.pauseIntent(context))
        }
    }

    fun finishSession() {
        val running = uiState.value.runningActivity ?: return
        viewModelScope.launch {
            activityRepo.logSession(running, isCompleted = true)
            activityRepo.updateTimer(
                id = running.id,
                elapsed = 0L,
                running = false
            )
            activityRepo.updateStreak(
                id = running.id,
                streak = running.streakDays + 1,
                date = "Today"
            )
            activityRepo.selectActivity(running.id)
            context.startService(TimerForegroundService.stopIntent(context))
        }
    }

    private fun buildRecentActivities(
        activities: List<ActivityUiModel>
    ): List<ActivityUiModel> {
        return activities
            .filter { activity ->
                val label = activity.lastActiveDate.trim().lowercase()
                label == "today" || label == "yesterday"
            }
            .sortedByDescending { activity ->
                lastUsedWeight(activity.lastActiveDate)
            }
            .take(5)
    }

    private fun lastUsedWeight(label: String): Int {
        return when (label.trim().lowercase()) {
            "today" -> 100
            "yesterday" -> 90
            else -> {
                if (label.contains("days ago", ignoreCase = true)) {
                    val n = label.filter { it.isDigit() }.toIntOrNull() ?: 99
                    maxOf(0, 80 - n)
                } else {
                    0
                }
            }
        }
    }

    private data class HomeCombinedState(
        val userName: String,
        val activities: List<ActivityUiModel>,
        val runningActivity: ActivityUiModel?,
        val selectedId: Int?
    )
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