package com.icare.service

import com.icare.model.ClinicModel
import com.icare.model.ConsultationModel
import com.icare.model.ResponseModel
import com.icare.model.TokenRequest


interface ClinicService {


    fun addClinic(clinic: ClinicModel): ResponseModel
    fun getClinics(token: String): ResponseModel
    fun getDoctors(token: String): ResponseModel
    fun Consultation(consultationModel: ConsultationModel): ResponseModel
    fun getConsultationsByPrescriptionStatus(request: TokenRequest): ResponseModel
    fun getConsultationsByLabTestStatus(request: TokenRequest): ResponseModel
    fun getConsultationsByImaginingTestStatus(request: TokenRequest): ResponseModel

}