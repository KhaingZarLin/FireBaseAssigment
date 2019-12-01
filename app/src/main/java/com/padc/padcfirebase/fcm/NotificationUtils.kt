package com.padc.padcfirebase.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.padc.padcfirebase.R

fun buildAndSendNotification(
    context: Context,
    title: String,
    message: String,
    pendingIntent: PendingIntent
) {
    val notificationManager = context
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val NOTIFICATION_CHANNEL_ID = context.getString(R.string.default_notification_channel_id)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null
    ) {
        val name = context.getString(R.string.app_name)
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(channel)
    }

    val notification = buildNoti(context, NOTIFICATION_CHANNEL_ID, title, message, pendingIntent)

    notificationManager.notify(getUniqueId(), notification)
}

private fun buildNoti(
    context: Context,
    channelId: String,
    title: String,
    content: String,
    intent: PendingIntent
): Notification {
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    return NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setColor(ContextCompat.getColor(context, R.color.colorAccent))
        .setContentTitle(title)
        .setContentText(title)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(content)
        )
        .setContentIntent(intent)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .build()
}

private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())