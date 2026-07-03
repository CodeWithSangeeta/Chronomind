package com.sangeeta.chronomind.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sangeeta.chronomind.local.datastore.SettingsDataStore
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val enabled = settingsDataStore.isDailyReminderEnabled.first()
                val time = settingsDataStore.reminderTime.first()
                if (enabled) {
                    ReminderScheduler.schedule(context, time)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}