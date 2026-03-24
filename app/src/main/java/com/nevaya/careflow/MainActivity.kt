package com.nevaya.careflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.nevaya.careflow.navigation.AppNavGraph
import com.nevaya.careflow.ui.components.MainScreenWithFloatingMenu

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var showSplash by remember { mutableStateOf(true) }

            if (showSplash) {
                SplashScreen {
                    showSplash = false
                }
            } else {
                MainScreenWithFloatingMenu(navController) { padding ->
                    AppNavGraph(navController, padding)
                }
            }
        }
    }
}


