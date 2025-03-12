package com.icare.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

fun getUid(token: String): String? {
    return try {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val decodedToken = auth.verifyIdToken(token)
        decodedToken.uid
    } catch (e: FirebaseAuthException) {
        // Log the error or handle it as needed
        println("Failed to verify ID token: ${e.message}")
        null
    } catch (e: Exception) {
        // Handle any other exceptions
        println("An unexpected error occurred: ${e.message}")
        null
    }
}