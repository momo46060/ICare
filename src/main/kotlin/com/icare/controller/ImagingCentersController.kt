package com.icare.controller

import com.icare.model.ImagingCentersModel
import com.icare.model.ResponseModel
import com.icare.service.ImagingCentersService
import com.icare.service.PharmacyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/imagingCentersApi")
class ImagingCentersController {
    @Autowired
    lateinit var service: ImagingCentersService
    @PostMapping("/add_imaging_center")
    fun addImagingCenter(@RequestBody imagingCenter: ImagingCentersModel): ResponseModel {
        return service.insertImagingCenter(imagingCenter)
    }
}