package com.icare.repository

import com.icare.model.CenterStaffModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.ResponseModel
import com.icare.model.Users


interface UserRepository {

    fun registerPatient(patient: PatientModel): ResponseModel
    fun registerDoctor(doctor: DoctorModel): ResponseModel
    fun insertUser(user: Users): Boolean
    fun registerCenterStaff(centerStaff: CenterStaffModel): ResponseModel

}