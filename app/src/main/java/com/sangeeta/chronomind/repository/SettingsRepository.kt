package com.sangeeta.chronomind.repository


import com.sangeeta.chronomind.local.datastore.SettingsDataStore
import com.sangeeta.chronomind.ui.create_activity.CompletionStyle
import com.sangeeta.chronomind.ui.create_activity.StreakBehavior
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class SettingsRepository @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) {

    val notificationsEnabled = settingsDataStore.notificationsEnabled
    val isDailyReminderEnabled = settingsDataStore.isDailyReminderEnabled
    val reminderTime = settingsDataStore.reminderTime

    val defaultCompletionStyle: Flow<CompletionStyle> =
        settingsDataStore.defaultCompletionStyle.map { stored ->
            when (stored.uppercase()) {
                "AUTO_CHECK", "TIMER_END", "TIMEREND", "AUTO" -> CompletionStyle.AUTO_CHECK
                "MANUAL_CHECK", "MANUAL" -> CompletionStyle.MANUAL_CHECK
                else -> CompletionStyle.MANUAL_CHECK
            }
        }

    val defaultStreakBehavior: Flow<StreakBehavior> =
        settingsDataStore.defaultStreakOnMiss.map { stored ->
            when (stored.uppercase()) {
                "RESET_TO_ZERO", "RESETTOZERO", "RESET" -> StreakBehavior.RESET_TO_ZERO
                "CONTINUE_STREAK", "CONTINUESTREAK", "CONTINUE" -> StreakBehavior.CONTINUE_STREAK
                else -> StreakBehavior.CONTINUE_STREAK
            }
        }

    suspend fun setNotificationsEnabled(value: Boolean) {
        settingsDataStore.setNotificationsEnabled(value)
    }

    suspend fun setDailyReminderEnabled(value: Boolean) {
        settingsDataStore.setDailyReminderEnabled(value)
    }

    suspend fun setReminderTime(value: String) {
        settingsDataStore.setReminderTime(value)
    }

    suspend fun setDefaultCompletionStyle(value: CompletionStyle) {
        settingsDataStore.setDefaultCompletionStyle(value.name)
    }

    suspend fun setDefaultStreakBehavior(value: StreakBehavior) {
        settingsDataStore.setDefaultStreakOnMiss(value.name)
    }


    suspend fun seedDefaultsIfMissing(
        completionStyle: CompletionStyle,
        streakBehavior: StreakBehavior
    ) {
        settingsDataStore.seedDefaultsIfMissing(
            completionStyle = completionStyle.name,
            streakOnMiss = streakBehavior.name
        )
    }

}