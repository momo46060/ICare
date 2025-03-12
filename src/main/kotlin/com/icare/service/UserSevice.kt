package com.icare.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.icare.model.Patient
import com.icare.model.PatientModel
import com.icare.model.ResponseModel
import com.icare.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class UserSevice {

    @Autowired
    lateinit var repository: UserRepository

    fun registerUser(patient: PatientModel): ResponseModel {
           return repository.registerPatient(patient)
    }

}