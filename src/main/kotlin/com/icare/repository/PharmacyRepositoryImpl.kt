package com.icare.repository


import com.icare.model.PharmacistsModel
import com.icare.model.PharmacyModel
import com.icare.utils.FAILED
import com.icare.utils.OK
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PharmacyRepositoryImpl : PharmacyRepository {
    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate

    override fun addPharmacy(pharmacy: PharmacyModel): Short {
        val sql = """
        insert into Pharmacies(Phamacy_ID,PhamacyName,Phone,Email,PhamacyAddress,
            ContactStatus,PharmacyLocation)
        values(${pharmacy.Pharmacy_ID},${pharmacy.Pharmacy_Name},${pharmacy.Phone}, 
        ${pharmacy.Email},${pharmacy.PharmacyAddress},${pharmacy.ContactStatus},${pharmacy.PhamacyLocation})
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

    override fun getPharmacy(): List<PharmacyModel> {
        val sql = """
            select * from Pharmacies
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            PharmacyModel(
                Pharmacy_ID = rs.getInt("Pharmacy_ID"),
                Pharmacy_Name = rs.getString("PharmacyName"),
                Phone = rs.getString("Phone"),
                Email = rs.getString("Email"),
                PharmacyAddress = rs.getString("PharmacyAddress"),
                ContactStatus = rs.getString("ContactStatus"),
                PhamacyLocation = rs.getString("PharmacyLocation"),
            )
        }
    }

    override fun getPharmaciest(): List<PharmacistsModel> {
        val sql = """
            SELECT U.UserID, U.FirstName, U.LastName, U.Email, U.phone, P.Pharmacy_ID
                 FROM Pharmacists P
                        JOIN Users U
              ON P.Pharmacy_ID = U.UserID;
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            PharmacistsModel(

                token = rs.getString("UserID"),
                fName = rs.getString("FirstName"),
                lName = rs.getString("LastName"),
                email = rs.getString("Email"),
               phoneNumber = rs.getString("phone"),
                PharmacyId = rs.getString("Pharmacy_ID"),


                )
        }
    }

}