package com.sangeeta.chronomind.ui.onboarding

import androidx.activity.compose.BackHandler
import com.sangeeta.chronomind.ui.onboarding.screens.IntroScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import com.sangeeta.chronomind.ui.onboarding.screens.AccountabilityScreen
import com.sangeeta.chronomind.ui.onboarding.screens.CheckInStyleScreen
import com.sangeeta.chronomind.ui.onboarding.screens.FocusAreaScreen
import com.sangeeta.chronomind.ui.onboarding.screens.MeetAuraScreen
import com.sangeeta.chronomind.ui.onboarding.screens.ReadyScreen
import com.sangeeta.chronomind.ui.onboarding.screens.SpaceSummaryScreen
import com.sangeeta.chronomind.ui.onboarding.screens.StreakMissScreen
import com.sangeeta.chronomind.ui.onboarding.screens.TimerPreviewScreen
import com.sangeeta.chronomind.ui.onboarding.screens.WidgetPreviewScreen
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun OnboardingNavHost(
    onNavigateToMain: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler(enabled = state.currentStep > 0) {
        viewModel.previousStep()
    }
    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                OnboardingNavEvent.NavigateToMain -> onNavigateToMain()
                OnboardingNavEvent.NextStep -> Unit
                OnboardingNavEvent.PreviousStep -> Unit
            }
        }
    }

    when (state.currentStep) {
        0 -> IntroScreen(
            onGetStarted = { viewModel.nextStep() },
            onSkip = { viewModel.finishOnboarding() }
        )

        1 -> MeetAuraScreen(
            onLetBegin = { viewModel.nextStep() }
        )

        2 -> FocusAreaScreen(
            selectedAreas = state.selectedFocusAreas,
            onToggleArea = {area -> viewModel.toggleFocusArea(area)},
            onContinue = { viewModel.nextStep() },
            currentStep = 0,
            totalSteps = 8
        )

        3 -> AccountabilityScreen(
            selected = state.selectedAccountability,
            onSelect ={ type -> viewModel.selectAccountability(type)},
            onContinue = { viewModel.nextStep() },
            currentStep = 1,
            totalSteps = 8
        )

        4 -> CheckInStyleScreen(
            selected = state.selectedCheckInStyle,
            onSelect = { style -> viewModel.selectCheckInStyle(style) },
            onContinue = { viewModel.nextStep() },
            currentStep = 2,
            totalSteps = 8
        )

        5 -> StreakMissScreen(
            selected = state.selectedStreakMissChoice,
            onSelect = { choice -> viewModel.selectStreakMissChoice(choice)},
            onContinue = { viewModel.nextStep() },
            currentStep = 3,
            totalSteps = 8
        )

        6 -> TimerPreviewScreen(
            onContinue = { viewModel.nextStep() },
            currentStep = 4,
            totalSteps = 8
        )

        7 -> WidgetPreviewScreen(
            onContinue = { viewModel.nextStep() },
            currentStep = 5,
            totalSteps = 8
        )

        8 -> SpaceSummaryScreen(
            focusAreas = viewModel.getFocusAreaLabels(),
            accountabilityLabel = viewModel.getAccountabilityLabel(),
            checkInLabel = viewModel.getCheckInLabel(),
            streakMissLabel = viewModel.getStreakMissLabel(),
            onCreateSpace = { viewModel.nextStep() },
            currentStep = 6,
            totalSteps = 8
        )

        9 -> ReadyScreen(
            onCreateMySpace = { viewModel.finishOnboarding() }
        )

        else -> IntroScreen(
            onGetStarted = { viewModel.nextStep() },
            onSkip = { viewModel.finishOnboarding() }
        )
    }
}