package com.nevaya.careflow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.nevaya.careflow.data.AppNotificationManager
import com.nevaya.careflow.navigation.AppNavGraph
import com.nevaya.careflow.ui.theme.CareFlowTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⭐ Create notification channels
        AppNotificationManager.createNotificationChannel(this)

        // ⭐ Log FCM token for testing
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("FCM", "Token: $token")
        }

        // ⭐ Read deep‑link BEFORE UI loads
        val deepLinkChatId = intent?.getStringExtra("chatId")

        setContent {
            CareFlowTheme {

                navController = rememberNavController()

                // ⭐ If app was opened from a notification → jump directly to chat
                if (deepLinkChatId != null) {
                    LaunchedEffect(Unit) {
                        navController.navigate("chat/$deepLinkChatId") {
                            popUpTo(0) // clear backstack so onboarding doesn't appear
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    AppNavGraph(
                        navController = navController,
                        padding = innerPadding,
                        // ⭐ Start at chat screen if deep‑linked, otherwise normal start
                        startDestination = if (deepLinkChatId == null) {
                            "onboarding"
                        } else {
                            "chat/$deepLinkChatId"
                        }
                    )
                }
            }
        }

        // ⭐ Handle notification tap when app is launched from CLOSED state
        handleNotificationIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        val chatId = intent?.getStringExtra("chatId")

        if (chatId != null && ::navController.isInitialized) {

            val targetRoute = "chat/$chatId"

            // ⭐ Prevent duplicate navigation
            if (navController.currentDestination?.route != targetRoute) {
                navController.navigate(targetRoute)
            }
        }

        // ⭐ Prevent re-triggering
        intent?.removeExtra("chatId")
    }
}
