package com.sangeeta.chronomind.ui.create_activity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Nav arg: activityId = -1 means CREATE mode
    private val activityId: Int = savedStateHandle.get<Int>("activityId") ?: -1

    private val _uiState = MutableStateFlow(CreateEditUiState())
    val uiState: StateFlow<CreateEditUiState> = _uiState.asStateFlow()

    init {
        if (activityId != -1) loadForEdit(activityId)
    }

    private fun loadForEdit(id: Int) {
        viewModelScope.launch {
            val entity = activityRepository.observeById(id).firstOrNull() ?: return@launch
            val icon = ActivityIconOption.entries.find { it.icon == entity.icon }
                ?: ActivityIconOption.GROWTH
            val color = ActivityColorOption.entries.find { it.hex == entity.colorHex }
                ?: ActivityColorOption.AMBER
            val targetType = if (entity.targetType == "STOPWATCH") TargetType.STOPWATCH
            else TargetType.TIMER
            val completionStyle = if (entity.completionStyle == "TIMER_END") CompletionStyle.TIMER_END
            else CompletionStyle.MANUAL
            val streakBehavior = if (entity.continueStreakOnMiss) StreakBehavior.CONTINUE
            else StreakBehavior.RESET
            _uiState.update {
                it.copy(
                    mode = CreateEditMode.EDIT,
                    activityId = id,
                    screenTitle = "Edit Activity",
                    activityName = entity.name,
                    selectedIcon = icon,
                    selectedColor = color,
                    targetType = targetType,
                    targetHours = entity.targetMinutes / 60,
                    targetMinutes = entity.targetMinutes % 60,
                    reminderEnabled = entity.reminderEnabled,
                    reminderTime = entity.reminderTime,
                    streakBehavior = streakBehavior,
                    completionStyle = completionStyle,
                )
            }
        }
    }

    // ── Field updaters ────────────────────────────────────────────────────────

    fun updateActivityName(name: String) {
        if (name.length <= 40) _uiState.update { it.copy(activityName = name) }
    }

    fun selectIcon(icon: ActivityIconOption) = _uiState.update { it.copy(selectedIcon = icon) }

    fun selectColor(color: ActivityColorOption) = _uiState.update { it.copy(selectedColor = color) }

    fun selectTargetType(type: TargetType) {
        _uiState.update {
            it.copy(
                targetType = type,
                // When switching to STOPWATCH, force MANUAL completion
                completionStyle = if (type == TargetType.STOPWATCH) CompletionStyle.MANUAL
                else it.completionStyle
            )
        }
    }

    fun updateTargetHours(h: Int) = _uiState.update { it.copy(targetHours = h.coerceIn(0, 23)) }

    fun updateTargetMinutes(m: Int) = _uiState.update { it.copy(targetMinutes = m.coerceIn(0, 59)) }

    fun toggleReminder(enabled: Boolean) = _uiState.update { it.copy(reminderEnabled = enabled) }

    fun setReminderTime(time: String) = _uiState.update { it.copy(reminderTime = time) }

    fun toggleAdvancedExpanded() = _uiState.update { it.copy(isAdvancedExpanded = !it.isAdvancedExpanded) }

    fun selectStreakBehavior(b: StreakBehavior) = _uiState.update { it.copy(streakBehavior = b) }

    fun selectCompletionStyle(s: CompletionStyle) = _uiState.update { it.copy(completionStyle = s) }

    fun showDeleteConfirm(show: Boolean) = _uiState.update { it.copy(showDeleteConfirm = show) }

    // ── Save ─────────────────────────────────────────────────────────────────

    fun submit(onDone: () -> Unit) {
        val state = _uiState.value
        if (!state.isValid || state.isSaving) return

        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            try {
                if (state.mode == CreateEditMode.CREATE) {
                    activityRepository.add(state.toEntity())
                } else {
                    val existing = activityRepository.observeById(state.activityId).firstOrNull()
                    activityRepository.update(state.toEntity(existing))
                }
                onDone()
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    fun deleteActivity(onDone: () -> Unit) {
        viewModelScope.launch {
            val entity = activityRepository.observeById(activityId).firstOrNull() ?: return@launch
            activityRepository.delete(entity)
            onDone()
        }
    }
}
