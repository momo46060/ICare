package com.icare.service

import com.icare.model.Appointment
import com.icare.model.ResponseModel



interface AppointmentService {
    fun bookAppointment(Appointments: Appointment ): ResponseModel
    fun  getPatientAppointments(token: String): ResponseModel
    fun getAppointmentsByStatus(status: Int,token: String): ResponseModel

}