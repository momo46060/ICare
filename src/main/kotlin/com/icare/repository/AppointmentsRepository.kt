package com.icare.repository

import com.icare.model.Appointment

interface AppointmentsRepository {
    fun insertAppointments(appointment: Appointment): Short
    fun  getPatientAppointments(token: String): List<Appointment>
    fun getAppointmentsByStatus(status: Int): List<Appointment>
}