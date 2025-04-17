package com.icare.controller

import com.icare.model.ClinicModel
import com.icare.model.ResponseModel
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
    fun getClinics (@RequestBody token: String): ResponseModel {
        return service.getClinics(token)
    }

}