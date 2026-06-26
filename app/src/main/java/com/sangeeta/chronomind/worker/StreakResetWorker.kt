//package com.sangeeta.chronomind.worker
//
//
//import android.content.Context
//import androidx.hilt.work.HiltWorker
//import androidx.work.*
//import com.sangeeta.chronomind.repository.ActivityRepository
//import dagger.assisted.Assisted
//import dagger.assisted.AssistedInject
//import java.util.concurrent.TimeUnit
//import java.util.Calendar
//
//
//@HiltWorker
//class StreakResetWorker @AssistedInject constructor(
//    @Assisted context: Context,
//    @Assisted workerParams: WorkerParameters,
//    private val activityRepository: ActivityRepository
//) : CoroutineWorker(context, workerParams) {
//
//    override suspend fun doWork(): Result {
//        return try {
//            activityRepository.resetMissedStreaks()
//            Result.success()
//        } catch (e: Exception) {
//            Result.retry()
//        }
//    }
//
//    companion object {
//        private const val WORK_NAME = "streak_reset_daily"
//
//
//        fun schedule(context: Context) {
//            val now = Calendar.getInstance()
//            val midnight = Calendar.getInstance().apply {
//                add(Calendar.DAY_OF_YEAR, 1)
//                set(Calendar.HOUR_OF_DAY, 0)
//                set(Calendar.MINUTE, 0)
//                set(Calendar.SECOND, 0)
//                set(Calendar.MILLISECOND, 0)
//            }
//            val delayMs = midnight.timeInMillis - now.timeInMillis
//
//            val request = OneTimeWorkRequestBuilder<StreakResetWorker>()
//                .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
//                .build()
//
//            WorkManager.getInstance(context)
//                .enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.KEEP, request)
//        }
//    }
//}


package com.sangeeta.chronomind.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.sangeeta.chronomind.repository.ActivityRepository
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltWorker
class StreakResetWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val activityRepository: ActivityRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            // New midnight rule:
            // 1. Any pending session from yesterday or earlier becomes incomplete history.
            // 2. Then streak-reset logic runs for activities the user missed.
            activityRepository.runMidnightMaintenance()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "streak_reset_worker"

        fun schedule(context: Context) {
            val workManager = WorkManager.getInstance(context)
            val request = PeriodicWorkRequestBuilder<StreakResetWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                .build()

            workManager.enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }

        private fun calculateInitialDelay(): Long {
            val now = Calendar.getInstance()
            val nextMidnight = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            return nextMidnight.timeInMillis - now.timeInMillis
        }
    }
}