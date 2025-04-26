package com.icare.model



data class ClinicModel (
    val token: String = "0",
    val clinicID: Int=0,
    val clinicName: String,
    val phone: String,
    val clinicType: String,
    val clinicLocaltion: String,
    val isOpen: Boolean
)

