package com.nevaya.careflow.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nevaya.careflow.data.AppNotificationManager

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM", "New token: $token")

        // TODO: send token to your backend so it can send push notifications
        // Example:
        // BackendApi.updateFcmToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM", "Message received: ${message.data}")

        val data = message.data

        val type = data["type"] ?: "message"

        when (type) {

            // Normal chat message
            "message" -> {
                val chatId = data["chatId"] ?: return
                val body = data["body"] ?: "New message"

                AppNotificationManager.showChatNotification(
                    context = this,
                    chatId = chatId,
                    message = body
                )
            }

            // Emergency alert
            "emergency" -> {
                val title = data["title"] ?: "Emergency Alert"
                val body = data["body"] ?: "Critical patient alert"

                AppNotificationManager.showEmergencyAlert(
                    context = this,
                    title = title,
                    message = body
                )
            }
        }
    }
}
