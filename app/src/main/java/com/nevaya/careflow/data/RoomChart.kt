package com.nevaya.careflow.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class RoomChart(
    val roomId: String,

    var hygiene: SnapshotStateList<String> = mutableStateListOf(),
    var linen: SnapshotStateList<String> = mutableStateListOf(),
    var tasks: SnapshotStateList<String> = mutableStateListOf(),

    var device: String? = null,

    var deviceComment: String = "",

    var comment: String = "",

    var mealIntake: String = "",
    var mealOutputMl: String = "",
    var mealPercentage: String = ""
)