package com.icare.repository

import com.icare.model.CenterStaffModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.PharmacistsModel
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
                            6,
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
USING (VALUES ('${getUid(doctor.token)}', '${doctor.specialization}', '${doctor.fromTime}','${doctor.toTime}', '${doctor.clinicId}'))
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
                            fName = doctor.fName,
                            lName = doctor.lName,
                            email = doctor.email,
                            isActive = doctor.isActive,
                            phoneNumber = doctor.phoneNumber,
                            roleID = 2
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
USING (VALUES ('${getUid(centerStaff.token)}', '${centerStaff.labCenterID}',))
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
                            userId = getUid(centerStaff.token)!!,
                            fName = centerStaff.firstName,
                            lName = centerStaff.lastName,
                            email = centerStaff.email,
                            isActive = centerStaff.isActive,
                            phoneNumber = centerStaff.phoneNumber,
                            roleID = 5
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

    override fun registerPharmaciest(pharmaciests: PharmacistsModel): Short {
        if (getUid(pharmaciests.token) == null) {
            return INVALID_TOKEN
        } else {
            val deleteUser = """
                delete from Users where UserID = '${getUid(pharmaciests.token)}';
            """.trimIndent()
            val meargsql = """
                MERGE INTO Pharmacists AS target
USING (VALUES ('${getUid(pharmaciests.token)}', '${pharmaciests.pharmacyId}'))
    AS source (pharmacistid, pharmacy_id)
ON target.pharmacistid = source.pharmacistid
WHEN MATCHED THEN
    UPDATE SET
        target.pharmacistid = source.pharmacistid,
        target.pharmacy_id = source.pharmacy_id,
        
WHEN NOT MATCHED BY TARGET THEN
    INSERT (pharmacistid, pharmacy_id)
    VALUES (source.pharmacistid, source.pharmacy_id);
            """.trimIndent()
            try {
                if (insertUser(
                        Users(
                            userId = getUid(pharmaciests.token)!!,
                            fName = pharmaciests.fName,
                            lName = pharmaciests.lName,
                            email = pharmaciests.email,
                            isActive = pharmaciests.isActive,
                            phoneNumber = pharmaciests.phoneNumber,
                            roleID = 4
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


}