//package com.sangeeta.chronomind.ui.create_activity
//
//
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import com.sangeeta.chronomind.repository.ActivityRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//@HiltViewModel
//class CreateEditViewModel @Inject constructor(
//    private val activityRepository: ActivityRepository,
//    savedStateHandle: SavedStateHandle
//) : ViewModel() {
//
//    private val activityId: Int? = savedStateHandle.get<String>("id")?.toIntOrNull()
//
//    private val _uiState = MutableStateFlow(
//        if (activityId == null) {
//            CreateEditUiState()
//        } else {
//            mockEditState(activityId)
//        }
//    )
//    val uiState: StateFlow<CreateEditUiState> = _uiState.asStateFlow()
//
//    fun updateActivityName(value: String) {
//        _uiState.update { it.copy(activityName = value.take(40)) }
//    }
//
//    fun selectIcon(icon: ActivityIconOption) {
//        _uiState.update { it.copy(selectedIcon = icon) }
//    }
//
//    fun selectColor(color: ActivityColorOption) {
//        _uiState.update { it.copy(selectedColor = color) }
//    }
//
//    fun selectTargetType(type: TargetType) {
//        _uiState.update { it.copy(targetType = type) }
//    }
//
//    fun updateTargetHours(value: Int) {
//        _uiState.update { it.copy(targetHours = value.coerceIn(0, 23)) }
//    }
//
//    fun updateTargetMinutes(value: Int) {
//        _uiState.update { it.copy(targetMinutes = value.coerceIn(0, 59)) }
//    }
//
//    fun updateTargetCount(value: String) {
//        _uiState.update { it.copy(targetCount = value.filter(Char::isDigit).take(4)) }
//    }
//
//    fun updateTargetUnit(value: String) {
//        _uiState.update { it.copy(targetUnit = value.take(12)) }
//    }
//
//    fun toggleReminder(enabled: Boolean) {
//        _uiState.update { it.copy(reminderEnabled = enabled) }
//    }
//
//    fun setReminderTime(time: String) {
//        _uiState.update { it.copy(reminderTime = time) }
//    }
//
//    fun toggleAdvancedExpanded() {
//        _uiState.update { it.copy(advancedExpanded = !it.advancedExpanded) }
//    }
//
//    fun selectStreakBehavior(behavior: StreakBehavior) {
//        _uiState.update { it.copy(streakBehavior = behavior) }
//    }
//
//    fun selectCompletionStyle(style: CompletionStyle) {
//        _uiState.update { it.copy(completionStyle = style) }
//    }
//
//    fun showDeleteConfirm(show: Boolean) {
//        _uiState.update { it.copy(showDeleteConfirm = show) }
//    }
//
//    fun submit() {
//        // UI-only phase. Wire save/create logic later.
//    }
//
//    fun deleteActivity() {
//        // UI-only phase. Wire delete logic later.
//    }
//
//    private fun mockEditState(id: Int): CreateEditUiState {
//        return CreateEditUiState(
//            mode = CreateEditMode.EDIT,
//            activityId = id,
//            activityName = "Creative Projects",
//            selectedIcon = ActivityIconOption.CREATIVE,
//            selectedColor = ActivityColorOption.AMBER,
//            targetType = TargetType.TIME,
//            targetHours = 1,
//            targetMinutes = 30,
//            reminderEnabled = true,
//            reminderTime = "07:00 PM",
//            advancedExpanded = true,
//            streakBehavior = StreakBehavior.CONTINUE,
//            completionStyle = CompletionStyle.MANUAL
//        )
//    }
//}








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

    private val activityId: Int =
        savedStateHandle[ChronoRoutes.CreateEditActivity.ARG] ?: -1

    private val _uiState = MutableStateFlow(CreateEditUiState())
    val uiState: StateFlow<CreateEditUiState> = _uiState.asStateFlow()

    init {
        if (activityId != -1) loadForEdit(activityId)
    }

    // ── Load existing activity for EDIT mode ────────────────────────────
    private fun loadForEdit(id: Int) {
        repository.observeById(id)
            .onEach { entity ->
                entity ?: return@onEach
                _uiState.update { current ->
                    current.copy(
                        mode           = CreateEditMode.EDIT,
//                        screenTitle    = "Edit Activity",
//                        primaryButtonText = "Save Changes",
//                        showDeleteAction  = true,
                        activityName   = entity.name,
                        targetHours    = entity.targetMinutes / 60,
                        targetMinutes  = entity.targetMinutes % 60,
                        selectedIcon   = ActivityIconOption.entries.firstOrNull {
                            it.emoji == entity.icon
                        } ?: ActivityIconOption.entries.first(),
                        selectedColor  = ActivityColorOption.entries.firstOrNull {
                            it.hex == entity.colorHex
                        } ?: ActivityColorOption.entries.first()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    // ── Save (insert or update) ─────────────────────────────────────────
    fun submit() {
        val state = _uiState.value
        if (!state.canSubmit) return

        viewModelScope.launch {
            val totalMinutes = (state.targetHours * 60) + state.targetMinutes

            if (activityId == -1) {
                // CREATE
                repository.add(
                    ActivityEntity(
                        name                 = state.activityName.trim(),
                        targetMinutes        = totalMinutes.coerceAtLeast(1),
                        icon                 = state.selectedIcon.emoji,
                        colorHex             = state.selectedColor.hex,
                        continueStreakOnMiss = state.streakBehavior == StreakBehavior.CONTINUE,
                        orderIndex           = 0
                    )
                )
            } else {
                // UPDATE — fetch current then copy new fields
                val existing = repository.observeById(activityId).firstOrNull() ?: return@launch
                repository.update(
                    existing.copy(
                        name                 = state.activityName.trim(),
                        targetMinutes        = totalMinutes.coerceAtLeast(1),
                        icon                 = state.selectedIcon.emoji,
                        colorHex             = state.selectedColor.hex,
                        continueStreakOnMiss = state.streakBehavior == StreakBehavior.CONTINUE
                    )
                )
            }
        }
    }

    // ── Delete ──────────────────────────────────────────────────────────
    fun deleteActivity() {
        if (activityId == -1) return
        viewModelScope.launch {
            val entity = repository.observeById(activityId).firstOrNull() ?: return@launch
            repository.delete(entity)
        }
    }

    // ── UI field updates (keep your existing ones, add emoji/hex access) ─
    fun updateActivityName(name: String) {
        if (name.length <= 40) _uiState.update { it.copy(activityName = name) }
    }
    fun selectIcon(icon: ActivityIconOption)           { _uiState.update { it.copy(selectedIcon = icon) } }
    fun selectColor(color: ActivityColorOption)        { _uiState.update { it.copy(selectedColor = color) } }
    fun selectTargetType(type: TargetType)             { _uiState.update { it.copy(targetType = type) } }
    fun updateTargetHours(hours: Int)                  { _uiState.update { it.copy(targetHours = hours) } }
    fun updateTargetMinutes(minutes: Int)              { _uiState.update { it.copy(targetMinutes = minutes) } }
    fun updateTargetCount(count: String)               { _uiState.update { it.copy(targetCount = count) } }
    fun updateTargetUnit(unit: String)                 { _uiState.update { it.copy(targetUnit = unit) } }
    fun toggleReminder(enabled: Boolean)               { _uiState.update { it.copy(reminderEnabled = enabled) } }
    fun setReminderTime(time: String)                  { _uiState.update { it.copy(reminderTime = time) } }
    fun toggleAdvancedExpanded()                       { _uiState.update { it.copy(advancedExpanded = !it.advancedExpanded) } }
    fun selectStreakBehavior(b: StreakBehavior)         { _uiState.update { it.copy(streakBehavior = b) } }
    fun selectCompletionStyle(s: CompletionStyle)      { _uiState.update { it.copy(completionStyle = s) } }
    fun showDeleteConfirm(show: Boolean)               { _uiState.update { it.copy(showDeleteConfirm = show) } }
}