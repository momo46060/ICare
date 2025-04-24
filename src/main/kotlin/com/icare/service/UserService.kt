package com.icare.service

import com.icare.model.CenterStaffModel
import com.icare.model.ClinicModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.PharmacistsModel
import com.icare.model.ResponseModel
import com.icare.model.TokenRequest
import com.icare.model.Users

interface UserService {
    fun getLoginInfo(request: TokenRequest): ResponseModel
    fun registerPatient(patient: PatientModel): ResponseModel
    fun registerDoctor(doctor: DoctorModel): ResponseModel
    fun registerCenterStaff(centerStaffModel: CenterStaffModel): ResponseModel
    fun registerPharmaciest(pharmaciests: PharmacistsModel): ResponseModel

}

