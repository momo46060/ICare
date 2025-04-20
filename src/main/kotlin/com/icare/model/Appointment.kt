package com.icare.model

data class Appointment(
    //get PatientIdFomToken
    val token: String = "",
    val doctorId: String="",
    val appointmentTime: Long=0,

    val appointmentId: Long=0,
    val patientImage: String="",
    val statusId: Short=0,
    val status: String = "",
    val doctorSpecialty: String,
    val patientName: String="",
    val doctorName: String = "",
    val doctorImage: String="",
)