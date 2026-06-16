package com.nevaya.careflow.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.getStringExtra("action")
        val chatId = intent.getStringExtra("chatId")

        when (action) {
            "mute" -> {
                // TODO: update backend or local DB
            }
            "archive" -> {
                // TODO: archive chat
            }
        }
    }
}
