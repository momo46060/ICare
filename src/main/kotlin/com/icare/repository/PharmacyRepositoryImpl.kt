package com.icare.repository


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
        MERGE INTO Pharmacies AS target
        USING (SELECT ? AS Pharmacy_ID,? AS PharmacyName, ? AS Phone, ? AS PharmacyAddress) AS source
        ON target.Pharmacy_ID = source.Pharmacy_ID
        WHEN MATCHED THEN
            UPDATE SET
                target.pharmacyName = source.PharmacyName,
                target.phone = source.Phone,
                target.pharmacyAddress = source.PharmacyAddress
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (pharmacyName, phone, pharmacyAddress)
            VALUES (source.PharmacyName, source.Phone, source.PharmacyAddress);
    """.trimIndent()

        return try {
            iCareJdbcTemplate.update(
                sql,
                pharmacy.pharmacyID,
                pharmacy.pharmacyName,
                pharmacy.phone,
                pharmacy.pharmacyAddress
            )
            return OK
        } catch (e: Exception) {
            println(e.stackTrace)
            println(e.message)
            FAILED
        }
    }


    override fun getPharmacy(): List<PharmacyModel> {
        val sql = """
            select * from Pharmacies
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            PharmacyModel(
                pharmacyID = rs.getInt("Pharmacy_ID"),
                pharmacyName = rs.getString("PharmacyName"),
                phone = rs.getString("Phone"),
                pharmacyAddress = rs.getString("PharmacyAddress"),

                )
        }
    }
}
