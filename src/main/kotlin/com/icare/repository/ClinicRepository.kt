package com.icare.repository

import com.icare.model.ClinicModel
import com.icare.model.ConsultationModel
import com.icare.model.DoctorModel
import org.apache.catalina.core.AprStatus

interface ClinicRepository {
    fun addClinic(clinic:ClinicModel):Short
    fun getClinics (): List<ClinicModel>
    fun getDoctors(): List<DoctorModel>
    fun consultation(consultation: ConsultationModel): Short
    fun getConsultationsByPrescriptionStatus(status: Short): List<ConsultationModel>
    fun getConsultationsByLabTestStatus(status: Short): List<ConsultationModel>
    fun getConsultationsByImaginingTestStatus(status: Short): List<ConsultationModel>
}