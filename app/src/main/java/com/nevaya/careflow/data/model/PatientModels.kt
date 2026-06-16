package com.nevaya.careflow.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Patient(
    val name: String,
    val fallRisk: String,
    val mobility: String,
    val diet: String,
    val notes: String,
    val medication: String,
    val careLevel: Int,
    val allergies: String,
    val codeStatus: String,
    val admissionReason: String,
    val assignedNurse: String
)


