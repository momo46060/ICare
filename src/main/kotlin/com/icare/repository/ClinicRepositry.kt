package com.icare.repository

import com.icare.model.ClinicModel

interface ClinicRepositry {
    fun addClinic(clinic:ClinicModel):Short
    fun getClinics (): List<ClinicModel>
}