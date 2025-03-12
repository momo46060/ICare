package com.icare.repository

import com.icare.model.PatientModel
import com.icare.model.ResponseModel
import com.icare.model.Users
import com.icare.utils.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class UserRepositoryImpl : UserRepository {

    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate

    override fun registerPatient(patient: PatientModel): ResponseModel {
        if (getUid(patient.token) == null) {
            return ResponseModel(INVALID_TOKEN, null)
        } else {
            val deleteUser = """
                delete from Users where UserID = '${getUid(patient.token)}';
            """.trimIndent()
            val sqlInsert = """ 
            INSERT into Patient([Patient_ID],[ChronicDiseases],[CurrentMedications],[Allergies],[PastSurgeries],[Weight])
        values ('${getUid(patient.token)}','${patient.chronicDiseases}','${patient.currentMedications}','${patient.allergies}','${patient.pastSurgeries}','${patient.weight}');""".trimIndent()
            try {
                if (insertUser(
                        Users(
                            getUid(patient.token)!!,
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
                    return ResponseModel(OK, null)
                } else {
                    iCareJdbcTemplate.update(deleteUser)
                    return ResponseModel(DUPLICATE_USER, null)
                }
            } catch (e: Exception) {
                println(e.stackTrace)
                println(e.message)
                iCareJdbcTemplate.update(deleteUser)
                return ResponseModel(FAILED, null)
            }
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


}