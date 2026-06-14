package com.sangeeta.chronomind.repository

import com.sangeeta.chronomind.local.datastore.OnboardingDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingRepository @Inject constructor(
    private val dataStore: OnboardingDataStore
) {
    val isOnboardingComplete: Flow<Boolean> = dataStore.isOnboardingComplete
    val userName: Flow<String>              = dataStore.userName
    val accountabilityType: Flow<String>    = dataStore.accountabilityType
    val checkInStyle: Flow<String>          = dataStore.checkInStyle
    val streakOnMiss: Flow<String>          = dataStore.streakOnMiss

    suspend fun saveOnboardingResult(
        name: String,
        accountability: String,
        checkIn: String,
        streakMiss: String
    ) = dataStore.completeOnboarding(name, accountability, checkIn, streakMiss)

    suspend fun resetOnboarding() = dataStore.resetOnboarding()
}