package com.icare.model

data class DoctorModel(
    val token: String = "",
    val doctorID: String = "",
    val fname: String = "",
    val lname: String = "",
    val email: String = "",
    val isActive: Boolean = true,
    val specialization: String = "",
    val fromTime: Long = 0,
    val toTime: Long = 0,
    val clinicId: String = "",
    val phoneNumber: String = "",
    val rating: Double = 0.0,
    val price: Double = 0.0,
    val profilePicture: String = "",

    )
