//package com.sangeeta.chronomind.ui.home
//
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.sangeeta.chronomind.local.db.entity.ActivityEntity
//import com.sangeeta.chronomind.repository.ActivityRepository
//import com.sangeeta.chronomind.repository.OnboardingRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.stateIn
//import java.time.LocalTime
//import javax.inject.Inject
//import kotlin.math.min
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    activityRepository: ActivityRepository,
//    onboardingRepository: OnboardingRepository
//) : ViewModel() {
//
//    val uiState: StateFlow<HomeUiState> = combine(
//        onboardingRepository.userName,
//        activityRepository.observeAll()
//    ) { userName, activities ->
//        val runningActivity = activities.firstOrNull { it.isRunning }
//        val primaryActivity = runningActivity ?: activities.firstOrNull()
//
//        HomeUiState(
//            userName = userName.ifBlank { "Sangeeta" },
//            greeting = greetingForCurrentTime(),
//            currentFocus = primaryActivity?.toHomeFocusCardUi(),
//            todayProgressText = activities.toTodayProgressText(),
//            activities = activities.map { it.toHomeActivityItemUi() },
//            isLoading = false
//        )
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = HomeUiState()
//    )
//
//    private fun greetingForCurrentTime(): String {
//        return when (LocalTime.now().hour) {
//            in 5..11 -> "Good morning"
//            in 12..16 -> "Good afternoon"
//            in 17..21 -> "Good evening"
//            else -> "Good evening"
//        }
//    }
//
//    private fun ActivityEntity.toHomeFocusCardUi(): HomeFocusCardUi {
//        return HomeFocusCardUi(
//            id = id,
//            title = name,
//            formattedElapsed = elapsedSeconds.toReadableTime(),
//            status = if (isRunning) "Running" else "Ready",
//            progress = progressFraction()
//        )
//    }
//
//    private fun ActivityEntity.toHomeActivityItemUi(): HomeActivityItemUi {
//        return HomeActivityItemUi(
//            id = id,
//            title = name,
//            formattedElapsed = elapsedSeconds.toReadableTime(),
//            status = when {
//                isRunning -> "Running"
//                elapsedSeconds > 0L -> "Paused"
//                else -> "Ready"
//            },
//            progress = progressFraction(),
//            isRunning = isRunning
//        )
//    }
//
//    private fun ActivityEntity.progressFraction(): Float {
//        val totalTargetSeconds = targetMinutes * 60f
//        if (totalTargetSeconds <= 0f) return 0f
//        return min(elapsedSeconds / totalTargetSeconds, 1f)
//    }
//
//    private fun List<ActivityEntity>.toTodayProgressText(): String {
//        val totalSeconds = sumOf { it.elapsedSeconds }
//        val hours = totalSeconds / 3600
//        val minutes = (totalSeconds % 3600) / 60
//
//        return when {
//            hours > 0 -> "${hours}h ${minutes}m"
//            else -> "${minutes}m"
//        }
//    }
//
//    private fun Long.toReadableTime(): String {
//        val totalMinutes = this / 60
//        val hours = totalMinutes / 60
//        val minutes = totalMinutes % 60
//
//        return when {
//            hours > 0 -> "${hours}h ${minutes}m"
//            else -> "${minutes}m"
//        }
//    }
//}

package com.sangeeta.chronomind.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.repository.ActivityRepository
import com.sangeeta.chronomind.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val userName:       String              = "",
    val activities:     List<ActivityUiModel> = emptyList(),
    val runningActivity: ActivityUiModel?   = null,
    val totalTodaySec:  Long                = 0L
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRepo:   ActivityRepository,
    private val onboardingRepo: OnboardingRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        onboardingRepo.userName,
        activityRepo.observeAll()
    ) { name, entities ->
        val models = entities.map { e ->
            ActivityUiModel(
                id             = e.id,
                name           = e.name,
                icon           = e.icon,
                colorHex       = e.colorHex,
                elapsedSeconds = e.elapsedSeconds,
                targetSeconds  = e.targetMinutes * 60L,
                isRunning      = e.isRunning,
                streakDays     = e.streakDays,
                lastActiveDate = e.lastActiveDate,
                continueOnMiss = e.continueStreakOnMiss
            )
        }
        HomeUiState(
            userName        = name,
            activities      = models,
            runningActivity = models.firstOrNull { it.isRunning },
            totalTodaySec   = models.sumOf { it.elapsedSeconds }
        )
    }.stateIn(
        scope         = viewModelScope,
        started       = SharingStarted.WhileSubscribed(5_000),
        initialValue  = HomeUiState()
    )

    // ── Timer control ────────────────────────────────────────────────────────

    fun startActivity(id: Int) {
        viewModelScope.launch {
            activityRepo.stopAll()                       // pause any running
            activityRepo.updateTimer(id, getCurrentElapsed(id), running = true)
        }
    }

    fun pauseActivity(id: Int) {
        viewModelScope.launch {
            activityRepo.updateTimer(id, getCurrentElapsed(id), running = false)
        }
    }

    fun finishActivity(id: Int) {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            val model = uiState.value.activities.firstOrNull { it.id == id } ?: return@launch
            activityRepo.updateTimer(id, model.elapsedSeconds, running = false)
            activityRepo.updateStreak(id, model.streakDays + 1, today)
        }
    }

    private fun getCurrentElapsed(id: Int): Long =
        uiState.value.activities.firstOrNull { it.id == id }?.elapsedSeconds ?: 0L
}