package com.icare.model

data class ClinicStaffModel(
    val token: String = "",
    val id: String = "",
    val fname: String = "",
    val lname: String = "",
    val clinicId: Long = 0,
    val clinicName: String = "",
    val email: String = "",
    val isActive:Boolean=true,
    val phoneNumber: String = "",
    val profilePicture: String = "",
)
