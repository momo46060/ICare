package com.icare.service

import com.icare.model.PharmacyModel
import com.icare.model.ResponseModel
import com.icare.repository.PharmacyRepository
import com.icare.utils.FAILED
import com.icare.utils.INVALID_TOKEN
import com.icare.utils.OK
import com.icare.utils.getUid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PharmacyServiceImpl : PharmacyService {
    @Autowired
    lateinit var repository: PharmacyRepository
    override fun addPharmacy(pharmacy: PharmacyModel): ResponseModel {
        return ResponseModel(repository.addPharmacy(pharmacy),"")
    }

    override fun getPharmacy(token: String): ResponseModel {
        try {
            if (getUid(token) == null) {
                return ResponseModel(status=INVALID_TOKEN)
            }else{
                return ResponseModel(status= OK, data = repository.getPharmacy())
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ResponseModel(status= FAILED)
        }

    }
}
