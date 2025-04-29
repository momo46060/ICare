package com.icare.model

class PharmacistsModel(
    val token: String = "0",
    val pharmacistID :String="",
    val firstName:String="",
    val lastName:String="",
    val email:String="",
    val isActive:Boolean=true,
    val phoneNumber: String = "",
    val pharmacyId :Long=0,
    val pharmacyName: String = "",
    val profilePicture: String = "",
)
