package com.sangeeta.chronomind.worker


import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.sangeeta.chronomind.repository.ActivityRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit
import java.util.Calendar

/**
 * Runs once per day at midnight.
 * Resets streak to 0 for all activities where:
 *   - continueStreakOnMiss = false (user chose "Reset streak on miss")
 *   - lastActiveDate != today (user missed yesterday)
 *
 * Schedule this from ChronoMindApp.onCreate() by calling
 * StreakResetWorker.schedule(context)
 */
@HiltWorker
class StreakResetWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val activityRepository: ActivityRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            activityRepository.resetMissedStreaks()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "streak_reset_daily"

        /**
         * Call once from ChronoMindApp.onCreate().
         * Uses KEEP policy so it won't reschedule if already enqueued.
         * Calculates delay to next midnight so it fires at ~00:00 each day.
         */
        fun schedule(context: Context) {
            val now = Calendar.getInstance()
            val midnight = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val delayMs = midnight.timeInMillis - now.timeInMillis

            val request = OneTimeWorkRequestBuilder<StreakResetWorker>()
                .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
                .build()

            // After it fires, chain another schedule so it repeats daily
            WorkManager.getInstance(context)
                .enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.KEEP, request)
        }
    }
}