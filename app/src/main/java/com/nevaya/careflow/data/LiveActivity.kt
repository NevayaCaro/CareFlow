package com.nevaya.careflow.data

data class LiveActivity(
    val workerName: String,
    val roomId: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)