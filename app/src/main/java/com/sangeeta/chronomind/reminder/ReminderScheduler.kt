package com.sangeeta.chronomind.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object ReminderScheduler {

    private const val REMINDER_REQUEST_CODE = 9001
    const val EXTRA_TIME = "extra_time"

    fun schedule(context: Context, timeString: String) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        val triggerAt = nextTriggerMillis(timeString)

        Log.d("ChronoMindReminder", "Scheduling reminder for: $timeString (Epoch: $triggerAt)")

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(EXTRA_TIME, timeString)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                try {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAt,
                        pendingIntent
                    )
                    return
                } catch (e: SecurityException) {
                    Log.e("ChronoMindReminder", "Exact alarm permission was denied at runtime fallback applied", e)
                }
            }
        }

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pendingIntent
        )
    }

    fun cancel(context: Context) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.d("ChronoMindReminder", "Reminders cancelled successfully.")
    }

    private fun nextTriggerMillis(timeString: String): Long {
        val sdf = SimpleDateFormat("hh:mm a", Locale.US)
        val parsed = runCatching { sdf.parse(timeString) }.getOrNull() ?: Date()

        val now = Calendar.getInstance()
        val parsedCal = Calendar.getInstance().apply { time = parsed }

        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, parsedCal.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, parsedCal.get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (target.timeInMillis <= now.timeInMillis) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        return target.timeInMillis
    }
}