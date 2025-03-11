package com.icare.model

import java.util.Date

data class Users (
    val userId: String = "0",
    val roleID: Int = 0,
    val fName:String="",
    val lName:String="",
    val email:String="",
    val birthDate:String="",
    val gender:String="",
    val isActive:Boolean=true,

    )