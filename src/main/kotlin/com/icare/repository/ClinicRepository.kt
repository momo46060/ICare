package com.icare.repository

import com.icare.model.*

interface ClinicRepository {
    fun addClinic(clinic: ClinicModel): Short
    fun getClinics(): List<ClinicModel>
    fun getDoctors(): List<DoctorModel>
    fun getDoctorSchedule(uid: String): DoctorSchedule
    fun getMedicalRecord(uid: String): MedicalRecord
    fun consultation(consultation: ConsultationModel): Short
    fun getConsultationsByPrescriptionStatus(status: Short): List<ConsultationModel>
    fun getConsultationsByLabTestStatus(status: Short): List<ConsultationModel>
    fun getConsultationsByImaginingTestStatus(status: Short): List<ConsultationModel>
}