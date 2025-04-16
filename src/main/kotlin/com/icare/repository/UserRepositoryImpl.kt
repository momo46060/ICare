package com.icare.repository

import com.google.api.services.storage.Storage
import com.icare.model.CenterStaffModel
import com.icare.model.ClinicModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.ResponseModel
import com.icare.model.Users
import com.icare.utils.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import kotlin.math.log


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
    MERGE INTO Patients AS target
    USING (VALUES ('${getUid(patient.token)}', '${patient.chronicDiseases}','${patient.currentMedications}',
     '${patient.allergies}', '${patient.pastSurgeries}','${patient.weight}'))
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
                            1,
                            patient.fName,
                            patient.lName,
                            patient.email,
                            patient.birthDate,
                            patient.gender,
                            patient.isActive,
                            phoneNumber = patient.phoneNumber,
                            address = patient.address,
                            nationalId = patient.nationalId,
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
USING (VALUES ('${getUid(doctor.token)}', '${doctor.specialization}', '${doctor.fromTime}','${doctor.toTime}', '${doctor.clincId}'))
    AS source (DoctorID, Specialization, ClinicID,from_time,to_time)
ON target.DoctorID = source.DoctorID
WHEN MATCHED THEN
    UPDATE SET
        target.Specialization = source.Specialization,
        target.ClinicID = source.ClinicID,
        target.fromTime = source.from_time,
        target.toTime = source.to_time,
WHEN NOT MATCHED BY TARGET THEN
    INSERT (DoctorID, Specialization , ClinicID , from_time , to_time)
    VALUES (source.DoctorID, source.Specialization, source.ClinicID , source.from_time, source.to_time);
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
                            doctor.isActive,
                            phoneNumber = doctor.phoneNumber,
                            address = doctor.address,
                            nationalId = doctor.nationalId,
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
USING (VALUES ('${user.userId}', '${user.roleID}', '${user.fName}', '${user.lName}', '${user.email}', '${user.birthDate}', '${user.gender}', '${user.isActive}','${user.phoneNumber}','${user.address}','${user.nationalId}'))
   AS source (
    [UserID], [RoleID], [FirstName], [LastName], [Email], [BirthDate], [Gender], [IsActive], 
    [phone], [address], [national_id]
)
ON target.[UserID] = source.[UserID]
WHEN MATCHED THEN
    UPDATE SET
        target.[RoleID] = source.[RoleID],
        target.[FirstName] = source.[FirstName],
        target.[LastName] = source.[LastName],
        target.[Email] = source.[Email],
        target.[BirthDate] = source.[BirthDate],
        target.[Gender] = source.[Gender],
        target.[IsActive] = source.[IsActive],
        target.[phone] = source.[phone],
        target.[address] = source.[address],
        target.[national_id] = source.[national_id]  -- <-- Removed trailing comma
WHEN NOT MATCHED BY TARGET THEN
    INSERT (
        [UserID], [RoleID], [FirstName], [LastName], [Email], 
        [BirthDate], [Gender], [IsActive], [phone], 
        [address], [national_id]
    )
    VALUES (
        source.[UserID], source.[RoleID], source.[FirstName], 
        source.[LastName], source.[Email], source.[BirthDate], 
        source.[Gender], source.[IsActive], source.[phone], 
        source.[address], source.[national_id]
    );""".trimIndent()
        try {
            iCareJdbcTemplate.update(meargsql)
            return true
        } catch (e: Exception) {
            println(e.stackTrace)
            println(e.message)
            return false
        }
    }

    override fun registerCenterStaff(centerStaff: CenterStaffModel): ResponseModel {
        if (getUid(centerStaff.token) == null) {
            return ResponseModel(INVALID_TOKEN, "")
        } else {
            val deleteUser = """
                delete from Users where UserID = '${getUid(centerStaff.token)}';
            """.trimIndent()
            val meargsql = """
                MERGE INTO Center_Staff AS target
USING (VALUES ('${getUid(centerStaff.token)}', '${centerStaff.LabCenterID}',))
    AS source (Staff_ID, Lab_Center_ID)
ON target.Staff_ID = source.Staff_ID
WHEN MATCHED THEN
    UPDATE SET
        target.Staff_ID = source.Staff_ID,
        target.Lab_Center_ID = source.Lab_Center_ID
        
WHEN NOT MATCHED BY TARGET THEN
    INSERT (Staff_ID, Lab_Center_ID)
    VALUES (source.Staff_ID, source.Lab_Center_ID);
            """.trimIndent()
            try {
                if (insertUser(
                        Users(
                            getUid(centerStaff.token)!!,
                            centerStaff.RoleID,
                            centerStaff.FirstName,
                            centerStaff.LastName,
                            centerStaff.Email,
                            centerStaff.BirthDate,
                            centerStaff.Gender,
                            centerStaff.IsActive,
                            phoneNumber = centerStaff.phoneNumber,
                            address = centerStaff.address,
                            nationalId = centerStaff.nationalId,
                        )
                    )
                ) {
                    iCareJdbcTemplate.update(meargsql)
                    return ResponseModel(OK, "")
                } else {
                    iCareJdbcTemplate.update(deleteUser)
                    return ResponseModel(DUPLICATE_USER, "")
                }
            } catch (e: Exception) {
                println(e.stackTrace)
                println(e.message)
                iCareJdbcTemplate.update(deleteUser)
                return ResponseModel(FAILED, "")
            }
        }
    }




}