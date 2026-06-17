package com.sangeeta.chronomind.ui.newactivity



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewActivityState(
    val name:            String  = "",
    val icon:            String  = "📖",
    val colorHex:        String  = "#FFCC00",
    val motivation:      String  = "",
    val targetHours:     Int     = 1,
    val targetMinutes:   Int     = 0,
    val reminderEnabled: Boolean = true,
    val isSaved:         Boolean = false
)

@HiltViewModel
class NewActivityViewModel @Inject constructor(
    private val repo: ActivityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewActivityState())
    val state: StateFlow<NewActivityState> = _state.asStateFlow()

    fun onNameChange(v: String)       = _state.update { it.copy(name = v) }
    fun onIconChange(v: String)       = _state.update { it.copy(icon = v) }
    fun onColorChange(v: String)      = _state.update { it.copy(colorHex = v) }
    fun onMotivationChange(v: String) = _state.update { it.copy(motivation = v) }
    fun onTargetHoursChange(v: Int)   = _state.update { it.copy(targetHours = v) }
    fun onTargetMinutesChange(v: Int) = _state.update { it.copy(targetMinutes = v) }
    fun onReminderToggle(v: Boolean)  = _state.update { it.copy(reminderEnabled = v) }

    fun save() {
        val s = _state.value
        if (s.name.isBlank()) return
        viewModelScope.launch {
            val targetMins = s.targetHours * 60 + s.targetMinutes
            repo.add(
                ActivityEntity(
                    name                = s.name,
                    icon                = s.icon,
                    colorHex            = s.colorHex,
                    targetMinutes       = if (targetMins > 0) targetMins else 60,
                    continueStreakOnMiss = true,
                    orderIndex          = 999
                )
            )
            _state.update { it.copy(isSaved = true) }
        }
    }
}