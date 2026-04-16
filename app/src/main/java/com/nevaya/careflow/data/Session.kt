package com.nevaya.careflow.data

import com.nevaya.careflow.data.TaskItem

// session holds everything tied to a code
data class Session(
    var code: String,
    var workerCode: String = "",
    var creatorCode: String = "",

    var rooms: List<Int> = emptyList(),

    // NEW structured task lists
    var showerTasks: List<TaskItem> = emptyList(),
    var mealTasks: List<TaskItem> = emptyList(),

    var assignments: List<NurseAssignment> = emptyList()
)