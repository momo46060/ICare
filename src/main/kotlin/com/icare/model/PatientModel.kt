package com.icare.model

import com.google.type.PhoneNumber
import javax.naming.RefAddr

data class PatientModel(
    val token:String="",
    val fName:String="",
    val lName:String="",
    val email:String="",
    val birthDate: String = "",
    val gender:String="",
    val isActive:Boolean=true,
    val chronicDiseases: String = "",
    val currentMedications: String = "",
    val allergies: String = "",
    val pastSurgeries: String = "",
    val weight: Double = 0.0,
    val phoneNumber: String = "",
    val address: String = "",
    val nationalId:String=""
)