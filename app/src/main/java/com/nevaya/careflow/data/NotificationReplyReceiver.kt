package com.nevaya.careflow.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput

class NotificationReplyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val chatId = intent.getStringExtra("chatId")
        val replyText = RemoteInput.getResultsFromIntent(intent)
            ?.getCharSequence("quick_reply_text")
            ?.toString()

        if (chatId != null && replyText != null) {
            // TODO: send reply to backend or local DB
        }
    }
}
