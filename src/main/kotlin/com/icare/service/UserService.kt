package com.icare.service

import com.icare.model.*

interface UserService {
    fun getLoginInfo(request: TokenRequest): ResponseModel
    fun registerPatient(patient: PatientModel): ResponseModel
    fun registerDoctor(doctor: DoctorModel): ResponseModel
    fun registerCenterStaff(centerStaffModel: CenterStaffModel): ResponseModel
    fun registerPharmacist(pharmacists: PharmacistsModel): ResponseModel
    fun getPharmacists(token: String): ResponseModel
    fun getClinicStaff(token: String): ResponseModel
    fun getCenterStaff(token: String): ResponseModel
}

