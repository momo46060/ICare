package com.icare.service

import com.icare.model.ClinicModel
import com.icare.model.ResponseModel
import com.icare.repository.ClinicRepository
import com.icare.utils.EMPTY_LIST
import com.icare.utils.FAILED
import com.icare.utils.INVALID_TOKEN
import com.icare.utils.OK
import com.icare.utils.getUid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClinicServiceImpl : ClinicService{
    @Autowired
    lateinit var repository: ClinicRepository

    override fun addClinic(clinic: ClinicModel): ResponseModel {
        return ResponseModel(repository.addClinic(clinic),"")
    }



    override fun getClinics(token:String): ResponseModel {
        val list = listOf<ClinicModel>()
        try {
            if(getUid(token) == null) {
                return ResponseModel(status=INVALID_TOKEN)
            }else if (repository.getClinics().isEmpty()){
                return ResponseModel(status= EMPTY_LIST, data = listOf<ClinicModel>())
            }else{
                return ResponseModel(status=OK,data = repository.getClinics())
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ResponseModel(status= FAILED)
        }


    }
}