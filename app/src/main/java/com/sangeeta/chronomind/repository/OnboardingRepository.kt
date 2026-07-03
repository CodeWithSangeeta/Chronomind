package com.sangeeta.chronomind.repository

import com.sangeeta.chronomind.local.datastore.OnboardingDataStore
import com.sangeeta.chronomind.ui.create_activity.CompletionStyle
import com.sangeeta.chronomind.ui.create_activity.StreakBehavior
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class OnboardingRepository @Inject constructor(
    private val onboardingDataStore: OnboardingDataStore,
    private val settingsRepository: SettingsRepository
) {
    val isOnboardingComplete = onboardingDataStore.isOnboardingComplete
    val accountabilityType = onboardingDataStore.accountabilityType
    val checkInStyle = onboardingDataStore.checkInStyle
    val streakOnMiss = onboardingDataStore.streakOnMiss
    val userName = onboardingDataStore.userName

    suspend fun completeOnboarding(
        name: String,
        accountability: String,
        checkIn: String,
        streakMiss: String
    ) {
        onboardingDataStore.completeOnboarding(
            name = name,
            accountability = accountability,
            checkIn = checkIn,
            streakMiss = streakMiss
        )

        settingsRepository.seedDefaultsIfMissing(
            completionStyle = when (checkIn) {
                "AUTO", "TIMER_END" -> CompletionStyle.AUTO_CHECK
                else -> CompletionStyle.MANUAL_CHECK
            },
            streakBehavior = when (streakMiss) {
                "RESET", "RESET_TO_ZERO" -> StreakBehavior.RESET_TO_ZERO
                else -> StreakBehavior.CONTINUE_STREAK
            }
        )
    }

    suspend fun resetOnboarding() = onboardingDataStore.resetOnboarding()
    suspend fun setCheckInStyle(value: String) = onboardingDataStore.setCheckInStyle(value)
    suspend fun setStreakOnMiss(value: String) = onboardingDataStore.setStreakOnMiss(value)
}