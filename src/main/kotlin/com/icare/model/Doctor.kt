package com.icare.model

import org.springframework.boot.availability.ApplicationAvailability

data class Doctor(
    val doctorID:String="",
    val specializationID:String="",
    val doctorAvailability:String="",
    val clincId:String="",

)