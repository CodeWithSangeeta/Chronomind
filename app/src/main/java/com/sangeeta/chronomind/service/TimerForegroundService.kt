package com.sangeeta.chronomind.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.sangeeta.chronomind.MainActivity
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.repository.ActivityRepository
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

        const val CHANNEL_ID = "chronomind_timer_channel"
        const val NOTIF_ID = 1001

        fun startIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply {
            action = ACTION_START
        }

        fun pauseIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply {
            action = ACTION_PAUSE
        }

        // Domain rule: STOP from notification behaves same as pause.
        fun stopIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply {
            action = ACTION_STOP
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
            else -> startTicking()
        }
        return START_STICKY
    }

    private fun startTicking() {
        startForeground(NOTIF_ID, buildNotification("Session running"))

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
                activityRepo.updateElapsedSnapshot(running.id, elapsed, running = true)

                val isStopwatch = running.targetType == "STOPWATCH"
                if (isStopwatch) {
                    updateNotification(formatTime(elapsed))
                } else {
                    val remaining = activityRepo.computeRemainingSeconds(running, now)
                    updateNotification(formatTime(remaining))

                    val isAutoFinishTimer = running.completionStyle == "TIMER_END" || running.completionStyle == "TIMEREND"
                    if (isAutoFinishTimer && remaining <= 0L) {
                        activityRepo.completeSession(running, finalElapsed = elapsed)
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()
                        break
                    }
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

    private fun formatTime(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return if (h > 0) {
            String.format("%02d:%02d:%02d", h, m, s)
        } else {
            String.format("%02d:%02d", m, s)
        }
    }

    private fun buildNotification(contentText: String) =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ChronoMind Focus Session")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

    private fun updateNotification(timeText: String) {
        getSystemService(NotificationManager::class.java)
            .notify(NOTIF_ID, buildNotification(timeText))
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