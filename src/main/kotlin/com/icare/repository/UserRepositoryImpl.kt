package com.icare.repository

import com.icare.model.DoctorModel
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

    override fun registerPatient(patient: PatientModel): Short {
        if (getUid(patient.token) == null) {
            return INVALID_TOKEN
        } else {
            val deleteUser = """
                delete from Users where UserID = '${getUid(patient.token)}';
            """.trimIndent()
            val sqlMerge = """
    MERGE INTO Patient AS target
    USING (VALUES ('12345', 'Diabetes', 'Insulin', 'Peanuts', 'Appendectomy', 70.5))
        AS source ([Patient_ID], [ChronicDiseases], [CurrentMedications], [Allergies], [PastSurgeries], [Weight])
    ON target.[Patient_ID] = source.[Patient_ID]
    WHEN MATCHED THEN
        UPDATE SET
            target.[ChronicDiseases] = source.[ChronicDiseases],
            target.[CurrentMedications] = source.[CurrentMedications],
            target.[Allergies] = source.[Allergies],
            target.[PastSurgeries] = source.[PastSurgeries],
            target.[Weight] = source.[Weight]
    WHEN NOT MATCHED BY TARGET THEN
        INSERT ([Patient_ID], [ChronicDiseases], [CurrentMedications], [Allergies], [PastSurgeries], [Weight])
        VALUES (source.[Patient_ID], source.[ChronicDiseases], source.[CurrentMedications], source.[Allergies], source.[PastSurgeries], source.[Weight]);
""".trimIndent()
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
                    iCareJdbcTemplate.update(sqlMerge)
                    return OK
                } else {
                    iCareJdbcTemplate.update(deleteUser)
                    return DUPLICATE_USER
                }
            } catch (e: Exception) {
                println(e.stackTrace)
                println(e.message)
                iCareJdbcTemplate.update(deleteUser)
                return FAILED
            }
        }
    }

    override fun registerDoctor(doctor: DoctorModel): Short {
        if (getUid(doctor.token) == null) {
            return INVALID_TOKEN
        } else {
            val deleteUser = """
                delete from Users where UserID = '${getUid(doctor.token)}';
            """.trimIndent()
            val meargsql = """
                MERGE INTO Doctor AS target
USING (VALUES ('${getUid(doctor.token)}', '${doctor.specialization}', '${doctor.doctorAvailability}', '${doctor.clincId}'))
    AS source (DoctorID, Specialization, Doctor_Availability, ClinicID)
ON target.DoctorID = source.DoctorID
WHEN MATCHED THEN
    UPDATE SET
        target.Specialization = source.Specialization,
        target.Doctor_Availability = source.Doctor_Availability,
        target.ClinicID = source.ClinicID
WHEN NOT MATCHED BY TARGET THEN
    INSERT (DoctorID, Specialization, Doctor_Availability, ClinicID)
    VALUES (source.DoctorID, source.Specialization, source.Doctor_Availability, source.ClinicID);
            """.trimIndent()
            try {
                if (insertUser(
                        Users(
                            getUid(doctor.token)!!,
                            doctor.roleID,
                            doctor.fName,
                            doctor.lName,
                            doctor.email,
                            doctor.birthDate,
                            doctor.gender,
                            doctor.isActive
                        )
                    )
                ) {
                    iCareJdbcTemplate.update(meargsql)
                    return OK
                } else {
                    iCareJdbcTemplate.update(deleteUser)
                    return DUPLICATE_USER
                }
            } catch (e: Exception) {
                println(e.stackTrace)
                println(e.message)
                iCareJdbcTemplate.update(deleteUser)
                return FAILED
            }
        }
    }

    override fun insertUser(user: Users): Boolean {
        val meargsql = """
              MERGE INTO Users AS target
USING (VALUES ('${user.userId}', '${user.roleID}', '${user.fName}', '${user.lName}', '${user.email}', '${user.birthDate}', '${user.gender}', '${user.isActive}'))
    AS source ([UserID], [RoleID], [FirstName], [LastName], [Email], [BirthDate], [Gender], [IsActive])
ON target.[UserID] = source.[UserID]
WHEN MATCHED THEN
    UPDATE SET
        target.[RoleID] = source.[RoleID],
        target.[FirstName] = source.[FirstName],
        target.[LastName] = source.[LastName],
        target.[Email] = source.[Email],
        target.[BirthDate] = source.[BirthDate],
        target.[Gender] = source.[Gender],
        target.[IsActive] = source.[IsActive]
WHEN NOT MATCHED BY TARGET THEN
    INSERT ([UserID], [RoleID], [FirstName], [LastName], [Email], [BirthDate], [Gender], [IsActive])
    VALUES (source.[UserID], 
    source.[RoleID], 
    source.[FirstName],
     source.[LastName], source.[Email], source.[BirthDate],
      source.[Gender], source.[IsActive]);
                                     """.trimIndent()
        try {
            iCareJdbcTemplate.update(meargsql)
            return true
        } catch (e: Exception) {
            println(e.stackTrace)
            println(e.message)
            return false
        }
    }


}