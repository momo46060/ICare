package com.icare.service

import com.icare.model.ClinicModel
import com.icare.model.PatientModel
import com.icare.model.ResponseModel
import com.icare.repository.ClinicRepositry
import com.icare.repository.PharmacyRepository
import org.springframework.beans.factory.annotation.Autowired


interface ClinicService {


    fun addClinic(clinic: ClinicModel): ResponseModel
    fun getClinics(token: String): ResponseModel
}