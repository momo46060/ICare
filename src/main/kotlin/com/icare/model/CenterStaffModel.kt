package com.icare.model


data class CenterStaffModel (
    val token:String = "",
    val staffID:String="",
    val labCenterID :Int=0,
    val firstName:String="",
    val lastName:String="",
    val email:String="",
    val isActive:Boolean=false,
    val phoneNumber: String = "",
    val profilePicture: String = "",
    )