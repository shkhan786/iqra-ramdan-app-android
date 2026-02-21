package com.shkhan.iqra.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId

object AlarmScheduler {

    fun scheduleIftarReminder(context: Context, iftarTime: String) {

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, IftarReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val now = LocalDateTime.now()

        val targetTime = LocalDateTime.of(
            now.toLocalDate(),
            java.time.LocalTime.parse(iftarTime)
        ).minusMinutes(10)

        val triggerMillis = targetTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            pendingIntent
        )
    }
}