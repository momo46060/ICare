package com.icare.repository

import com.icare.model.ImagingCentersModel
import com.icare.utils.FAILED
import com.icare.utils.OK
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ImagingCentersRepositoryImpl : ImagingCentersRepositry {
    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate

    override fun insertImagingCenter(ImagingCenters: ImagingCentersModel): Short {

        val sql = """
            MERGE INTO Lab_Imaging_Centers AS target
            USING (
                SELECT ? AS CenterID, ? AS CenterName, ? AS Phone, ? AS Address, ? AS CenterType
            ) AS source
            ON target.Center_ID = source.CenterID
            WHEN MATCHED THEN
                UPDATE SET
                    target.CenterName = source.CenterName,
                    target.Phone = source.Phone,
                    target.Address = source.Address,
                    target.CenterType = source.CenterType
            WHEN NOT MATCHED BY TARGET THEN
                INSERT (CenterName, Phone, Address, CenterType)
                VALUES (source.CenterName, source.Phone, source.Address, source.CenterType);
        """.trimIndent()

        try {
            iCareJdbcTemplate.update(
                sql,
                ImagingCenters.centerID,
                ImagingCenters.centerName,
                ImagingCenters.phone,
                ImagingCenters.address,
                ImagingCenters.centerType
            )
            return OK
        } catch (e: Exception) {
            println(e.stackTrace)
            println(e.message)
            return FAILED
        }
    }

    override fun getImagingCenters(): List<ImagingCentersModel> {
        val sql = """
            select *  from Lab_Imaging_Centers
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            ImagingCentersModel(
                centerID = rs.getInt("Center_ID"),
                centerName = rs.getString("CenterName"),
                phone = rs.getString("Phone"),
                address = rs.getString("address"),
                centerType = rs.getInt("CenterType")

            )
        }
    }

}