package com.padc.padcfirebase.fcm

import android.app.PendingIntent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.padc.padcfirebase.activities.DetailActivity
import com.padc.padcfirebase.activities.MainActivity

class AppFCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived: ${remoteMessage.notification}")

        var title: String? = null
        var body: String? = null

        val mainIntent = MainActivity.getNewIntent(this)
        var pendingIntent = PendingIntent.getActivity(
            this, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")

            title = it.title
            body = it.body


        }

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {

            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            val id = remoteMessage.data["id"]
            id?.let {
                val detailIntent = DetailActivity.newIntent(this, id)
                pendingIntent = PendingIntent.getActivity(
                    this, 1, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        }

        buildAndSendNotification(
            this,
            title ?: "FCM Testing",
            body ?: "FCM Testing",
            pendingIntent
        )

    }

    companion object {
        const val TAG = "AppFCMService"
    }
}