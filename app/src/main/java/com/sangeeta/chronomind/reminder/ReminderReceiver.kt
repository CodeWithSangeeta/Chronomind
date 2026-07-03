package com.sangeeta.chronomind.reminder

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.sangeeta.chronomind.MainActivity
import com.sangeeta.chronomind.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timeString = intent.getStringExtra(ReminderScheduler.EXTRA_TIME) ?: "07:00 AM"

        val openIntent = PendingIntent.getActivity(
            context,
            100,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val canPostNotification =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

        if (canPostNotification) {
            val notification = NotificationCompat.Builder(
                context,
                "chronomind_reminder_channel"
            )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Daily focus reminder")
                .setContentText("Tap to start your focus session today")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(openIntent)
                .build()

            NotificationManagerCompat.from(context).notify(2001, notification)
        }

        ReminderScheduler.schedule(context, timeString)
    }
}