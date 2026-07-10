package com.sangeeta.chronomind.repository

import com.sangeeta.chronomind.local.datastore.OnboardingDataStore
import com.sangeeta.chronomind.ui.create_activity.CompletionStyle
import com.sangeeta.chronomind.ui.create_activity.StreakBehavior
import kotlinx.coroutines.flow.first
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

        syncSettingsFromOnboarding(checkIn, streakMiss)
    }

    suspend fun resetOnboarding() = onboardingDataStore.resetOnboarding()

    suspend fun setCheckInStyle(value: String) {
        onboardingDataStore.setCheckInStyle(value)
        syncSettingsFromOnboarding(value, onboardingDataStore.streakOnMiss.first())
    }

    suspend fun setStreakOnMiss(value: String) {
        onboardingDataStore.setStreakOnMiss(value)
        syncSettingsFromOnboarding(onboardingDataStore.checkInStyle.first(), value)
    }

    private suspend fun syncSettingsFromOnboarding(
        checkIn: String,
        streakMiss: String
    ) {
        val completionStyle = when (checkIn.uppercase()) {
            "AUTOCHECK", "AUTO_CHECK", "AUTO", "TIMEREND", "TIMER_END" ->
                CompletionStyle.AUTO_CHECK
            else ->
                CompletionStyle.MANUAL_CHECK
        }

        val streakBehavior = when (streakMiss.uppercase()) {
            "RESET", "RESETTOZERO", "RESET_TO_ZERO" ->
                StreakBehavior.RESET_TO_ZERO
            else ->
                StreakBehavior.CONTINUE_STREAK
        }

        settingsRepository.setDefaultCompletionStyle(completionStyle)
        settingsRepository.setDefaultStreakBehavior(streakBehavior)
    }

}