package com.icare.service

import com.icare.model.ClinicModel
import com.icare.model.ImagingCentersModel
import com.icare.model.ResponseModel
import com.icare.repository.ImagingCentersRepositry
import com.icare.repository.PharmacyRepository
import com.icare.utils.EMPTY_LIST
import com.icare.utils.FAILED
import com.icare.utils.INVALID_TOKEN
import com.icare.utils.OK
import com.icare.utils.getUid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ImagingCentersServiceImpl: ImagingCentersService {
    @Autowired
    lateinit var repository: ImagingCentersRepositry
    override fun insertImagingCenter(imagingCenter: ImagingCentersModel): ResponseModel {
            return ResponseModel(repository.insertImagingCenter(imagingCenter),"")
        }

    override fun getImagingCenters(token: String): ResponseModel {
        val list = listOf<ImagingCentersModel>()
        try {
            if(getUid(token) == null) {
                return ResponseModel(status=INVALID_TOKEN)
            }else if (repository.getImagingCenters().isEmpty()){
                return ResponseModel(status= EMPTY_LIST, data = listOf<ImagingCentersModel>())
            }else{
                return ResponseModel(status=OK,data = repository.getImagingCenters())
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ResponseModel(status= FAILED)
        }
    }
}
