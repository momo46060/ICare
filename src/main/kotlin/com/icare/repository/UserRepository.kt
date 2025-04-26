package com.icare.repository

import com.icare.model.CenterStaffModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.ResponseModel
import com.icare.model.ClinicModel
import com.icare.model.PharmacistsModel
import com.icare.model.PharmacyModel
import com.icare.model.TokenRequest
import com.icare.model.Users


interface UserRepository {

    fun getLoginInfo(uid:String): Users?

    fun registerPatient(patient: PatientModel): Short
    fun registerDoctor(doctor: DoctorModel): Short
    fun insertUser(user: Users): Boolean
    fun registerCenterStaff(centerStaff: CenterStaffModel): ResponseModel
    fun registerPharmaciest(pharmaciests: PharmacistsModel): Short


}