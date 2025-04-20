package com.icare.controller

import com.icare.model.ClinicModel
import com.icare.model.ResponseModel
import com.icare.model.TokenRequest
import com.icare.service.ClinicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clinicApi")
class ClinicController {
    @Autowired
    lateinit var service: ClinicService

    @PostMapping("/add_clinic")
    fun addClinic(@RequestBody clinicModel:  ClinicModel): ResponseModel {
        return service.addClinic(clinicModel)
    }

    @PostMapping("/getclinics")
    fun getClinics (@RequestBody request: TokenRequest): ResponseModel {
        return service.getClinics(request.token)
    }

    @PostMapping("/getdoctors")
    fun getDoctors (@RequestBody request: TokenRequest): ResponseModel {
        return service.getDoctors(request.token)
    }

}