package com.nevaya.careflow.data

// Represents one room-based task (showers or meals)
data class TaskItem(
    val room: Int,
    var done: Boolean = false,
    var completedAt: String? = null
)