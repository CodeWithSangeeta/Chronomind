package com.sangeeta.chronomind.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


sealed class OnboardingNavEvent {
    data object NextStep         : OnboardingNavEvent()
    data object PreviousStep     : OnboardingNavEvent()
    data object NavigateToMain   : OnboardingNavEvent()
}


@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepo: OnboardingRepository,
    private val activityRepo: ActivityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _navEvent = Channel<OnboardingNavEvent>(Channel.BUFFERED)
    val navEvent = _navEvent.receiveAsFlow()


    fun nextStep() {
        _state.update { it.copy(currentStep = it.currentStep + 1) }
        viewModelScope.launch { _navEvent.send(OnboardingNavEvent.NextStep) }
    }

    fun previousStep() {
        if (_state.value.currentStep > 0) {
            _state.update { it.copy(currentStep = it.currentStep - 1) }
            viewModelScope.launch { _navEvent.send(OnboardingNavEvent.PreviousStep) }
        }
    }

    fun toggleFocusArea(area: FocusArea) {
        _state.update { current ->
            val updated = current.selectedFocusAreas.toMutableSet()
            if (area in updated) updated.remove(area) else updated.add(area)
            current.copy(selectedFocusAreas = updated)
        }
    }
    fun toggleAccountability(type: AccountabilityType) {
        _state.update { current ->
            val updated = current.selectedAccountabilityTypes.toMutableSet()
            if (type in updated) updated.remove(type) else updated.add(type)
            current.copy(selectedAccountabilityTypes = updated)
        }
    }

    fun selectCheckInStyle(style: CheckInStyle) {
        _state.update { it.copy(selectedCheckInStyle = style) }
    }

    fun selectStreakMissChoice(choice: StreakMissChoice) {
        _state.update { it.copy(selectedStreakMissChoice = choice) }
    }

    fun getFocusAreaLabels(): List<String> =
        _state.value.selectedFocusAreas.map { it.label }

    fun getAccountabilityLabels(): List<String> =
        _state.value.selectedAccountabilityTypes.map { it.title }

    fun getCheckInLabel(): String =
        _state.value.selectedCheckInStyle?.title ?: "Not selected"

    fun getStreakMissLabel(): String =
        _state.value.selectedStreakMissChoice?.title ?: "Not selected"

   fun finishOnboarding() {
        viewModelScope.launch {
            val current = _state.value
            onboardingRepo.saveOnboardingResult(
                name           = "",
                accountability = current.selectedAccountabilityTypes
                    .map { it.name }
                    .joinToString(","),
                checkIn        = current.selectedCheckInStyle?.name    ?: "",
                streakMiss     = current.selectedStreakMissChoice?.name ?: ""
            )

            val today    = LocalDate.now().toString()
            val continueStreak = current.selectedStreakMissChoice == StreakMissChoice.CONTINUE

            val activities = current.selectedFocusAreas
                .mapIndexed { index, area ->
                    ActivityEntity(
                        name = area.label,
                        targetMinutes = defaultTargetMinutes(area),
                        elapsedSeconds = 0L,
                        isRunning = false,
                        streakDays = 0,
                        lastActiveDate = today,
                        continueStreakOnMiss = continueStreak,
                        orderIndex = index,
                        icon = area.icon,
                    )
                }

            if (activities.isNotEmpty()) {
                activityRepo.seedActivities(activities)
            }

            _state.update { it.copy(isFinished = true) }
            _navEvent.send(OnboardingNavEvent.NavigateToMain)
        }
    }


    private fun defaultTargetMinutes(area: FocusArea): Int = when (area) {
        FocusArea.STUDY      -> 120
        FocusArea.EXERCISE   -> 45
        FocusArea.READING    -> 30
        FocusArea.WORK       -> 180
        FocusArea.MEDITATION -> 20
        FocusArea.CREATIVE   -> 60
        FocusArea.GROWTH     -> 30
        FocusArea.OTHER      -> 60
    }
}