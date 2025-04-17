package com.icare.service

import com.icare.model.ClinicModel
import com.icare.model.ResponseModel


interface ClinicService {


    fun addClinic(clinic: ClinicModel): ResponseModel
    fun getClinics(token: String): ResponseModel
}