package com.icare.repository

import com.icare.model.ClinicModel

interface ClinicRepository {
    fun addClinic(clinic:ClinicModel):Short
    fun getClinics (): List<ClinicModel>
}