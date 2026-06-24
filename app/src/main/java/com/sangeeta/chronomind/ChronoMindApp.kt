//package com.sangeeta.chronomind
//
//
//import android.app.Application
//import dagger.hilt.android.HiltAndroidApp
//
//@HiltAndroidApp
//class ChronoMindApp : Application()




/**
 * FIX: Added HiltWorkerFactory for StreakResetWorker (Hilt + WorkManager integration).
 * Also schedules the daily streak-reset job at app start.
 */
package com.sangeeta.chronomind

import android.app.Application
import android.content.res.Configuration
import androidx.hilt.work.HiltWorkerFactory
import com.sangeeta.chronomind.worker.StreakResetWorker
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
        StreakResetWorker.schedule(this)
    }
}