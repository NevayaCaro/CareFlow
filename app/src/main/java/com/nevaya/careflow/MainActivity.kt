package com.nevaya.careflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.nevaya.careflow.screens.CreateJoinScreen
import com.nevaya.careflow.screens.CreateScreen
import com.nevaya.careflow.screens.CreatorAssignmentScreen
import com.nevaya.careflow.screens.WorkerAssignmentScreen
import com.nevaya.careflow.ui.theme.CareFlowTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CareFlowTheme {

                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "createJoin",
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        // FIRST SCREEN
                        composable("createJoin") {
                            CreateJoinScreen(
                                onCreateClick = {
                                    navController.navigate("create")
                                },
                                onJoinValid = { code ->
                                    navController.navigate("worker/$code")
                                }
                            )
                        }

                        // SECOND SCREEN
                        composable("create") {
                            CreateScreen(
                                onDoneClick = { code ->
                                    navController.navigate("creator/$code")
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )

                        }

                        composable("worker/{code}") { backStackEntry ->
                            val code = backStackEntry.arguments?.getString("code") ?: ""

                            WorkerAssignmentScreen(sessionCode = code)
                        }

                        composable("creator/{code}") { backStackEntry ->
                            val code = backStackEntry.arguments?.getString("code") ?: ""

                            CreatorAssignmentScreen(sessionCode = code)
                        }
                    }
                }
            }
        }
    }
}