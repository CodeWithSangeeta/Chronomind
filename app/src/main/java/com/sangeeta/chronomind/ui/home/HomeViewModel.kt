package com.sangeeta.chronomind.ui.home

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import com.sangeeta.chronomind.service.TimerForegroundService
import com.sangeeta.chronomind.ui.mapper.toUiModel
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                        appName = if (combined.userName.isBlank()) "ChronoMind" else "${combined.userName}'s ChronoMind",
                        subtitle = if (combined.runningActivity != null) "Stay locked in" else "Focus • Track • Grow",
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
                activityRepo.updateTimer(id = running.id, elapsed = running.elapsedSeconds, running = false)
                context.startService(TimerForegroundService.pauseIntent(context))
            }
            activityRepo.selectActivity(activityId)
        }
    }

    fun startFocus() {
        val selected = uiState.value.selectedActivity ?: return
        viewModelScope.launch {
            activityRepo.stopAll()
            activityRepo.updateTimer(id = selected.id, elapsed = selected.elapsedSeconds, running = true)
            activityRepo.selectActivity(selected.id)
            startTimerService()
        }
    }

    fun pauseSession() {
        val running = uiState.value.runningActivity ?: return
        viewModelScope.launch {
            activityRepo.updateTimer(id = running.id, elapsed = running.elapsedSeconds, running = false)
            activityRepo.selectActivity(running.id)
            context.startService(TimerForegroundService.pauseIntent(context))
        }
    }

    fun finishSession() {
        val running = uiState.value.runningActivity ?: return
        viewModelScope.launch {
            activityRepo.logSession(running, isCompleted = true)
            activityRepo.updateTimer(id = running.id, elapsed = 0L, running = false)
            activityRepo.updateStreak(id = running.id, streak = running.streakDays + 1, date = "Today")
            activityRepo.selectActivity(running.id)
            context.startService(TimerForegroundService.stopIntent(context))
        }
    }

    fun startActivityDirectly(activityId: Int) {
        viewModelScope.launch {
            activityRepo.stopAll()
            activityRepo.selectActivity(activityId)
            val entity = activityRepo.observeById(activityId).firstOrNull() ?: return@launch
            activityRepo.updateTimer(id = activityId, elapsed = entity.elapsedSeconds, running = true)
            startTimerService()
        }
    }

    private fun startTimerService() {
        val intent = TimerForegroundService.startIntent(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    private fun buildRecentActivities(activities: List<ActivityUiModel>): List<ActivityUiModel> {
        return activities
            .filter { activity ->
                val label = activity.lastActiveDate.trim().lowercase()
                label == "today" || label == "yesterday" || activity.elapsedSeconds > 0 ||
                        (label.contains("days ago") && (label.filter { it.isDigit() }.toIntOrNull() ?: 99) <= 7)
            }
            .sortedWith(
                compareByDescending<ActivityUiModel> { lastUsedWeight(it.lastActiveDate) }
                    .thenByDescending { it.elapsedSeconds }
            )
            .take(5)
    }

    private fun lastUsedWeight(label: String): Int = when (label.trim().lowercase()) {
        "today" -> 100
        "yesterday" -> 90
        else -> 0
    }

    private data class HomeCombinedState(
        val userName: String,
        val activities: List<ActivityUiModel>,
        val runningActivity: ActivityUiModel?,
        val selectedId: Int?
    )
}
