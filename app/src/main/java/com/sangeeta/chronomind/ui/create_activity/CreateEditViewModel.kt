package com.sangeeta.chronomind.ui.create_activity


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sangeeta.chronomind.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val activityId: Int? = savedStateHandle.get<String>("id")?.toIntOrNull()

    private val _uiState = MutableStateFlow(
        if (activityId == null) {
            CreateEditUiState()
        } else {
            mockEditState(activityId)
        }
    )
    val uiState: StateFlow<CreateEditUiState> = _uiState.asStateFlow()

    fun updateActivityName(value: String) {
        _uiState.update { it.copy(activityName = value.take(40)) }
    }

    fun selectIcon(icon: ActivityIconOption) {
        _uiState.update { it.copy(selectedIcon = icon) }
    }

    fun selectColor(color: ActivityColorOption) {
        _uiState.update { it.copy(selectedColor = color) }
    }

    fun selectTargetType(type: TargetType) {
        _uiState.update { it.copy(targetType = type) }
    }

    fun updateTargetHours(value: Int) {
        _uiState.update { it.copy(targetHours = value.coerceIn(0, 23)) }
    }

    fun updateTargetMinutes(value: Int) {
        _uiState.update { it.copy(targetMinutes = value.coerceIn(0, 59)) }
    }

    fun updateTargetCount(value: String) {
        _uiState.update { it.copy(targetCount = value.filter(Char::isDigit).take(4)) }
    }

    fun updateTargetUnit(value: String) {
        _uiState.update { it.copy(targetUnit = value.take(12)) }
    }

    fun toggleReminder(enabled: Boolean) {
        _uiState.update { it.copy(reminderEnabled = enabled) }
    }

    fun setReminderTime(time: String) {
        _uiState.update { it.copy(reminderTime = time) }
    }

    fun toggleAdvancedExpanded() {
        _uiState.update { it.copy(advancedExpanded = !it.advancedExpanded) }
    }

    fun selectStreakBehavior(behavior: StreakBehavior) {
        _uiState.update { it.copy(streakBehavior = behavior) }
    }

    fun selectCompletionStyle(style: CompletionStyle) {
        _uiState.update { it.copy(completionStyle = style) }
    }

    fun showDeleteConfirm(show: Boolean) {
        _uiState.update { it.copy(showDeleteConfirm = show) }
    }

    fun submit() {
        // UI-only phase. Wire save/create logic later.
    }

    fun deleteActivity() {
        // UI-only phase. Wire delete logic later.
    }

    private fun mockEditState(id: Int): CreateEditUiState {
        return CreateEditUiState(
            mode = CreateEditMode.EDIT,
            activityId = id,
            activityName = "Creative Projects",
            selectedIcon = ActivityIconOption.CREATIVE,
            selectedColor = ActivityColorOption.AMBER,
            targetType = TargetType.TIME,
            targetHours = 1,
            targetMinutes = 30,
            reminderEnabled = true,
            reminderTime = "07:00 PM",
            advancedExpanded = true,
            streakBehavior = StreakBehavior.CONTINUE,
            completionStyle = CompletionStyle.MANUAL
        )
    }
}