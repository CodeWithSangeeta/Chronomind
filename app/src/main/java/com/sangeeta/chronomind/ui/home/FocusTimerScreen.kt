package com.sangeeta.chronomind.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Full screen wrapper — integrates ViewModel + FocusTimerCard.
 * Drop this into your NavGraph as a Composable destination.
 */
@Composable
fun FocusTimerScreen(
    viewModel: FocusTimerViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)),
        contentAlignment = Alignment.Center
    ) {
        FocusTimerCard(
            totalSeconds     = state.totalSeconds,
            remainingSeconds = state.remainingSeconds,
            sessionMode      = state.sessionMode,
            targetMinutes    = state.targetMinutes,
            streakDays       = state.streakDays,
            completedToday   = state.completedToday,
            totalToday       = state.totalToday,
            onPause  = { if (state.isRunning) viewModel.pause() else viewModel.resume() },
            onFinish = { viewModel.finish() },
            onSwitch = { viewModel.switchMode("Reading", 30) }
        )
    }
}


