package com.nevaya.careflow.data

// KEEP EVERYTHING AS STRING to match Create Screen
data class NurseAssignment(
    val name: String,
    val role: String,
    val rooms: String,
    val showers: String,
    val meals: String
)