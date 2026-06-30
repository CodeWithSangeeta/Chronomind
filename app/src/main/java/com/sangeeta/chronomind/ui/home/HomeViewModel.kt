package com.sangeeta.chronomind.ui.home

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import com.sangeeta.chronomind.service.TimerForegroundService
import com.sangeeta.chronomind.ui.mapper.toUiModel
import com.sangeeta.chronomind.ui.model.ActivityDisplayState
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRepo: ActivityRepository,
    private val onboardingRepo: OnboardingRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()
    var lastPermissionRequestedActivityId: Int? = null
        private set

    sealed interface HomeEvent {
        data class RequestNotificationPermission(val activityId: Int) : HomeEvent
    }
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _scrollToTimerSignal = MutableSharedFlow<Long>(extraBufferCapacity = 1)
    val scrollToTimerSignal = _scrollToTimerSignal.asSharedFlow()

    private val _activeTimerCardActivityId = MutableStateFlow<Int?>(null)
    val activeTimerCardActivityId: StateFlow<Int?> = _activeTimerCardActivityId.asStateFlow()

    val heroDisplayState: StateFlow<ActivityDisplayState?> =
        combine(
            activityRepo.observeAll(),
            activityRepo.selectedActivityId
        ) { activities, selectedId ->
            val now = System.currentTimeMillis()
            val running  = activities.firstOrNull { it.isRunning }
            val selected = activities.firstOrNull { it.id == selectedId }
            val fallback = activities.firstOrNull()
            val hero = running ?: selected ?: fallback
            hero?.let { activityRepo.buildDisplayState(it, now) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _showFinishDialog = MutableStateFlow(false)
    val showFinishDialog: StateFlow<Boolean> = _showFinishDialog.asStateFlow()

    init {
        viewModelScope.launch { activityRepo.syncDayBoundaryIfNeeded() }
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
                    userName     = userName,
                    activities   = activities.map { it.toUiModel() },
                    runningActivity = running?.toUiModel(),
                    selectedId   = selectedId
                )
            }.collect { combined ->

                val recent = buildRecentActivities(combined.activities)

                val selectedUi  = combined.activities.firstOrNull { it.id == combined.selectedId }
                val heroSelected = combined.runningActivity
                    ?: selectedUi
                    ?: recent.firstOrNull()
                    ?: combined.activities.firstOrNull()

                _activeTimerCardActivityId.value = combined.runningActivity?.id ?: heroSelected?.id

                if (combined.selectedId == null && heroSelected != null) {
                    activityRepo.selectActivity(heroSelected.id)
                }

                _uiState.update {
                    it.copy(
                        isLoading        = false,
                        appName          = if (combined.userName.isBlank()) "ChronoMind"
                        else "${combined.userName}'s ChronoMind",
                        selectedActivity = heroSelected,
                        runningActivity  = combined.runningActivity,
                        recentActivities = recent
                    )
                }
            }
        }
    }


    fun onRecentActivitySelected(activityId: Int) {
        activityRepo.selectActivity(activityId)
    }



    fun startFocus() {
        val activityId = heroDisplayState.value?.activityId
            ?: uiState.value.selectedActivity?.id
            ?: return
        lastPermissionRequestedActivityId = activityId
        viewModelScope.launch {
            _events.send(HomeEvent.RequestNotificationPermission(activityId))
        }
    }

    fun onNotificationPermissionDenied() {
        // optional: update snackbar/message state
    }

    fun continueStartFocusAfterPermission(activityId: Int) {
        viewModelScope.launch {
            val entity = activityRepo.observeById(activityId).firstOrNull() ?: return@launch
            val displayState = activityRepo.buildDisplayState(entity)
            if (!displayState.canStart) return@launch

            activityRepo.resumeOrStart(entity)
            startTimerService()
            _scrollToTimerSignal.tryEmit(System.currentTimeMillis())
        }
    }

    fun startActivityDirectly(activityId: Int) {
        viewModelScope.launch {
            val entity = activityRepo.observeById(activityId).firstOrNull() ?: return@launch
            if (!activityRepo.buildDisplayState(entity).canStart) return@launch
            activityRepo.switchToActivity(entity)
            startTimerService()
            _scrollToTimerSignal.tryEmit(System.currentTimeMillis())
        }
    }

    fun pauseSession() {
        context.startService(TimerForegroundService.pauseIntent(context))
    }

    fun requestFinish() {
        _showFinishDialog.value = true
    }

    fun confirmFinish() {
        _showFinishDialog.value = false
        val running = uiState.value.runningActivity ?: return
        viewModelScope.launch {
            val entity = activityRepo.observeById(running.id).firstOrNull() ?: return@launch
            activityRepo.completeSession(entity)
            context.startService(TimerForegroundService.stopIntent(context))
        }
    }

    fun cancelFinish() {
        _showFinishDialog.value = false
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
                        (label.contains("days ago") &&
                                (label.filter { it.isDigit() }.toIntOrNull() ?: 99) <= 7)
            }
            .sortedWith(
                compareByDescending<ActivityUiModel> { lastUsedWeight(it.lastActiveDate) }
                    .thenByDescending { it.elapsedSeconds }
            )
            .take(5)
    }

    private fun lastUsedWeight(label: String): Int = when (label.trim().lowercase()) {
        "today"     -> 100
        "yesterday" -> 90
        else        -> 0
    }

    private data class HomeCombinedState(
        val userName: String,
        val activities: List<ActivityUiModel>,
        val runningActivity: ActivityUiModel?,
        val selectedId: Int?
    )
}