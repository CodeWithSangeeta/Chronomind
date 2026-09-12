package com.sangeeta.chronomind.timer


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sangeeta.chronomind.repository.ActivityRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerFinishedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var activityRepository: ActivityRepository

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("FinishedTimer", "Receiver fired")
        val activityId = intent.getIntExtra(EXTRA_ACTIVITY_ID, -1)
        if (activityId == -1) return

        val pendingResult = goAsync()

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val activity = activityRepository
                    .observeById(activityId)
                    .firstOrNull()

                Log.d("FinishedTimer", "Auto cleanup for activityId=$activityId")
                if (activity?.timerFinishedAtEpochMillis != null) {
                    activityRepository.abandonToHistory(activity)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val EXTRA_ACTIVITY_ID = "extra_activity_id"
    }
}