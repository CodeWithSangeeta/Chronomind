package com.sangeeta.chronomind

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import com.sangeeta.chronomind.ui.model.ActivitySessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    val uiState = onboardingRepository.isOnboardingComplete
        .map { completed ->
            MainUiState(
                isLoading = false,
                isOnboardingComplete = completed
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = MainUiState(isLoading = true)
        )

    val finishedTimerActivity = activityRepository.observeAll()
        .map { activities ->
            val now = System.currentTimeMillis()

            activities.firstOrNull { activity ->
                activityRepository
                    .buildDisplayState(activity, now)
                    .sessionState == ActivitySessionState.FINISHED_WAITING_FOR_USER
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun stopFinishedTimer() {
        val activity = finishedTimerActivity.value ?: return

        viewModelScope.launch {
            val entity = activityRepository
                .observeById(activity.id)
                .firstOrNull()
                ?: return@launch

            activityRepository.abandonToHistory(entity)
        }
    }

    fun completeFinishedTimer() {
        val activity = finishedTimerActivity.value ?: return

        viewModelScope.launch {
            val entity = activityRepository
                .observeById(activity.id)
                .firstOrNull()
                ?: return@launch

            activityRepository.completeSession(
                entity,
                finalElapsed = entity.targetMinutes * 60L
            )
        }
    }

    fun getFinishedOvertimeSeconds(activity: ActivityEntity): Long {
        return activityRepository.computeFinishedOvertimeSeconds(activity)
    }
}

data class MainUiState(
    val isLoading: Boolean = true,
    val isOnboardingComplete: Boolean = false
)