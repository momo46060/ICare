package com.icare.service

import com.icare.model.CenterStaffModel
import com.icare.model.ClinicModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.ResponseModel

interface UserService {
    fun registerPatient(patient: PatientModel): ResponseModel
    fun registerDoctor(doctor: DoctorModel): ResponseModel
    fun registerCenterStaff(centerStaffModel: CenterStaffModel): ResponseModel


}

