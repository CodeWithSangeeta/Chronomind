package com.sangeeta.chronomind.ui.onboarding

data class OnboardingState(
    val currentStep: Int = 0,
    val selectedFocusAreas: Set<FocusArea> = emptySet(),
    val selectedAccountability: AccountabilityType? = null,
    val selectedCheckInStyle: CheckInStyle? = null,
    val selectedStreakMissChoice: StreakMissChoice? = null,
    val isFinished: Boolean = false
)