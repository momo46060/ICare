package com.icare.utils

import com.google.firebase.auth.AuthErrorCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord

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

fun getImage(uid: String): String? {
    return try {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.getUser(uid)?.photoUrl.toString()
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

/*
fun createUser(email: String, password: String, name: String): String {
    val request = UserRecord.CreateRequest()
        .setEmail(email)
        .setPassword(password)
        .setDisplayName(name)
        .setEmailVerified(false)
        .setDisabled(false)

    val userRecord = FirebaseAuth.getInstance().createUser(request)
    return userRecord.uid
}*/
/*fun createUser(email: String, password: String, name: String): String {
    val auth = FirebaseAuth.getInstance()

    return try {
        val request = UserRecord.CreateRequest()
            .setEmail(email)
            .setPassword(password)
            .setDisplayName(name)
            .setEmailVerified(false)
            .setDisabled(false)

        val userRecord = auth.createUser(request)
        userRecord.uid
    } catch (e: FirebaseAuthException) {
        // الصح نستخدم authErrorCode مش errorCode
        if (e.authErrorCode == AuthErrorCode.EMAIL_ALREADY_EXISTS) {
            val existingUser = auth.getUserByEmail(email)

            val updateRequest = UserRecord.UpdateRequest(existingUser.uid)
                .setDisplayName(name)
                .setPassword(password) // ممكن تشيله لو مش عايز تحدث الباسورد
                .setEmailVerified(false)
                .setDisabled(false)

            val updatedUser = auth.updateUser(updateRequest)
            updatedUser.uid
        } else {
            throw e
        }
    }
}*/

fun createUser(email: String, password: String, name: String,uid: String): String {
    val auth = FirebaseAuth.getInstance()

    try {
        // لو المستخدم موجود بالإيميل، حدّثه
        val existingUser = auth.getUser(uid)

        val updateRequest = UserRecord.UpdateRequest(existingUser.uid)
            .setEmail(email)
            .setDisplayName(name)
            .setPassword(password) // احذف دي لو مش عايز تحدث الباسورد
            .setEmailVerified(false)
            .setDisabled(false)

        val updatedUser = auth.updateUser(updateRequest)
        return updatedUser.uid

    } catch (e: FirebaseAuthException) {
        // المستخدم مش موجود بالإيميل، يبقى ننشئه
        if (e.authErrorCode == AuthErrorCode.USER_NOT_FOUND) {
            val request = UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(name)
                .setEmailVerified(false)
                .setDisabled(false)

            val userRecord = auth.createUser(request)
            return userRecord.uid
        } else {
            throw e
        }
    }
}

