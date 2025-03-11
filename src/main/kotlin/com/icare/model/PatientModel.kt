package com.icare.model

data class PatientModel(
    val token:String="",
    val roleID: Int = 0,
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
)