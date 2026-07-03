package com.sangeeta.chronomind

import android.app.Application
import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ChronoMindApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        com.sangeeta.chronomind.worker.StreakResetWorker.schedule(this)
        createReminderNotificationChannel()
    }

    private fun createReminderNotificationChannel() {
        val manager = getSystemService(android.app.NotificationManager::class.java)
        val channel = android.app.NotificationChannel(
            "chronomind_reminder_channel",
            "Daily reminders",
            android.app.NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Daily focus reminders"
        }
        manager.createNotificationChannel(channel)
    }
}