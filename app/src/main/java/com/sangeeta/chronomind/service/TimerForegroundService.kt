//package com.sangeeta.chronomind.service
//
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.os.IBinder
//import androidx.core.app.NotificationCompat
//import com.sangeeta.chronomind.MainActivity
//import com.sangeeta.chronomind.R
//import com.sangeeta.chronomind.repository.ActivityRepository
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.firstOrNull
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class TimerForegroundService : Service() {
//
//    @Inject lateinit var activityRepo: ActivityRepository
//
//    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private var tickJob: Job? = null
//
//    companion object {
//        const val ACTION_START  = "ACTION_TIMER_START"
//        const val ACTION_PAUSE  = "ACTION_TIMER_PAUSE"
//        const val ACTION_STOP   = "ACTION_TIMER_STOP"
//        const val CHANNEL_ID    = "chronomind_timer_channel"
//        const val NOTIF_ID      = 1001
//
//        fun startIntent(ctx: Context)  = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_START }
//        fun pauseIntent(ctx: Context)  = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_PAUSE }
//        fun stopIntent(ctx: Context)   = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_STOP }
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        createNotificationChannel()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        when (intent?.action) {
//            ACTION_START -> startTicking()
//            ACTION_PAUSE -> pauseTicking()
//            ACTION_STOP  -> stopSelf()
//        }
//        return START_STICKY  // Restarts service if killed by OS
//    }
//
//    private fun startTicking() {
//        startForeground(NOTIF_ID, buildNotification("Session running…"))
//        tickJob?.cancel()
//        tickJob = serviceScope.launch {
//            while (isActive) {
//                val running = activityRepo.observeRunning().firstOrNull()
//                if (running == null) { stopSelf(); break }
//
//                val newElapsed = running.elapsedSeconds + 1
//                activityRepo.updateTimer(running.id, newElapsed, true)
//
//                // Update notification text every tick
//                updateNotification(formatTime(newElapsed))
//                delay(1000L)
//            }
//        }
//    }
//
//    private fun pauseTicking() {
//        tickJob?.cancel()
//        stopForeground(STOP_FOREGROUND_REMOVE)
//        stopSelf()
//    }
//
//    private fun formatTime(seconds: Long): String {
//        val h = seconds / 3600
//        val m = (seconds % 3600) / 60
//        val s = seconds % 60
//        return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
//    }
//
//    private fun buildNotification(contentText: String) =
//        NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("ChronoMind – Focus Session")
//            .setContentText(contentText)
//            .setSmallIcon(R.drawable.ic_launcher_foreground) // use your app icon
//            .setOngoing(true)
//            .setContentIntent(
//                PendingIntent.getActivity(
//                    this, 0,
//                    Intent(this, MainActivity::class.java),
//                    PendingIntent.FLAG_IMMUTABLE
//                )
//            )
//            .build()
//
//    private fun updateNotification(timeText: String) {
//        val manager = getSystemService(NotificationManager::class.java)
//        manager.notify(NOTIF_ID, buildNotification(timeText))
//    }
//
//    private fun createNotificationChannel() {
//        val channel = NotificationChannel(
//            CHANNEL_ID,
//            "Timer",
//            NotificationManager.IMPORTANCE_LOW  // LOW = silent, no sound, but persistent
//        ).apply { description = "Shows your active focus session timer" }
//        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
//    }
//
//    override fun onDestroy() {
//        tickJob?.cancel()
//        serviceScope.cancel()
//        super.onDestroy()
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//}




//package com.sangeeta.chronomind.service
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.os.IBinder
//import androidx.core.app.NotificationCompat
//import com.sangeeta.chronomind.MainActivity
//import com.sangeeta.chronomind.R
//import com.sangeeta.chronomind.local.db.entity.ActivityEntity
//import com.sangeeta.chronomind.repository.ActivityRepository
//import com.sangeeta.chronomind.ui.model.ActivityUiModel
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.firstOrNull
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class TimerForegroundService : Service() {
//
//    @Inject lateinit var activityRepo: ActivityRepository
//
//    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private var tickJob: Job? = null
//
//    companion object {
//        const val ACTION_START  = "ACTION_TIMER_START"
//        const val ACTION_PAUSE  = "ACTION_TIMER_PAUSE"
//        const val ACTION_STOP   = "ACTION_TIMER_STOP"
//        const val CHANNEL_ID    = "chronomind_timer_channel"
//        const val NOTIF_ID      = 1001
//
//        fun startIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_START }
//        fun pauseIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_PAUSE }
//        fun stopIntent(ctx:  Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_STOP }
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        createNotificationChannel()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        when (intent?.action) {
//            ACTION_START -> startTicking()
//            ACTION_PAUSE -> pauseTicking()
//            ACTION_STOP  -> stopSelf()
//        }
//        return START_STICKY
//    }
//
//    private fun startTicking() {
//        startForeground(NOTIF_ID, buildNotification("Session running"))
//        tickJob?.cancel()
//        tickJob = serviceScope.launch {
//            while (isActive) {
//                val running = activityRepo.observeRunning().firstOrNull()
//                if (running == null) {
//                    stopSelf()
//                    break
//                }
//
//                val newElapsed = running.elapsedSeconds + 1
//                activityRepo.updateTimer(running.id, newElapsed, true)
//                updateNotification(formatTime(newElapsed))
//
//                // FIX: completionStyle == TIMER_END → auto-finish when elapsed >= target
//                if (running.completionStyle == "TIMER_END") {
//                    val targetSeconds = running.targetMinutes * 60L
//                    if (newElapsed >= targetSeconds) {
//                        // Log session as completed, reset elapsed, stop timer
//                        val uiModel = running.toUiModel()
//                        activityRepo.logSession(uiModel.copy(elapsedSeconds = newElapsed), isCompleted = true)
//                        activityRepo.finishActivity(running.copy(elapsedSeconds = newElapsed))
//                        stopSelf()
//                        break
//                    }
//                }
//
//                delay(1000L)
//            }
//        }
//    }
//
//    private fun pauseTicking() {
//        tickJob?.cancel()
//        stopForeground(STOP_FOREGROUND_REMOVE)
//        stopSelf()
//    }
//
//    private fun formatTime(seconds: Long): String {
//        val h = seconds / 3600
//        val m = (seconds % 3600) / 60
//        val s = seconds % 60
//        return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
//    }
//
//    private fun buildNotification(contentText: String) =
//        NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("ChronoMind · Focus Session")
//            .setContentText(contentText)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setOngoing(true)
//            .setContentIntent(
//                PendingIntent.getActivity(
//                    this, 0,
//                    Intent(this, MainActivity::class.java),
//                    PendingIntent.FLAG_IMMUTABLE
//                )
//            )
//            .build()
//
//    private fun updateNotification(timeText: String) {
//        val manager = getSystemService(NotificationManager::class.java)
//        manager.notify(NOTIF_ID, buildNotification(timeText))
//    }
//
//    private fun createNotificationChannel() {
//        val channel = NotificationChannel(CHANNEL_ID, "Timer", NotificationManager.IMPORTANCE_LOW)
//            .apply { description = "Shows your active focus session timer" }
//        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
//    }
//
//    override fun onDestroy() {
//        tickJob?.cancel()
//        serviceScope.cancel()
//        super.onDestroy()
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//    // Local extension — mirrors mapper but avoids cross-module import in service
//    private fun ActivityEntity.toUiModel() = ActivityUiModel(
//        id = id,
//        name = name,
//        icon = icon,
//        colorHex = colorHex,
//        elapsedSeconds = elapsedSeconds,
//        targetSeconds = targetMinutes * 60L,
//        isRunning = isRunning,
//        streakDays = streakDays,
//        lastActiveDate = lastActiveDate,
//        continueOnMiss = continueStreakOnMiss
//    )
//}





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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@AndroidEntryPoint
class TimerForegroundService : Service() {

    @Inject lateinit var activityRepo: ActivityRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var tickJob: Job? = null

    companion object {
        const val ACTION_START = "ACTION_TIMER_START"
        const val ACTION_PAUSE = "ACTION_TIMER_PAUSE"
        const val ACTION_STOP  = "ACTION_TIMER_STOP"
        const val CHANNEL_ID   = "chronomind_timer_channel"
        const val NOTIF_ID     = 1001

        fun startIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply {
            action = ACTION_START
        }
        fun pauseIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply {
            action = ACTION_PAUSE
        }
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
            ACTION_STOP  -> stopSelf()
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
                    stopSelf()
                    break
                }

                val newElapsed = running.elapsedSeconds + 1
                activityRepo.updateTimer(running.id, newElapsed, true)
                updateNotification(formatTime(newElapsed))

                // Auto-finish only for TIMER mode with TIMER_END completion style
                if (running.targetType != "STOPWATCH" && running.completionStyle == "TIMER_END") {
                    val targetSeconds = running.targetMinutes * 60L
                    if (targetSeconds > 0 && newElapsed >= targetSeconds) {
                        // Log session as completed then stop
                        activityRepo.finishActivity(running)
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
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun formatTime(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return if (h > 0) "%d:%02d:%02d".format(h, m, s)
        else "%02d:%02d".format(m, s)
    }

    private fun buildNotification(contentText: String) = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("ChronoMind · Focus Session")
        .setContentText(contentText)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setOngoing(true)
        .setContentIntent(
            PendingIntent.getActivity(
                this, 0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        .build()

    private fun updateNotification(timeText: String) {
        getSystemService(NotificationManager::class.java).notify(NOTIF_ID, buildNotification(timeText))
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "Timer", NotificationManager.IMPORTANCE_LOW).apply {
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