package com.icare.repository

import com.icare.model.PatientModel
import com.icare.model.Users


interface UserRepository {

    fun registerPatient(patient: PatientModel): Boolean
    fun insertUser(users: Users): Boolean

}