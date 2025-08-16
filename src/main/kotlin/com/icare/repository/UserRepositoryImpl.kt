package com.icare.repository

import com.icare.model.*
import com.icare.utils.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp


@Repository
class UserRepositoryImpl : UserRepository {

    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate
    override fun getLoginInfo(uid: String): Users? {
        val userSql = """
       SELECT [RoleID] ,[FirstName] ,[LastName] ,[Email] ,[BirthDate] ,[Gender] ,[IsActive] ,[phone]
        ,[Address] ,[national_id] FROM [Users] WHERE [USERID] = '$uid'
        """.trimIndent()
        return runCatching {
            iCareJdbcTemplate.queryForObject(userSql) { rs, _ ->
                Users(
                    roleID = rs.getInt("RoleID"),
                    fName = rs.getString("FirstName"),
                    lName = rs.getString("LastName"),
                    email = rs.getString("Email"),
                    birthDate = rs.getTimestamp("BirthDate").time,
                    gender = rs.getString("Gender"),
                    isActive = rs.getBoolean("IsActive"),
                    phoneNumber = rs.getString("phone"),
                    address = rs.getString("Address"),
                    nationalId = rs.getString("national_id"),
                )
            }
        }.getOrElse { Users() }
    }

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
            runCatching {
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
            }.getOrElse { e ->
                println(e.stackTrace)
                println(e.message)
                iCareJdbcTemplate.update(deleteUser)
                return FAILED
            }
        }
    }

    /*override fun registerDoctor(doctor: DoctorModel): Short {
        if (getUid(doctor.token) == null) {
            return INVALID_TOKEN
        } else {
            val deleteUser = """
            DELETE FROM Users WHERE UserID = '${getUid(doctor.token)}';
        """.trimIndent()

            val mergeSql = """
            MERGE INTO Doctors AS target
            USING (VALUES ('${getUid(doctor.token)}', '${doctor.specialization}', '${doctor.fromTime}', '${doctor.toTime}', '${doctor.clinicId}'))
                AS source (DoctorID, Specialization, ClinicID, from_time, to_time)
            ON target.DoctorID = source.DoctorID
            WHEN MATCHED THEN
                UPDATE SET
                    target.Specialization = source.Specialization,
                    target.ClinicID = source.ClinicID,
                    target.from_time = source.from_time,
                    target.to_time = source.to_time
            WHEN NOT MATCHED BY TARGET THEN
                INSERT (DoctorID, Specialization, ClinicID, from_time, to_time)
                VALUES (source.DoctorID, source.Specialization, source.ClinicID, source.from_time, source.to_time);
        """.trimIndent()

            try {
                val userId = createUser(doctor.email, "123456", "${doctor.fname} ${doctor.lname}")

                // Insert user into Users table
                insertUser(
                    Users(
                        userId = userId,
                        fName = doctor.fname,
                        lName = doctor.lname,
                        email = doctor.email,
                        isActive = doctor.isActive,
                        phoneNumber = doctor.phoneNumber,
                        roleID = 2 // Role 2 for doctor
                    )
                )

                // Now insert/update doctor details in Doctors table
                iCareJdbcTemplate.update(mergeSql)
                return OK

            } catch (e: Exception) {
                println(e.stackTrace)
                println(e.message)
                iCareJdbcTemplate.update(deleteUser)
                return FAILED
            }
        }
    }*/

    override fun registerDoctor(doctor: DoctorModel): Short {

        return runCatching {
            val userId =
                createOrUpdateFirebaseUser(
                    email = doctor.email,
                    password = "P@ssw0rd",
                    displayName = "${doctor.fname} ${doctor.lname}",
                    uid = doctor.doctorID
                )
            println(userId)
            if (userId != null) {
                insertUser(
                    Users(
                        userId = userId,
                        fName = doctor.fname,
                        lName = doctor.lname,
                        email = doctor.email,
                        isActive = doctor.isActive,
                        phoneNumber = doctor.phoneNumber,
                        roleID = 2 // دكتور
                    )
                )
            }


            // Step 3: MERGE into Doctors table
            val mergeSql = """
            MERGE INTO Doctors AS target
            USING (VALUES (?, ?, ?, ?, ? , ? , ?))
                AS source (DoctorID, Specialization, ClinicID, from_time, to_time,rating,price)
            ON target.DoctorID = source.DoctorID
            WHEN MATCHED THEN
                UPDATE SET
                    target.Specialization = source.Specialization,
                    target.ClinicID = source.ClinicID,
                    target.From_Time = source.from_time,
                    target.To_Time = source.to_time,
                    target.Rating = source.rating,
                    target.Price = source.price
            WHEN NOT MATCHED BY TARGET THEN
                INSERT (DoctorID, Specialization, ClinicID, From_Time, To_Time,Rating,Price)
                VALUES (source.DoctorID, source.Specialization, source.ClinicID, source.from_time, source.to_time,source.rating,source.price);
        """.trimIndent()

            iCareJdbcTemplate.update(
                mergeSql,
                userId,
                doctor.specialization,
                doctor.clinicId,
                doctor.fromTime,
                doctor.toTime,
                doctor.rating,
                doctor.price,
            )

            OK

        }.getOrElse { e ->
            e.printStackTrace()
            FAILED
        }
    }

    override fun insertUser(user: Users): Boolean =
        runCatching {
            val meargsql = """
              MERGE INTO Users AS target
                USING (VALUES ('${user.userId}', '${user.roleID}', '${user.fName}', '${user.lName}', '${user.email}', '${
                Timestamp(
                    user.birthDate
                )
            }', '${user.gender}', '${user.isActive}','${user.phoneNumber}','${user.address}','${user.nationalId}'))
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
            iCareJdbcTemplate.update(meargsql)
            return true
        }.getOrElse { e ->
            println(e.stackTrace)
            println(e.message)
            return false
        }

    override fun registerCenterStaff(centerStaff: CenterStaffModel): Short =
        runCatching {
            getUid(centerStaff.token) ?: return INVALID_TOKEN

            val firebaseUserId = createOrUpdateFirebaseUser(
                email = centerStaff.email,
                password = "P@ssw0rd",
                displayName = "${centerStaff.firstName} ${centerStaff.lastName}",
                uid = centerStaff.staffID
            ) ?: return FAILED

            val userInserted = insertUser(
                Users(
                    userId = firebaseUserId,
                    fName = centerStaff.firstName,
                    lName = centerStaff.lastName,
                    email = centerStaff.email,
                    isActive = centerStaff.isActive,
                    phoneNumber = centerStaff.phoneNumber,
                    roleID = 3
                )
            )

            if (!userInserted) return FAILED

            val mergeSql = """
            MERGE INTO dbo.Center_Staff AS target
            USING (VALUES (?, ?))
            AS source (Staff_ID, Lab_Center_ID)
            ON target.Staff_ID = source.Staff_ID
            WHEN MATCHED THEN
                UPDATE SET target.Lab_Center_ID = source.Lab_Center_ID
            WHEN NOT MATCHED BY TARGET THEN
                INSERT (Staff_ID, Lab_Center_ID)
                VALUES (source.Staff_ID, source.Lab_Center_ID);
        """.trimIndent()

            iCareJdbcTemplate.update(mergeSql, firebaseUserId, centerStaff.labCenterID)
            OK
        }.getOrElse { e ->
            e.printStackTrace()
            FAILED
        }

    override fun registerPharmacist(pharmacists: PharmacistsModel): Short =
        runCatching {
            val uid = getUid(pharmacists.token) ?: return INVALID_TOKEN

            val firebaseUserId = createOrUpdateFirebaseUser(
                email = pharmacists.email,
                password = "P@ssw0rd",
                displayName = "${pharmacists.firstName} ${pharmacists.lastName}",
                uid = pharmacists.pharmacistID
            ) ?: return FAILED

            val userInserted = insertUser(
                Users(
                    userId = firebaseUserId,
                    fName = pharmacists.firstName,
                    lName = pharmacists.lastName,
                    email = pharmacists.email,
                    isActive = pharmacists.isActive,
                    phoneNumber = pharmacists.phoneNumber,
                    roleID = 4
                )
            )

            if (!userInserted) return FAILED

            val mergeSql = """
            MERGE INTO Pharmacists AS target
            USING (VALUES (?, ?))
            AS source (PharmacistID, Pharmacy_ID)
            ON target.PharmacistID = source.PharmacistID
            WHEN MATCHED THEN
                UPDATE SET target.Pharmacy_ID = source.Pharmacy_ID
            WHEN NOT MATCHED BY TARGET THEN
                INSERT (PharmacistID, Pharmacy_ID)
                VALUES (source.PharmacistID, source.Pharmacy_ID);
        """.trimIndent()

            iCareJdbcTemplate.update(mergeSql, firebaseUserId, pharmacists.pharmacyId)
            OK
        }.getOrElse { e ->
            e.printStackTrace()
            FAILED
        }

    override fun getPharmacists(): List<PharmacistsModel> {
        val sql = """
            SELECT U.UserID, U.FirstName, U.LastName, U.Email, U.phone, P.Pharmacy_ID, S.PharmacyName
                 FROM Pharmacists P
                 JOIN Users U
                 ON P.PharmacistID = U.UserID
                 JOIN Pharmacies S
                 ON P.Pharmacy_ID = S.Pharmacy_ID;
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            PharmacistsModel(
                pharmacistID = rs.getString("UserID"),
                firstName = rs.getString("FirstName"),
                lastName = rs.getString("LastName"),
                email = rs.getString("Email"),
                phoneNumber = rs.getString("phone"),
                pharmacyId = rs.getLong("Pharmacy_ID"),
                pharmacyName = rs.getString("PharmacyName"),
                profilePicture = getImage(rs.getString("UserID")) ?: "",
            )
        }
    }

    override fun getClinicStaff(): List<ClinicStaffModel> {
        val sql = """
            SELECT U.UserID, U.FirstName, U.LastName, U.Email, U.phone, S.Clinic_ID, C.Clinic_Name
            FROM Clinic_Staff S
            JOIN Users U
            ON S.Staff_ID = U.UserID
            JOIN dbo.Clinics C
            ON S.Clinic_ID = C.ClinicID;
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            ClinicStaffModel(
                id = rs.getString("UserID"),
                fname = rs.getString("FirstName"),
                lname = rs.getString("LastName"),
                email = rs.getString("Email"),
                phoneNumber = rs.getString("phone"),
                clinicId = rs.getLong("Clinic_ID"),
                clinicName = rs.getString("Clinic_Name"),
                profilePicture = getImage(rs.getString("UserID")) ?: "",
            )
        }
    }

    override fun getCenterStaff(): List<CenterStaffModel> {
        val sql = """
            SELECT U.UserID, U.FirstName, U.LastName, U.Email, U.phone, S.Lab_Center_ID, C.CenterName
            FROM dbo.Center_Staff S
            JOIN Users U
            ON S.Staff_ID = U.UserID
            JOIN dbo.Lab_Imaging_Centers C
            ON S.Lab_Center_ID = C.Center_ID;
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            CenterStaffModel(
                staffID = rs.getString("UserID"),
                firstName = rs.getString("FirstName"),
                lastName = rs.getString("LastName"),
                email = rs.getString("Email"),
                phoneNumber = rs.getString("phone"),
                labCenterID = rs.getLong("Lab_Center_ID"),
                centerName = rs.getString("CenterName"),
                profilePicture = getImage(rs.getString("UserID")) ?: "",
            )
        }
    }

    override fun registerClinicStaff(clinicStaff: ClinicStaffModel): Short =
        runCatching {
            getUid(clinicStaff.token)?.let {
                val userId =
                    createOrUpdateFirebaseUser(
                        email = clinicStaff.email,
                        password = "P@ssw0rd",
                        displayName = "${clinicStaff.fname} ${clinicStaff.lname}",
                        uid = clinicStaff.id
                    )

                userId?.let { uid ->
                    insertUser(
                        Users(
                            userId = uid,
                            fName = clinicStaff.fname,
                            lName = clinicStaff.lname,
                            email = clinicStaff.email,
                            isActive = clinicStaff.isActive,
                            phoneNumber = clinicStaff.phoneNumber,
                            roleID = 3
                        )
                    )
                }

                val mergeSql = """
        MERGE INTO dbo.Clinic_Staff AS target
USING (VALUES (?, ?))
AS source (Staff_ID, Clinic_ID)
ON target.Staff_ID = source.Staff_ID
WHEN MATCHED THEN
UPDATE SET
    target.Clinic_ID = source.Clinic_ID
WHEN NOT MATCHED BY TARGET THEN
INSERT (Staff_ID, Clinic_ID)
VALUES (source.Staff_ID, source.Clinic_ID);
    """.trimIndent()

                iCareJdbcTemplate.update(
                    mergeSql,
                    userId,
                    clinicStaff.clinicId,
                )
                OK
            } ?: INVALID_TOKEN
        }.getOrElse { e ->
            e.printStackTrace()
            FAILED
        }
}