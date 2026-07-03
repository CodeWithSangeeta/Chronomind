package com.sangeeta.chronomind.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "chronomind_settings"
)

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val store = context.settingsDataStore

    private object Keys {
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val DAILY_REMINDER_ENABLED = booleanPreferencesKey("daily_reminder_enabled")
        val REMINDER_TIME = stringPreferencesKey("reminder_time")
        val DEFAULT_COMPLETION_STYLE = stringPreferencesKey("default_completion_style")
        val DEFAULT_STREAK_ON_MISS = stringPreferencesKey("default_streak_on_miss")

    }

    val notificationsEnabled: Flow<Boolean> =
        store.data
            .catch { emit(emptyPreferences()) }
            .map { prefs -> prefs[Keys.NOTIFICATIONS_ENABLED] ?: false }

    suspend fun setNotificationsEnabled(value: Boolean) {
        store.edit { prefs -> prefs[Keys.NOTIFICATIONS_ENABLED] = value }
    }

    val isDailyReminderEnabled: Flow<Boolean> =
        store.data
            .catch { emit(emptyPreferences()) }
            .map { prefs -> prefs[Keys.DAILY_REMINDER_ENABLED] ?: false }

    suspend fun setDailyReminderEnabled(value: Boolean) {
        store.edit { prefs -> prefs[Keys.DAILY_REMINDER_ENABLED] = value }
    }
    val reminderTime: Flow<String> =
        store.data
            .catch { emit(emptyPreferences()) }
            .map { prefs -> prefs[Keys.REMINDER_TIME] ?: "07:00 AM" }



    suspend fun setReminderTime(value: String) {
        store.edit { prefs ->
            prefs[Keys.REMINDER_TIME] = value
        }
    }

    val defaultCompletionStyle: Flow<String> = store.data
        .catch { emit(emptyPreferences()) }
        .map { prefs -> prefs[Keys.DEFAULT_COMPLETION_STYLE] ?: "MANUAL" }

    suspend fun setDefaultCompletionStyle(value: String) {
        store.edit { prefs -> prefs[Keys.DEFAULT_COMPLETION_STYLE] = value }
    }

    val defaultStreakOnMiss: Flow<String> = store.data
        .catch { emit(emptyPreferences()) }
        .map { prefs -> prefs[Keys.DEFAULT_STREAK_ON_MISS] ?: "CONTINUE" }

    suspend fun setDefaultStreakOnMiss(value: String) {
        store.edit { prefs -> prefs[Keys.DEFAULT_STREAK_ON_MISS] = value }
    }

    suspend fun seedDefaultsIfMissing(
        completionStyle: String,
        streakOnMiss: String
    ) {
        store.edit { prefs ->
            if (prefs[Keys.DEFAULT_COMPLETION_STYLE] == null) {
                prefs[Keys.DEFAULT_COMPLETION_STYLE] = completionStyle
            }
            if (prefs[Keys.DEFAULT_STREAK_ON_MISS] == null) {
                prefs[Keys.DEFAULT_STREAK_ON_MISS] = streakOnMiss
            }
        }
    }

    suspend fun reset() {
        store.edit { prefs ->
            prefs.clear()
        }
    }
}