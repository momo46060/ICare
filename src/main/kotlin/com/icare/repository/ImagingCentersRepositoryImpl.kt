package com.icare.repository

import com.icare.model.ImagingCentersModel
import com.icare.utils.getUid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ImagingCentersRepositoryImpl: ImagingCentersRepositry {
    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate
    override fun insertImagingCenter(ImagingCenters: ImagingCentersModel): Short {
                val sqlMerge = """
    MERGE INTO Patients AS target
    USING (VALUES ('${ImagingCenters.CenterID}', '${ImagingCenters.CenterName}','${ImagingCenters.Phone}',
     '${ImagingCenters.address}', '${ImagingCenters.type}'))
        AS source ([Patient_ID], [ChronicDiseases], [CurrentMedications], [Allergies], [PastSurgeries], [Weight])
    ON target.[CenterID] = source.[CenterID]
    WHEN MATCHED THEN
        UPDATE SET
            target.[CenterID] = source.[CenterID],
            target.[CenterName] = source.[CenterName],
            target.[Phone] = source.[Phone],
            target.[address] = source.[address],
            target.[type] = source.[type]
    WHEN NOT MATCHED BY TARGET THEN
        INSERT ([CenterID], [CenterName], [Phone], [address], [type]
        VALUES (source.[CenterID], source.[CenterName], source.[Phone], source.[address], source.[type]);
 """.trimIndent()
                iCareJdbcTemplate.update(sqlMerge)
        return 0
    }

}