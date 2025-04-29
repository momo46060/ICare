package com.icare.model


data class CenterStaffModel(
    val token: String = "",
    val staffID: String = "",
    val labCenterID: Long = 0,
    val centerName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val isActive: Boolean = true,
    val phoneNumber: String = "",
    val profilePicture: String = "",
)