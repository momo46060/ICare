package com.icare.model

data class ClinicStaffModel(
    val token: String = "",
    val id: String = "",
    val fName: String = "",
    val lName: String = "",
    val clinicId: Long = 0,
    val clinicName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val profilePicture: String = "",
)
