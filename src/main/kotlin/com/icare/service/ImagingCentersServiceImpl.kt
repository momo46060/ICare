package com.icare.service

import com.icare.model.ClinicModel
import com.icare.model.ImagingCentersModel
import com.icare.model.ResponseModel
import com.icare.repository.ImagingCentersRepositry
import com.icare.repository.PharmacyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ImagingCentersServiceImpl: ImagingCentersService {
    @Autowired
    lateinit var repository: ImagingCentersRepositry
    override fun insertImagingCenter(imagingCenter: ImagingCentersModel): ResponseModel {
            return ResponseModel(repository.insertImagingCenter(imagingCenter),"")
        }
}
