package com.icare.model



data class ClinicModel (
   val ClinicID: String,
    val Openinig_Hours: Int,
    val ClinicName: String,
    val StaffCount: Int,
    val Phone: String,
    val ClinicType: String,
    val ClinicLocaltion: String,
    val isOpen: Boolean
)

