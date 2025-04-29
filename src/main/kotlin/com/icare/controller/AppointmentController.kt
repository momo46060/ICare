package com.icare.controller

import com.icare.model.Appointment
import com.icare.model.ResponseModel
import com.icare.model.TokenRequest
import com.icare.service.AppointmentServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/appointmentApi")
class AppointmentController {
    @Autowired
    lateinit var service: AppointmentServiceImpl

    @PostMapping("/bookAppointment")
    fun bookAppointment(@RequestBody appointment: Appointment): ResponseModel {
        return service.bookAppointment(appointment)
    }

    @PostMapping("/patientAppointment")
    fun getPatientAppointment(@RequestBody request: TokenRequest): ResponseModel {
        return service.getPatientAppointments(request.token)
    }

    @PostMapping("/appointmentByStatus")
    fun getAppointmentByStatus(@RequestBody request: TokenRequest): ResponseModel {
        return service.getAppointmentsByStatus(request.status, request.token)
    }

    @PostMapping("/getAppointments")
    fun getAppointments(@RequestBody request: TokenRequest): ResponseModel {
        return service.getAppointments(request.token)
    }

}