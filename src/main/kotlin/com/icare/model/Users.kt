package com.icare.model


data class Users (
    val userId: String = "0",
    val roleID: Int = 0,
    val fName:String="",
    val lName:String="",
    val email:String="",
    val birthDate:String="",
    val gender:String="M",
    val isActive:Boolean=true,
    val phoneNumber: String = "",
    val address: String = "",
    val nationalId:String=""

    )