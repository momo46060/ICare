package com.icare.repository

import com.icare.model.ClinicModel
import com.icare.model.DoctorModel
import com.icare.utils.FAILED
import com.icare.utils.OK
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ClinicRepositoryImpl : ClinicRepository {

    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate

    override fun addClinic(clinic: ClinicModel): Short {
        val sql = """
            MERGE INTO Clinics AS target
            USING (VALUES ('${clinic.clinicID}',
                '${clinic.clinicName}',
                '${clinic.clinicType}',
                '${clinic.phone}',
                '${clinic.clinicLocaltion}',
                '${clinic.isOpen}'
            )) AS source (ClinicID,Clinic_Name, ClinicType, Phone, ClinicLocation, IsOpen)
            ON target.ClinicID = source.clinicID  
            WHEN MATCHED THEN
                UPDATE SET 
                    Clinic_Name = source.Clinic_Name, 
                    ClinicType = source.ClinicType,
                    Phone = source.Phone,
                    ClinicLocation = source.ClinicLocation,
                    IsOpen = source.IsOpen
            WHEN NOT MATCHED THEN
                INSERT (Clinic_Name, ClinicType, Phone, ClinicLocation, IsOpen)
                VALUES (source.Clinic_Name, source.ClinicType, source.Phone, source.ClinicLocation, source.IsOpen);

        """.trimIndent()
        try {
            iCareJdbcTemplate.update(sql)
            return OK
        } catch (e: Exception) {
            println(e.stackTrace)
            println(e.message)
            return FAILED
        }
    }



    override fun getClinics(): List<ClinicModel> {
        val sql="""
            select * from Clinics
        """.trimIndent()
        return iCareJdbcTemplate.query(sql)  { rs, _ ->
            ClinicModel(
                clinicID = rs.getInt("ClinicID"),
                clinicName = rs.getString("Clinic_Name"),
                clinicType = rs.getString("ClinicType"),
                phone = rs.getString("Phone"),
                clinicLocaltion = rs.getString("ClinicLocation"),
                isOpen=rs.getBoolean("IsOpen")
            )
        }
    }

    override fun getDoctors(): List<DoctorModel> {
        val sql="""
            select D.DoctorID,U.FirstName,U.LastName,U.Email,U.IsActive,D.Specialization,
                   D.from_time,D.to_time,D.ClinicID,U.phone
            from Doctors D
            join Users U
            on D.DoctorID = U.UserID
        """.trimIndent()

        return iCareJdbcTemplate.query(sql){rs, _ ->

            DoctorModel(
                doctorID = rs.getString("DoctorID"),
                fname =  rs.getString("FirstName"),
                lname = rs.getString("LastName"),
                email = rs.getString("Email"),
                isActive = rs.getBoolean("IsActive"),
                specialization = rs.getString("Specialization"),
                fromTime = rs.getLong("from_time"),
                toTime = rs.getLong("to_time"),
                clinicId = rs.getString("ClinicID"),
                phoneNumber = rs.getString("phone")


            )

        }
    }

}