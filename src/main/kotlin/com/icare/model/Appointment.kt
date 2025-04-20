package com.icare.model

data class Appointment(
    //get PatientIdFomToken
    val token: String = "",//
    val patientId: String = "",

    val doctorId: String="",//
    val appointmentTime: Long=0,//
    //val status: Int = 0,//
    val appointmentId: Long=0,//
    val statusId: Short=0,//
    val doctorSpecialty: String,//
    val patientName: String="",//
    val doctorName: String = "",//
    val doctorImage: String="",
    val patientImage: String="",
)