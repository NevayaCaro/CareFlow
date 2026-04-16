package com.nevaya.careflow.data

// session holds everything tied to a code
data class Session(
    var code: String,
    var workerCode: String = "",
    var creatorCode: String = "",
    var rooms: List<Int> = emptyList(),
    var assignments: List<NurseAssignment> = emptyList()
)