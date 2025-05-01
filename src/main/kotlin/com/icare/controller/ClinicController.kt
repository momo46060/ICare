package com.icare.controller

import com.icare.model.ClinicModel
import com.icare.model.ConsultationModel
import com.icare.model.ResponseModel
import com.icare.model.TokenRequest
import com.icare.service.ClinicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clinicApi")
class ClinicController {
    @Autowired
    lateinit var service: ClinicService

    @PostMapping("/addClinic")
    fun addClinic(@RequestBody clinicModel: ClinicModel): ResponseModel {
        println(clinicModel)
        return service.addClinic(clinicModel)
    }

    @PostMapping("/getClinics")
    fun getClinics(@RequestBody request: TokenRequest): ResponseModel {
        return service.getClinics(request.token)
    }

    @PostMapping("/doctorSchedule")
    fun getDoctorSchedule(@RequestBody request: TokenRequest): ResponseModel {
        return service.getDoctorSchedule(request.token, request.uid)
    }

    @PostMapping("/medicalRecord")
    fun getMedicalRecord(@RequestBody request: TokenRequest): ResponseModel {
        return service.getMedicalRecord(request.token, request.uid)
    }

    @PostMapping("/getDoctors")
    fun getDoctors(@RequestBody request: TokenRequest): ResponseModel {
        println(request)
        return service.getDoctors(request.token)
    }

    @PostMapping("/consultation")
    fun consultation(@RequestBody consultationModel: ConsultationModel): ResponseModel {
        return service.consultation(consultationModel)
    }

    @PostMapping("/getConsultationsByPrescriptionStatus")
    fun getConsultationsByPrescriptionStatus(@RequestBody request: TokenRequest): ResponseModel {
        println(request)
        return service.getConsultationsByPrescriptionStatus(request)
    }

    @PostMapping("/getConsultationsByLabTestStatus")
    fun getConsultationsByLabTestStatus(@RequestBody request: TokenRequest): ResponseModel {
        return service.getConsultationsByLabTestStatus(request)
    }

    @PostMapping("/getConsultationsByImaginingTestStatus")
    fun getConsultationsByImaginingTestStatus(@RequestBody request: TokenRequest): ResponseModel {
        return service.getConsultationsByImaginingTestStatus(request)
    }


    @PostMapping("/getAdminStatistics")
    fun getAdminStatistics(@RequestBody request: TokenRequest): ResponseModel =
        service.getAdminStatistics(request)


}