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

        return if (verifyToken(patient.token)==ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token")){
            false
        }else{
            repository.registerPatient(patient)
        }
    }

   private fun verifyToken(token: String): ResponseEntity<String> {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        try {
            val decodedToken = auth.verifyIdToken(token);
            val uid = decodedToken.uid;
            return ResponseEntity.ok(uid);
        } catch (e: FirebaseAuthException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }



}