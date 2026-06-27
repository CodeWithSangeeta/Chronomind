package com.sangeeta.chronomind.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FocusTimerState(
    val totalSeconds: Int = 45 * 60,
    val remainingSeconds: Int = 45 * 60,
    val isRunning: Boolean = true,
    val sessionMode: String = "Deep Work",
    val targetMinutes: Int = 45,
    val streakDays: Int = 12,
    val completedToday: Int = 1,
    val totalToday: Int = 2
)

class FocusTimerViewModel : ViewModel() {

    private val _state = MutableStateFlow(FocusTimerState())
    val state: StateFlow<FocusTimerState> = _state.asStateFlow()

    private var timerJob: Job? = null

    init { startTimer() }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_state.value.remainingSeconds > 0 && _state.value.isRunning) {
                delay(1000L)
                _state.value = _state.value.copy(
                    remainingSeconds = (_state.value.remainingSeconds - 1).coerceAtLeast(0)
                )
            }
            if (_state.value.remainingSeconds == 0) {
                _state.value = _state.value.copy(
                    isRunning = false,
                    completedToday = _state.value.completedToday + 1
                )
            }
        }
    }

    fun pause() {
        _state.value = _state.value.copy(isRunning = false)
        timerJob?.cancel()
    }

    fun resume() {
        _state.value = _state.value.copy(isRunning = true)
        startTimer()
    }

    fun finish() {
        timerJob?.cancel()
        _state.value = _state.value.copy(
            remainingSeconds = 0,
            isRunning = false,
            completedToday = _state.value.completedToday + 1
        )
    }

    fun reset() {
        timerJob?.cancel()
        _state.value = _state.value.copy(
            remainingSeconds = _state.value.totalSeconds,
            isRunning = true
        )
        startTimer()
    }

    fun switchMode(mode: String, durationMinutes: Int) {
        timerJob?.cancel()
        _state.value = _state.value.copy(
            sessionMode = mode,
            totalSeconds = durationMinutes * 60,
            remainingSeconds = durationMinutes * 60,
            targetMinutes = durationMinutes,
            isRunning = true
        )
        startTimer()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}