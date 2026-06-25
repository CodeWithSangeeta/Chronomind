package com.sangeeta.chronomind.ui.create_activity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.ui.navigation.ChronoRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val repository: ActivityRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val activityId: Int = savedStateHandle[ChronoRoutes.CreateEditActivity.ARG] ?: -1

    private val _uiState = MutableStateFlow(CreateEditUiState())
    val uiState: StateFlow<CreateEditUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreateEditEvent>()
    val events: SharedFlow<CreateEditEvent> = _events.asSharedFlow()

    init {
        if (activityId != -1) loadForEdit(activityId)
    }

    private fun loadForEdit(id: Int) {
        repository.observeById(id)
            .onEach { entity ->
                entity ?: return@onEach
                _uiState.update { current ->
                    current.copy(
                        mode = CreateEditMode.EDIT,
                        activityId = entity.id,
                        activityName = entity.name,
                        targetHours = entity.targetMinutes / 60,
                        targetMinutes = entity.targetMinutes % 60,
                        selectedIcon = ActivityIconOption.entries.firstOrNull { it.emoji == entity.icon }
                            ?: ActivityIconOption.entries.first(),
                        selectedColor = ActivityColorOption.entries.firstOrNull { it.hex == entity.colorHex }
                            ?: ActivityColorOption.entries.first(),
                        targetType = TargetType.entries.firstOrNull { it.name == entity.targetType }
                            ?: TargetType.TIME,
                        targetCount = entity.targetCount.toString(),
                        targetUnit = entity.targetUnit,
                        streakBehavior = if (entity.continueStreakOnMiss) StreakBehavior.CONTINUE else StreakBehavior.RESET,
                        completionStyle = CompletionStyle.entries.firstOrNull { it.name == entity.completionStyle }
                            ?: CompletionStyle.MANUAL,
                        reminderEnabled = entity.reminderEnabled,
                        reminderTime = entity.reminderTime
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun submit() {
        val state = _uiState.value
        if (!state.canSubmit) return

        viewModelScope.launch {
            val totalMinutes = (state.targetHours * 60 + state.targetMinutes).coerceAtLeast(1)

            if (activityId == -1) {
                repository.add(
                    ActivityEntity(
                        name = state.activityName.trim(),
                        targetMinutes = totalMinutes,
                        icon = state.selectedIcon.emoji,
                        colorHex = state.selectedColor.hex,
                        continueStreakOnMiss = state.streakBehavior == StreakBehavior.CONTINUE,
                        orderIndex = 0,
                        targetType = state.targetType.name,
                        targetCount = state.targetCount.toIntOrNull() ?: 0,
                        targetUnit = state.targetUnit,
                        completionStyle = state.completionStyle.name,
                        reminderEnabled = state.reminderEnabled,
                        reminderTime = state.reminderTime
                    )
                )
            } else {
                val existing = repository.observeById(activityId).firstOrNull() ?: return@launch
                repository.update(
                    existing.copy(
                        name = state.activityName.trim(),
                        targetMinutes = totalMinutes,
                        icon = state.selectedIcon.emoji,
                        colorHex = state.selectedColor.hex,
                        continueStreakOnMiss = state.streakBehavior == StreakBehavior.CONTINUE,
                        targetType = state.targetType.name,
                        targetCount = state.targetCount.toIntOrNull() ?: 0,
                        targetUnit = state.targetUnit,
                        completionStyle = state.completionStyle.name,
                        reminderEnabled = state.reminderEnabled,
                        reminderTime = state.reminderTime
                    )
                )
            }
            _events.emit(CreateEditEvent.Done)
        }
    }

    fun deleteActivity() {
        if (activityId == -1) return
        viewModelScope.launch {
            val entity = repository.observeById(activityId).firstOrNull() ?: return@launch
            repository.delete(entity)
            _events.emit(CreateEditEvent.Done)
        }
    }

    fun updateActivityName(name: String) { if (name.length <= 40) _uiState.update { it.copy(activityName = name) } }
    fun selectIcon(icon: ActivityIconOption) { _uiState.update { it.copy(selectedIcon = icon) } }
    fun selectColor(color: ActivityColorOption) { _uiState.update { it.copy(selectedColor = color) } }
    fun selectTargetType(type: TargetType) { _uiState.update { it.copy(targetType = type) } }
    fun updateTargetHours(hours: Int) { _uiState.update { it.copy(targetHours = hours) } }
    fun updateTargetMinutes(minutes: Int) { _uiState.update { it.copy(targetMinutes = minutes) } }
    fun updateTargetCount(count: String) { _uiState.update { it.copy(targetCount = count) } }
    fun updateTargetUnit(unit: String) { _uiState.update { it.copy(targetUnit = unit) } }
    fun toggleReminder(enabled: Boolean) { _uiState.update { it.copy(reminderEnabled = enabled) } }
    fun setReminderTime(time: String) { _uiState.update { it.copy(reminderTime = time) } }
    fun toggleAdvancedExpanded() { _uiState.update { it.copy(advancedExpanded = !it.advancedExpanded) } }
    fun selectStreakBehavior(b: StreakBehavior) { _uiState.update { it.copy(streakBehavior = b) } }
    fun selectCompletionStyle(s: CompletionStyle) { _uiState.update { it.copy(completionStyle = s) } }
    fun showDeleteConfirm(show: Boolean) { _uiState.update { it.copy(showDeleteConfirm = show) } }
}

sealed class CreateEditEvent {
    object Done : CreateEditEvent()
}