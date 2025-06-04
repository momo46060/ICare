package com.icare.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.icare.model.TimeSlot

data class EmptyUIdException(override val message: String?) : Exception()

const val MINUTES_TO_MILLIS = 60000


fun getUid(token: String): String? {
    return try {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val decodedToken = auth.verifyIdToken(token)
        decodedToken.uid
    } catch (e: FirebaseAuthException) {
        println("Failed to verify ID token: ${e.message}")
        null
    } catch (e: Exception) {
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

fun createOrUpdateFirebaseUser(email: String, password: String, displayName: String, uid: String): String? {
    val auth = FirebaseAuth.getInstance()

    return try {
        if (uid.isEmpty()) throw EmptyUIdException("Doctor ID is empty")
        val existingUser = auth.getUser(uid)

        val updateRequest = UserRecord.UpdateRequest(existingUser.uid)
            .setEmail(email)
            .setDisplayName(displayName)

        auth.updateUser(updateRequest)

        println("Updated existing user: ${existingUser.uid}")
        existingUser.uid

    } catch (ex: Exception) {
        when (ex) {
            is FirebaseAuthException, is EmptyUIdException -> {
                val createRequest = UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(displayName)
                    .setEmailVerified(false)
                    .setDisabled(false)

                val newUser = auth.createUser(createRequest)
                println("Created new user: ${newUser.uid}")
                newUser.uid
            }

            else -> throw ex
        }
    }
}

fun TimeSlot.divide(slotDurationMinutes: Short): List<TimeSlot> {
    require(slotDurationMinutes > 0) { "lengthMinutes was $slotDurationMinutes. Must specify positive amount of minutes." }

    val lengthMillis = slotDurationMinutes * MINUTES_TO_MILLIS
    val timeSlots = mutableListOf<TimeSlot>()
    var nextStartTime = startTime

    while (nextStartTime < endTime) {
        val nextEndTime = nextStartTime + lengthMillis
        val slotEndTime = if (nextEndTime > endTime) endTime else nextEndTime
        timeSlots.add(TimeSlot(nextStartTime, slotEndTime))
        nextStartTime = nextEndTime
    }

    return timeSlots
}