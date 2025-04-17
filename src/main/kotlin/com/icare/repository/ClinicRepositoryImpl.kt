package com.icare.repository

import com.icare.model.ClinicModel
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
            insert into Clinics(ClinicID,Openinig_Hours,Clinic_Name,ClinicType,StaffCount,
            Phone,ClinicLocation,IsOpen)
            values ('${clinic.ClinicID}','${clinic.Openinig_Hours}','${clinic.ClinicName}'
,'${clinic.ClinicType}','${clinic.StaffCount}','${clinic.Phone}','${clinic.ClinicLocaltion}'
,'${clinic.isOpen}')
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
                ClinicID = rs.getString("ClinicID"),
                Openinig_Hours = rs.getInt("Opening_Hours"),
                ClinicName = rs.getString("Clinic_Name"),
                ClinicType = rs.getString("ClinicType"),
                StaffCount = rs.getInt("StaffCount"),
                Phone = rs.getString("Phone"),
                ClinicLocaltion = rs.getString("ClinicLocation"),
                isOpen=rs.getBoolean("IsOpen")
            )
        }
    }

}