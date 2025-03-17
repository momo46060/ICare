package com.icare.repository

import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.ResponseModel
import com.icare.model.Users


interface UserRepository {

    fun registerPatient(patient: PatientModel): Short
    fun registerDoctor(doctor: DoctorModel): Short
    fun insertUser(user: Users): Boolean

}