//package com.sangeeta.chronomind.local.datastore
//
//
//import android.content.Context
//import androidx.datastore.preferences.core.booleanPreferencesKey
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.emptyPreferences
//import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.map
//import javax.inject.Inject
//import javax.inject.Singleton
//
//private val Context.settingsDataStore by preferencesDataStore(name = "chronomind_settings")
//
//@Singleton
//class SettingsDataStore @Inject constructor(
//    @ApplicationContext private val context: Context
//) {
//    private val store = context.settingsDataStore
//
//    companion object {
//        val KEY_SOUND_ENABLED       = booleanPreferencesKey("sound_enabled")
//        val KEY_VIBRATION_ENABLED   = booleanPreferencesKey("vibration_enabled")
//        val KEY_KEEP_SCREEN_ON      = booleanPreferencesKey("keep_screen_on")
//        val KEY_DAILY_REMINDER      = booleanPreferencesKey("daily_reminder")
//        val KEY_REMINDER_TIME       = stringPreferencesKey("reminder_time")
//        val KEY_THEME               = stringPreferencesKey("theme") // DARK / LIGHT / SYSTEM
//    }
//
//    val isSoundEnabled: Flow<Boolean> = store.data
//        .catch { emit(emptyPreferences()) }
//        .map { it[KEY_SOUND_ENABLED] ?: true }
//
//    val isVibrationEnabled: Flow<Boolean> = store.data
//        .catch { emit(emptyPreferences()) }
//        .map { it[KEY_VIBRATION_ENABLED] ?: true }
//
//    val isKeepScreenOn: Flow<Boolean> = store.data
//        .catch { emit(emptyPreferences()) }
//        .map { it[KEY_KEEP_SCREEN_ON] ?: true }
//
//    val isDailyReminderEnabled: Flow<Boolean> = store.data
//        .catch { emit(emptyPreferences()) }
//        .map { it[KEY_DAILY_REMINDER] ?: false }
//
//    val reminderTime: Flow<String> = store.data
//        .catch { emit(emptyPreferences()) }
//        .map { it[KEY_REMINDER_TIME] ?: "07:00 AM" }
//
//    val theme: Flow<String> = store.data
//        .catch { emit(emptyPreferences()) }
//        .map { it[KEY_THEME] ?: "DARK" }
//
//    suspend fun setSoundEnabled(value: Boolean) = store.edit { it[KEY_SOUND_ENABLED] = value }
//    suspend fun setVibrationEnabled(value: Boolean) = store.edit { it[KEY_VIBRATION_ENABLED] = value }
//    suspend fun setKeepScreenOn(value: Boolean) = store.edit { it[KEY_KEEP_SCREEN_ON] = value }
//    suspend fun setDailyReminder(value: Boolean) = store.edit { it[KEY_DAILY_REMINDER] = value }
//    suspend fun setReminderTime(value: String) = store.edit { it[KEY_REMINDER_TIME] = value }
//    suspend fun setTheme(value: String) = store.edit { it[KEY_THEME] = value }
//}