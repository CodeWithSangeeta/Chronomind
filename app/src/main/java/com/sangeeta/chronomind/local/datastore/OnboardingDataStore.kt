package com.sangeeta.chronomind.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = OnboardingPreferences.DATASTORE_NAME
)

@Singleton
class OnboardingDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val store = context.dataStore

    private val keyOnboardingDone  = booleanPreferencesKey(OnboardingPreferences.KEY_ONBOARDING_COMPLETE)
    private val keyAccountability  = stringPreferencesKey(OnboardingPreferences.KEY_ACCOUNTABILITY_TYPE)
    private val keyCheckInStyle    = stringPreferencesKey(OnboardingPreferences.KEY_CHECK_IN_STYLE)
    private val keyStreakOnMiss    = stringPreferencesKey(OnboardingPreferences.KEY_STREAK_ON_MISS)
    private val keyUserName        = stringPreferencesKey(OnboardingPreferences.KEY_USER_NAME)

    val isOnboardingComplete: Flow<Boolean> = store.data
        .catch { emit(emptyPreferences()) }
        .map { it[keyOnboardingDone] ?: false }

    val accountabilityType: Flow<String> = store.data
        .catch { emit(emptyPreferences()) }
        .map { it[keyAccountability] ?: "STREAKS" }

    val checkInStyle: Flow<String> = store.data
        .catch { emit(emptyPreferences()) }
        .map { it[keyCheckInStyle] ?: "MANUAL" }

    val streakOnMiss: Flow<String> = store.data
        .catch { emit(emptyPreferences()) }
        .map { it[keyStreakOnMiss] ?: "CONTINUE" }

    val userName: Flow<String> = store.data
        .catch { emit(emptyPreferences()) }
        .map { it[keyUserName] ?: "" }

    suspend fun completeOnboarding(
        name: String,
        accountability: String,
        checkIn: String,
        streakMiss: String
    ) {
        store.edit { prefs ->
            prefs[keyOnboardingDone] = true
            prefs[keyUserName]       = name
            prefs[keyAccountability] = accountability
            prefs[keyCheckInStyle]   = checkIn
            prefs[keyStreakOnMiss]   = streakMiss
        }
    }

    suspend fun resetOnboarding() {
        store.edit { it.clear() }
    }
}