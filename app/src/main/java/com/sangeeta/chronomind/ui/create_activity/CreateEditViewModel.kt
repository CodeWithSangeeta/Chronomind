package com.sangeeta.chronomind.ui.create_activity


import android.content.Context
import android.os.Build
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.service.TimerForegroundService
import com.sangeeta.chronomind.ui.model.ActivityColorOption
import com.sangeeta.chronomind.ui.model.ActivityIconOption
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface CreateEditActivityEvent {
    data object NavigateBack : CreateEditActivityEvent
    data object NavigateHomeAfterStart : CreateEditActivityEvent
}

@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val activityId: Int = savedStateHandle.get<Int>("activityId") ?: -1
    private val isEditMode: Boolean = activityId != -1

    private val _uiState = MutableStateFlow(
        CreateEditUiState(
            mode = if (isEditMode) CreateEditMode.EDIT else CreateEditMode.CREATE
        )
    )
    val uiState: StateFlow<CreateEditUiState> = _uiState.asStateFlow()

    private val _events = Channel<CreateEditActivityEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        if (isEditMode) {
            loadActivity()
        }
    }

    private fun loadActivity() {
        viewModelScope.launch {
            val entity = activityRepository.observeById(activityId).firstOrNull() ?: return@launch
            _uiState.value = _uiState.value.copy(
                mode = CreateEditMode.EDIT,
                editingActivityId = entity.id,
                activityName = entity.name,
                selectedIcon = ActivityIconOption.fromName(ActivityIconOption.toName(entity.icon)),
                selectedColor = ActivityColorOption.fromHex(entity.colorHex),
                targetType = if (entity.targetType == "STOPWATCH") TargetType.STOPWATCH else TargetType.TIMER,
                targetHours = entity.targetMinutes / 60,
                targetMinutes = entity.targetMinutes % 60,
                reminderEnabled = entity.reminderEnabled,
                reminderTime = entity.reminderTime,
                streakBehavior = if (entity.continueStreakOnMiss) StreakBehavior.CONTINUE_STREAK else StreakBehavior.RESET_TO_ZERO,
                completionStyle = if (entity.completionStyle == "TIMEREND") {
                    CompletionStyle.AUTO_CHECK
                } else {
                    CompletionStyle.MANUAL_CHECK
                },
                existingElapsedSeconds = entity.elapsedSeconds,
                existingStreakDays = entity.streakDays,
                existingLastActiveDate = entity.lastActiveDate,
                existingOrderIndex = entity.orderIndex,
                existingHasPendingSession = entity.hasPendingSession,
                existingPendingSessionDate = entity.pendingSessionDate,
                existingSessionStartedAtEpochMillis = entity.sessionStartedAtEpochMillis,
                existingSessionEndsAtEpochMillis = entity.sessionEndsAtEpochMillis,
                existingAccumulatedElapsedBeforeStartSeconds = entity.accumulatedElapsedBeforeStartSeconds,
                existingCompletedDate = entity.completedDate,
                isLoading = false
            )
        }
    }

    fun updateActivityName(value: String) {
        _uiState.value = _uiState.value.copy(activityName = value.take(40))
    }

    fun selectIcon(option: ActivityIconOption) {
        _uiState.value = _uiState.value.copy(selectedIcon = option)
    }

    fun selectColor(option: ActivityColorOption) {
        _uiState.value = _uiState.value.copy(selectedColor = option)
    }

    fun selectTargetType(type: TargetType) {
        _uiState.value = _uiState.value.copy(targetType = type)
    }

    fun updateTargetHours(value: Int) {
        _uiState.value = _uiState.value.copy(targetHours = value.coerceIn(0, 23))
    }

    fun updateTargetMinutes(value: Int) {
        _uiState.value = _uiState.value.copy(targetMinutes = value.coerceIn(0, 59))
    }

    fun toggleReminder(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(reminderEnabled = enabled)
    }

    fun setReminderTime(time: String) {
        _uiState.value = _uiState.value.copy(reminderTime = time)
    }

    fun toggleAdvancedExpanded() {
        _uiState.value = _uiState.value.copy(
            isAdvancedExpanded = !_uiState.value.isAdvancedExpanded
        )
    }

    fun selectStreakBehavior(value: StreakBehavior) {
        _uiState.value = _uiState.value.copy(streakBehavior = value)
    }

    fun selectCompletionStyle(value: CompletionStyle) {
        _uiState.value = _uiState.value.copy(completionStyle = value)
    }

    fun showDeleteConfirm(show: Boolean) {
        _uiState.value = _uiState.value.copy(showDeleteConfirm = show)
    }

    fun submit() {
        val current = _uiState.value
        if (!current.isValid || current.isSaving) return

        viewModelScope.launch {
            _uiState.value = current.copy(isSaving = true)

            val entity = ActivityEntity(
                id = current.editingActivityId ?: 0,
                name = current.activityName.trim(),
                targetMinutes = if (current.targetType == TargetType.STOPWATCH) {
                    0
                } else {
                    current.targetHours * 60 + current.targetMinutes
                },
                elapsedSeconds = if (isEditMode) current.existingElapsedSeconds else 0L,
                isRunning = false,
                streakDays = if (isEditMode) current.existingStreakDays else 0,
                lastActiveDate = if (isEditMode) current.existingLastActiveDate else "",
                continueStreakOnMiss = current.streakBehavior == StreakBehavior.CONTINUE_STREAK,
                orderIndex = if (isEditMode) current.existingOrderIndex else Int.MAX_VALUE,
                icon = current.selectedIcon.icon,
                colorHex = current.selectedColor.hex,
                targetType = if (current.targetType == TargetType.STOPWATCH) "STOPWATCH" else "TIMER",
                completionStyle = if (current.completionStyle == CompletionStyle.AUTO_CHECK) "TIMEREND" else "MANUAL",
                reminderEnabled = current.reminderEnabled,
                reminderTime = current.reminderTime,
                hasPendingSession = if (isEditMode) current.existingHasPendingSession else false,
                pendingSessionDate = if (isEditMode) current.existingPendingSessionDate else "",
                sessionStartedAtEpochMillis = if (isEditMode) current.existingSessionStartedAtEpochMillis else null,
                sessionEndsAtEpochMillis = if (isEditMode) current.existingSessionEndsAtEpochMillis else null,
                accumulatedElapsedBeforeStartSeconds = if (isEditMode) {
                    current.existingAccumulatedElapsedBeforeStartSeconds
                } else {
                    0L
                },
                completedDate = if (isEditMode) current.existingCompletedDate else ""
            )

            if (isEditMode) {
                activityRepository.update(entity)
                _uiState.value = _uiState.value.copy(isSaving = false)
                _events.send(CreateEditActivityEvent.NavigateBack)
            } else {
                val insertedId = activityRepository.add(entity).toInt()
                val saved = activityRepository.observeById(insertedId).firstOrNull()

                if (saved != null) {
                    activityRepository.resumeOrStart(saved)
                    startTimerService()
                }

                _uiState.value = _uiState.value.copy(isSaving = false)
                _events.send(CreateEditActivityEvent.NavigateHomeAfterStart)
            }
        }
    }

    fun deleteActivity(onDeleted: () -> Unit = {}) {
        val id = _uiState.value.editingActivityId ?: return
        viewModelScope.launch {
            val entity = activityRepository.observeById(id).firstOrNull() ?: return@launch
            activityRepository.delete(entity)
            onDeleted()
            _events.send(CreateEditActivityEvent.NavigateBack)
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
}