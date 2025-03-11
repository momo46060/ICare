package com.icare.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.icare.model.Patient
import com.icare.model.PatientModel
import com.icare.model.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@Repository
class UserRepositoryImpl : UserRepository {

    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate

    override fun registerPatient(patient: PatientModel): Boolean {
            val deleteUser = """
                delete from Users where UserID = '${getUid(patient.token)}';
            """.trimIndent()
        val sqlInsert = """ 
            INSERT into Patient([Patient_ID],[ChronicDiseases],[CurrentMedications],[Allergies],[PastSurgeries],[Weight])
        values ('${getUid(patient.token)}','${patient.chronicDiseases}','${patient.currentMedications}','${patient.allergies}','${patient.pastSurgeries}','${patient.weight}');""".trimIndent()
        try {
            if (insertUser(
                    Users(
                        getUid(patient.token),
                        patient.roleID,
                        patient.fName,
                        patient.lName,
                        patient.email,
                        patient.birthDate,
                        patient.gender,
                        patient.isActive
                    )
                )
            ) {
                iCareJdbcTemplate.update(sqlInsert)
                return true
            }else{
                return false

            }
        } catch (e: Exception) {
            println(e.stackTrace)
            println(e.message)
            iCareJdbcTemplate.update(deleteUser)
            return false
        }
    }

    override fun insertUser(user: Users): Boolean {
        val insertUser = """ 
               insert into Users([UserID],[RoleID],[FirstName],[LastName],[Email],[BirthDate],[Gender],[IsActive])values (
                '${user.userId}',
                '${user.roleID}',
                '${user.fName}',
                '${user.lName}',
                '${user.email}',
                '${user.birthDate}',
                '${user.gender}',
                '${user.isActive}'
               )
          """
        try {
            iCareJdbcTemplate.update(insertUser)
            return true
        } catch (e: Exception) {
            println(e.stackTrace)
            println(e.message)
            return false
        }
    }

    private fun getUid(token: String): String {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        val decodedToken = auth.verifyIdToken(token);
        return decodedToken.uid;
    }


}