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
            when (stored) {
                "TIMER_END" -> CompletionStyle.AUTO_CHECK
                "AUTO" -> CompletionStyle.AUTO_CHECK
                else -> CompletionStyle.MANUAL_CHECK
            }
        }

    val defaultStreakBehavior: Flow<StreakBehavior> =
        settingsDataStore.defaultStreakOnMiss.map { stored ->
            when (stored) {
                "RESET", "RESET_TO_ZERO" -> StreakBehavior.RESET_TO_ZERO
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
        settingsDataStore.setDefaultCompletionStyle(
            when (value) {
                CompletionStyle.MANUAL_CHECK -> "MANUAL"
                CompletionStyle.AUTO_CHECK -> "TIMER_END"
            }
        )
    }

    suspend fun setDefaultStreakBehavior(value: StreakBehavior) {
        settingsDataStore.setDefaultStreakOnMiss(
            when (value) {
                StreakBehavior.CONTINUE_STREAK -> "CONTINUE"
                StreakBehavior.RESET_TO_ZERO -> "RESET"
            }
        )
    }

    suspend fun seedDefaultsIfMissing(
        completionStyle: CompletionStyle,
        streakBehavior: StreakBehavior
    ) {
        settingsDataStore.seedDefaultsIfMissing(
            completionStyle = when (completionStyle) {
                CompletionStyle.MANUAL_CHECK -> "MANUAL"
                CompletionStyle.AUTO_CHECK -> "TIMER_END"
            },
            streakOnMiss = when (streakBehavior) {
                StreakBehavior.CONTINUE_STREAK -> "CONTINUE"
                StreakBehavior.RESET_TO_ZERO -> "RESET"
            }
        )
    }
}