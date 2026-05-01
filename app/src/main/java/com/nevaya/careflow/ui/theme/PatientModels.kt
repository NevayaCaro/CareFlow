package com.nevaya.careflow.ui.screens

data class Patient(
    val name: String,
    val fallRisk: String,
    val mobility: String,
    val diet: String,
    val notes: String,
    val careLevel: Int
)

data class Room(
    val roomNumber: Int,
    val patient: Patient? = null
)