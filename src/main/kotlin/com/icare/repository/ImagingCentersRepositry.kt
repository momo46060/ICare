package com.icare.repository

import com.icare.model.ImagingCentersModel
import com.icare.model.ResponseModel

interface ImagingCentersRepositry {
    fun insertImagingCenter(imagingCenter: ImagingCentersModel): Short
}