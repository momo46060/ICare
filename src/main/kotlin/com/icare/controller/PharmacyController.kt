package com.icare.controller

import com.icare.model.PharmacyModel
import com.icare.model.ResponseModel
import com.icare.model.TokenRequest
import com.icare.service.PharmacyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pharmacyApi")
class PharmacyController {
    @Autowired
     lateinit var service: PharmacyService

    @PostMapping("/addPharmacy")
    fun addPharmacy(@RequestBody pharmacyModel: PharmacyModel): ResponseModel {

        return service.addPhamacy(pharmacyModel)
    }

    @PostMapping("/getPharmacy")
    fun getPharmacy(@RequestBody request: TokenRequest): ResponseModel {
        return service.getPharmacy(request.token)
    }

    @PostMapping("/getPharmaciest")
    fun getPharmaciest(@RequestBody request: TokenRequest): ResponseModel {
        return service.getPharmaciest(request.token)
    }


}