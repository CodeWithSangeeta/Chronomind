//package com.sangeeta.chronomind.service
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.media.Ringtone
//import android.media.RingtoneManager
//import android.os.Build
//import android.os.IBinder
//import android.os.VibrationEffect
//import android.os.Vibrator
//import com.sangeeta.chronomind.MainActivity
//import com.sangeeta.chronomind.R
//import com.sangeeta.chronomind.repository.ActivityRepository
//import androidx.core.app.NotificationCompat
//import androidx.core.content.ContextCompat
//import androidx.core.content.ContextCompat.getSystemService
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.firstOrNull
//import kotlinx.coroutines.isActive
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//@AndroidEntryPoint
//class TimerForegroundService : Service() {
//
//    @Inject
//    lateinit var activityRepo: ActivityRepository
//
//    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private var tickJob: Job? = null
//
//    companion object {
//        const val ACTION_START = "ACTION_TIMER_START"
//        const val ACTION_PAUSE = "ACTION_TIMER_PAUSE"
//        const val ACTION_STOP = "ACTION_TIMER_STOP"
//        const val ACTION_COMPLETE = "ACTION_TIMER_COMPLETE"
//
//        const val CHANNEL_ID = "chronomind_timer_channel"
//        const val NOTIF_ID = 1001
//
//        fun startIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_START }
//        fun pauseIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_PAUSE }
//        fun stopIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_STOP }
//        fun completeIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_COMPLETE }
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
//            ACTION_STOP -> pauseTicking()
//            ACTION_COMPLETE -> completeFromNotification()
//            else -> startTicking()
//        }
//        return START_STICKY
//    }
//
//    private fun startTicking() {
//        startForeground(NOTIF_ID, buildNotification("Session running").build())
//
//        tickJob?.cancel()
//        tickJob = serviceScope.launch {
//            while (isActive) {
//                val running = activityRepo.observeRunning().firstOrNull()
//
//                if (running == null) {
//                    stopServiceGracefully()
//                    break
//                }
//
//                val now = System.currentTimeMillis()
//                val elapsed = activityRepo.computeElapsedSeconds(running, now)
//                activityRepo.updateElapsedSnapshot(running.id, elapsed, true)
//
//                val refreshed = activityRepo.observeById(running.id).firstOrNull() ?: running
//                val displayState = activityRepo.buildDisplayState(refreshed, now)
//                updateNotification(displayState.displayTime)
//
//                val isTimer = running.targetType != "STOPWATCH"
//                val remaining = activityRepo.computeRemainingSeconds(running, now)
//                val isTimerEnded = isTimer && remaining <= 0L
//
//                if (isTimerEnded) {
//
//                    // Safe sequential update block: process data fully BEFORE killing service threads
//                    if (running.completionStyle == "AUTO_CHECK" || running.completionStyle == "TIMER_END") {
//                        activityRepo.completeSession(
//                            running,
//                            finalElapsed = running.targetMinutes * 60L
//                        )
//                    } else {
//                        activityRepo.pauseSession(running)
//                    }
//
//                    stopServiceGracefully()
//                    break
//                }
//
//                delay(1000L)
//            }
//        }
//    }
//
//    private fun pauseTicking() {
//        tickJob?.cancel()
//        serviceScope.launch {
//            val running = activityRepo.observeRunning().firstOrNull()
//            if (running != null) {
//                activityRepo.pauseSession(running)
//            }
//            stopServiceGracefully()
//        }
//    }
//
//    private fun completeFromNotification() {
//        tickJob?.cancel()
//        serviceScope.launch {
//            val running = activityRepo.observeRunning().firstOrNull()
//            if (running != null) {
//                val elapsed = activityRepo.computeElapsedSeconds(running)
//                activityRepo.completeSession(running, finalElapsed = elapsed)
//            }
//            stopServiceGracefully()
//        }
//    }
//
//    private suspend fun stopServiceGracefully() {
//        withContext(Dispatchers.Main) {
//            stopForeground(STOP_FOREGROUND_REMOVE)
//            stopSelf()
//        }
//    }
//
//    private fun buildNotification(contentText: String): NotificationCompat.Builder {
//        val openAppIntent = PendingIntent.getActivity(
//            this, 0, Intent(this, MainActivity::class.java),
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val pausePendingIntent = PendingIntent.getService(
//            this, 1, pauseIntent(this),
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val completePendingIntent = PendingIntent.getService(
//            this, 2, completeIntent(this),
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        return NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("ChronoMind Focus Session")
//            .setContentText(contentText)
//            .setSmallIcon(R.drawable.app_logo)
//            .setOngoing(true)
//            .setContentIntent(openAppIntent)
//            .addAction(R.drawable.app_logo, "Pause", pausePendingIntent)
//            .addAction(R.drawable.app_logo, "Mark Complete", completePendingIntent)
//    }
//
//    private fun updateNotification(timeText: String) {
//        getSystemService(NotificationManager::class.java)?.notify(NOTIF_ID, buildNotification(timeText).build())
//    }
//
//    private fun createNotificationChannel() {
//        val channel = NotificationChannel(
//            CHANNEL_ID, "Timer", NotificationManager.IMPORTANCE_LOW
//        ).apply {
//            description = "Shows your active focus session timer"
//        }
//        getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
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
//}
//




package com.sangeeta.chronomind.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
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
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class TimerForegroundService : Service() {

    @Inject
    lateinit var activityRepo: ActivityRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var tickJob: Job? = null
    private var alertJob: Job? = null
    private var alertMediaPlayer: MediaPlayer? = null

    companion object {
        const val ACTION_START = "ACTION_TIMER_START"
        const val ACTION_PAUSE = "ACTION_TIMER_PAUSE"
        const val ACTION_STOP = "ACTION_TIMER_STOP"
        const val ACTION_COMPLETE = "ACTION_TIMER_COMPLETE"
        const val ACTION_DISMISS_ALERT = "ACTION_TIMER_DISMISS_ALERT"

        const val CHANNEL_ID = "chronomind_timer_channel"
        const val ALERT_CHANNEL_ID = "chronomind_timer_alert_channel"
        const val NOTIF_ID = 1001

        private const val ALERT_MAX_DURATION_MS = 90_000L

        fun startIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_START }
        fun pauseIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_PAUSE }
        fun stopIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_STOP }
        fun completeIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_COMPLETE }
        fun dismissAlertIntent(ctx: Context) = Intent(ctx, TimerForegroundService::class.java).apply { action = ACTION_DISMISS_ALERT }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // IMPORTANT: with START_STICKY, if this service is killed and restarted by the
        // system, Android redelivers a NULL intent (not the original one). We must NOT
        // silently re-run startTicking() in that case -- there may be nothing to resume,
        // and doing so blindly is what causes crash-loops. Just let the service exit
        // cleanly; the user can resume from the Home screen.
        if (intent == null) {
            stopServiceGracefullySync()
            return START_NOT_STICKY
        }

        when (intent.action) {
            ACTION_START -> startTicking()
            ACTION_PAUSE -> pauseTicking()
            ACTION_STOP -> pauseTicking()
            ACTION_COMPLETE -> completeFromNotification()
            ACTION_DISMISS_ALERT -> dismissAlertAndStop()
            else -> startTicking()
        }
        return START_NOT_STICKY
    }

    private fun startTicking() {
        stopCompletionAlert()

        startForeground(NOTIF_ID, buildRunningNotification("Session running").build())

        tickJob?.cancel()
        tickJob = serviceScope.launch {
            while (isActive) {
                val running = activityRepo.observeRunning().firstOrNull()

                if (running == null) {
                    stopServiceGracefully()
                    break
                }

                val now = System.currentTimeMillis()
                val elapsed = activityRepo.computeElapsedSeconds(running, now)
                activityRepo.updateElapsedSnapshot(running.id, elapsed, true)

                val refreshed = activityRepo.observeById(running.id).firstOrNull() ?: running
                val displayState = activityRepo.buildDisplayState(refreshed, now)
                updateRunningNotification(displayState.displayTime)

                val isTimer = running.targetType != "STOPWATCH"
                val remaining = activityRepo.computeRemainingSeconds(running, now)
                val isTimerEnded = isTimer && remaining <= 0L

                if (isTimerEnded) {
                    if (running.completionStyle == "AUTO_CHECK" || running.completionStyle == "TIMER_END") {
                        activityRepo.completeSession(
                            running,
                            finalElapsed = running.targetMinutes * 60L
                        )
                    } else {
                        activityRepo.pauseSession(running)
                    }

                    triggerCompletionAlert(running.name)
                    break
                }

                delay(1000L)
            }
        }
    }

    private fun pauseTicking() {
        tickJob?.cancel()
        stopCompletionAlert()
        serviceScope.launch {
            val running = activityRepo.observeRunning().firstOrNull()
            if (running != null) {
                activityRepo.pauseSession(running)
            }
            stopServiceGracefully()
        }
    }

    private fun completeFromNotification() {
        tickJob?.cancel()
        stopCompletionAlert()
        serviceScope.launch {
            val running = activityRepo.observeRunning().firstOrNull()
            if (running != null) {
                val elapsed = activityRepo.computeElapsedSeconds(running)
                activityRepo.completeSession(running, finalElapsed = elapsed)
            }
            stopServiceGracefully()
        }
    }

    private fun triggerCompletionAlert(activityName: String) {
        startForeground(NOTIF_ID, buildCompletionNotification(activityName).build())
        playAlarmSound()
        vibrateLoop()

        alertJob?.cancel()
        alertJob = serviceScope.launch {
            delay(ALERT_MAX_DURATION_MS)
            dismissAlertAndStop()
        }
    }

    private fun dismissAlertAndStop() {
        stopCompletionAlert()
        serviceScope.launch { stopServiceGracefully() }
    }

    private fun stopCompletionAlert() {
        alertJob?.cancel()
        alertJob = null
        alertMediaPlayer?.let {
            runCatching {
                if (it.isPlaying) it.stop()
                it.release()
            }
        }
        alertMediaPlayer = null
        // Safe: no-op if vibrator service isn't available on this device.
        runCatching { getVibratorOrNull()?.cancel() }
    }

    private fun playAlarmSound() {
        runCatching {
            val alarmUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                ?: return

            alertMediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(applicationContext, alarmUri)
                isLooping = true
                prepare()
                start()
            }
        }
    }

    private fun vibrateLoop() {
        runCatching {
            val vibrator = getVibratorOrNull() ?: return
            if (!vibrator.hasVibrator()) return

            val pattern = longArrayOf(0, 500, 300, 500, 300)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, 0)
            }
        }
    }

    /**
     * Null-safe vibrator lookup. getSystemService() CAN return null on some devices/
     * emulators -- forcing a non-null cast here is what was causing the crash on every
     * Pause/Start tap. Never force-cast a platform-type result from getSystemService().
     */
    private fun getVibratorOrNull(): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private suspend fun stopServiceGracefully() {
        withContext(Dispatchers.Main) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun stopServiceGracefullySync() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun buildRunningNotification(contentText: String): NotificationCompat.Builder {
        val openAppIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val pausePendingIntent = PendingIntent.getService(
            this, 1, pauseIntent(this),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val completePendingIntent = PendingIntent.getService(
            this, 2, completeIntent(this),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ChronoMind Focus Session")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(openAppIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Pause", pausePendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Mark Complete", completePendingIntent)
    }

    private fun buildCompletionNotification(activityName: String): NotificationCompat.Builder {
        val openAppIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val dismissPendingIntent = PendingIntent.getService(
            this, 3, dismissAlertIntent(this),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, ALERT_CHANNEL_ID)
            .setContentTitle("$activityName complete!")
            .setContentText("Tap Dismiss to stop the alert.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOngoing(false)
            .setAutoCancel(false)
            .setContentIntent(openAppIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Dismiss", dismissPendingIntent)
    }

    private fun updateRunningNotification(timeText: String) {
        getSystemService(NotificationManager::class.java)
            ?.notify(NOTIF_ID, buildRunningNotification(timeText).build())
    }

    private fun createNotificationChannels() {
        val manager = getSystemService(NotificationManager::class.java) ?: return

        val runningChannel = NotificationChannel(
            CHANNEL_ID, "Timer", NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows your active focus session timer"
        }

        val alertChannel = NotificationChannel(
            ALERT_CHANNEL_ID, "Timer Completed", NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alerts you when a focus session finishes"
            setSound(null, null)
            enableVibration(false)
        }

        manager.createNotificationChannel(runningChannel)
        manager.createNotificationChannel(alertChannel)
    }

    override fun onDestroy() {
        tickJob?.cancel()
        stopCompletionAlert()
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}