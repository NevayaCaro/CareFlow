package com.nevaya.careflow.data


data class Patient(
    var roomNumber: Int,
    var patientName: String,
    var age: String,
    var gender: String,
    var diagnosis: String,
    var allergies: String,
    var diet: String,
    var mobility: String,
    var precautions: String,
    var codeStatus: String,
    var notes: String
)