package com.nevaya.careflow.data

data class RoomChart(
    val room: String,

    var hygiene: MutableList<String> = mutableListOf(),
    var linen: MutableList<String> = mutableListOf(),

    var mealIntake: String = "",
    var mealOutputMl: String = "",

    var tasks: MutableList<String> = mutableListOf(),

    var comment: String = ""
)