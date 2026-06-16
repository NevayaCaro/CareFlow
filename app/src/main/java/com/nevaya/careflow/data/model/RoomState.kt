package com.nevaya.careflow.data.models

import com.nevaya.careflow.data.model.Patient
import kotlinx.serialization.Serializable
@Serializable
data class RoomState(
    val roomNumber: Int,
    val patient: Patient? = null
)
