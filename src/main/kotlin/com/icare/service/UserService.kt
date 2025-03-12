package com.icare.service

import com.icare.model.PatientModel
import com.icare.model.ResponseModel

interface UserService {
    fun registerUser(patient: PatientModel): ResponseModel
}