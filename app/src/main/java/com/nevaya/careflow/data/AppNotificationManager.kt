package com.nevaya.careflow.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.nevaya.careflow.MainActivity
import com.nevaya.careflow.R

object AppNotificationManager {

    private const val CHANNEL_ID = "messages_channel"
    private const val EMERGENCY_CHANNEL = "emergency_alerts"
    private const val GROUP_KEY_MESSAGES = "careflow_messages"
    private const val QUICK_REPLY_KEY = "quick_reply_text"

    // Create both channels
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val messageChannel = NotificationChannel(
                CHANNEL_ID,
                "Messages",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for new messages and alerts"
            }

            val emergencyChannel = NotificationChannel(
                EMERGENCY_CHANNEL,
                "Emergency Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical patient alerts"
                enableVibration(true)
                enableLights(true)
            }

            // 🔹 Add this default channel to match your manifest
            val defaultChannel = NotificationChannel(
                "careflow_default_channel_id",
                "Default CareFlow Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Default channel for Firebase notifications"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(messageChannel)
            manager.createNotificationChannel(emergencyChannel)
            manager.createNotificationChannel(defaultChannel) // ✅ Added line
        }
    }

    // Deep link into chat screen
    fun createChatPendingIntent(context: Context, chatId: String): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("chatId", chatId)
        }

        return PendingIntent.getActivity(
            context,
            chatId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    // Quick reply action
    private fun quickReplyAction(context: Context, chatId: String): NotificationCompat.Action {
        val remoteInput = RemoteInput.Builder(QUICK_REPLY_KEY)
            .setLabel("Reply")
            .build()

        val intent = Intent(context, NotificationReplyReceiver::class.java).apply {
            putExtra("chatId", chatId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            chatId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_message,
            "Reply",
            pendingIntent
        ).addRemoteInput(remoteInput).build()
    }

    // Mute action
    private fun muteAction(context: Context, chatId: String): NotificationCompat.Action {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra("action", "mute")
            putExtra("chatId", chatId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            chatId.hashCode() + 1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_message,
            "Mute",
            pendingIntent
        ).build()
    }

    // Archive action
    private fun archiveAction(context: Context, chatId: String): NotificationCompat.Action {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra("action", "archive")
            putExtra("chatId", chatId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            chatId.hashCode() + 2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_message,
            "Archive",
            pendingIntent
        ).build()
    }

    // Show notification for new message
    fun showChatNotification(context: Context, chatId: String, message: String) {

        val pendingIntent = createChatPendingIntent(context, chatId)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle("New Message")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setGroup(GROUP_KEY_MESSAGES)
            .addAction(quickReplyAction(context, chatId))
            .addAction(muteAction(context, chatId))
            .addAction(archiveAction(context, chatId))
            .build()

        NotificationManagerCompat.from(context)
            .notify(chatId.hashCode(), notification)

        // Summary notification for grouping
        val summary = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_message)
            .setStyle(NotificationCompat.InboxStyle())
            .setGroup(GROUP_KEY_MESSAGES)
            .setGroupSummary(true)
            .build()

        NotificationManagerCompat.from(context).notify(0, summary)
    }

    // Emergency alert
    fun showEmergencyAlert(context: Context, title: String, message: String) {
        val notification = NotificationCompat.Builder(context, EMERGENCY_CHANNEL)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toInt(), notification)
    }
}
