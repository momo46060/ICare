package com.icare.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.icare.model.Patient
import com.icare.model.PatientModel
import com.icare.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class UserSevice {

    @Autowired
    lateinit var repository: UserRepository

    fun registerUser(patient: PatientModel): Boolean {

//        return if (verifyToken(patient.token)){
//            println("Invalid token")
//            false
//        }else{
           return repository.registerPatient(patient)
//        }
    }

   private fun verifyToken(token: String): Boolean {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        try {
            val decodedToken = auth.verifyIdToken(token);
            val uid = decodedToken.uid;
            return true;
        } catch (e: FirebaseAuthException) {
            return false;
        }
    }



}