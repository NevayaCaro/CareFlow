package com.nevaya.careflow.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nevaya.careflow.data.LiveActivity



data class Session(
    var code: String,
    var workerCode: String = "",
    var creatorCode: String,

    var rooms: List<Int> = emptyList(),

    var showerTasks: List<TaskItem> = emptyList(),
    var mealTasks: List<TaskItem> = emptyList(),

    var assignments: List<NurseAssignment> = emptyList(),

    // ACTIVE ROOM CHARTS
    var roomCharts: MutableMap<String, RoomChart> = mutableMapOf(),

    // SAVED ROOM SNAPSHOTS
    var savedRoomCharts: SnapshotStateList<RoomChartSnapshot> =
        mutableStateListOf(),

    var liveActivities: SnapshotStateList<LiveActivity> =
        mutableStateListOf(),
    val liveFeed: SnapshotStateList<LiveActivity> =
        mutableStateListOf()
)