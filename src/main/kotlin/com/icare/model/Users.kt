package com.icare.model


data class Users(
    val userId: String = "0",
    val roleID: Int = 0,
    val fName: String = "",
    val lName: String = "",
    val email: String = "",
    val birthDate: Long = 0,
    val gender: String = "M",
    val isActive: Boolean = false,
    val phoneNumber: String = "",
    val address: String = "",
    val nationalId: String = ""
)