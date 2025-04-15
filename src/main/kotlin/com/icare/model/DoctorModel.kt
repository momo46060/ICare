package com.icare.model

data class DoctorModel(
    val token:String="",
    val roleID: Int = 0,
    val fName:String="",
    val lName:String="",
    val email:String="",
    val birthDate: String = "",
    val gender:String="",
    val isActive:Boolean=true,
    val doctorID:String="",
    val specialization:String="",
    val doctorAvailability:String="",
    val clincId:String="",
    val phoneNumber: String = "",
    val address: String = "",
    val nationalId:String=""
)
