package com.sangeeta.chronomind.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.sangeeta.chronomind.MainActivity
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.repository.ActivityRepository
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TimerForegroundService : Service() {

    @Inject
    lateinit var activityRepo: ActivityRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var tickJob: Job? = null

    companion object {
        const val ACTION_START = "ACTION_TIMER_START"
        const val ACTION_PAUSE = "ACTION_TIMER_PAUSE"
        const val ACTION_STOP = "ACTION_TIMER_STOP"
        const val ACTION_COMPLETE = "ACTION_TIMER_COMPLETE"

        const val CHANNEL_ID = "chronomind_timer_channel"
        const val NOTIF_ID = 1001

        fun startIntent(ctx: Context) =
            Intent(ctx, TimerForegroundService::class.java).apply {
                action = ACTION_START
            }

        fun pauseIntent(ctx: Context) =
            Intent(ctx, TimerForegroundService::class.java).apply {
                action = ACTION_PAUSE
            }

        fun stopIntent(ctx: Context) =
            Intent(ctx, TimerForegroundService::class.java).apply {
                action = ACTION_STOP
            }

        fun completeIntent(ctx: Context) =
            Intent(ctx, TimerForegroundService::class.java).apply {
                action = ACTION_COMPLETE
            }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startTicking()
            ACTION_PAUSE -> pauseTicking()
            ACTION_STOP -> pauseTicking()
            ACTION_COMPLETE -> completeFromNotification()
            else -> startTicking()
        }
        return START_STICKY
    }

    private fun startTicking() {
        startForeground(NOTIF_ID, buildNotification("Session running").build())

        tickJob?.cancel()
        tickJob = serviceScope.launch {
            while (isActive) {
                val running = activityRepo.observeRunning().firstOrNull()

                if (running == null) {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                    break
                }

                val now = System.currentTimeMillis()
                val elapsed = activityRepo.computeElapsedSeconds(running, now)
                activityRepo.updateElapsedSnapshot(running.id, elapsed, true)

                val refreshed = activityRepo.observeById(running.id).firstOrNull() ?: running
                val displayState = activityRepo.buildDisplayState(refreshed, now)
                updateNotification(displayState.displayTime)

                val isTimer = running.targetType != "STOPWATCH"
                val remaining = activityRepo.computeRemainingSeconds(running, now)
                val shouldAutoFinish = isTimer && remaining <= 0L

                if (shouldAutoFinish) {
                    activityRepo.completeSession(running, finalElapsed = elapsed)
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                    break
                }

                delay(1000L)
            }
        }
    }

    private fun pauseTicking() {
        tickJob?.cancel()

        serviceScope.launch {
            val running = activityRepo.observeRunning().firstOrNull()
            if (running != null) {
                activityRepo.pauseSession(running)
            }
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun completeFromNotification() {
        tickJob?.cancel()

        serviceScope.launch {
            val running = activityRepo.observeRunning().firstOrNull()
            if (running != null) {
                val elapsed = activityRepo.computeElapsedSeconds(running)
                activityRepo.completeSession(running, finalElapsed = elapsed)
            }
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun buildNotification(contentText: String): NotificationCompat.Builder {
        val openAppIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val pausePendingIntent = PendingIntent.getService(
            this,
            1,
            pauseIntent(this),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val completePendingIntent = PendingIntent.getService(
            this,
            2,
            completeIntent(this),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ChronoMind Focus Session")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setContentIntent(openAppIntent)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Pause",
                pausePendingIntent
            )
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Mark Complete",
                completePendingIntent
            )
    }

    private fun updateNotification(timeText: String) {
        getSystemService(NotificationManager::class.java)
            .notify(NOTIF_ID, buildNotification(timeText).build())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Timer",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows your active focus session timer"
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    override fun onDestroy() {
        tickJob?.cancel()
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}