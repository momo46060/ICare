package com.icare.service

import com.icare.model.ImagingCentersModel
import com.icare.model.ResponseModel

interface ImagingCentersService {
    fun insertImagingCenter(imagingCenter: ImagingCentersModel): ResponseModel

}