package com.icare.model

import org.springframework.boot.availability.ApplicationAvailability

data class Doctor(
    val doctorID:String="",
    val specializationID:String="",
    val fromTime: Long = 0,
    val toTime: Long = 0,
    val clincId:String="",

    )