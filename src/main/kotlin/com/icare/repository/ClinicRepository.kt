package com.icare.repository

import com.icare.model.ClinicModel
import com.icare.model.DoctorModel

interface ClinicRepository {
    fun addClinic(clinic:ClinicModel):Short
    fun getClinics (): List<ClinicModel>
    fun getDoctors(): List<DoctorModel>
}