package com.icare.service

import com.icare.model.PharmacyModel
import com.icare.model.ResponseModel

interface PharmacyService {
    fun addPhamacy(pharmacy: PharmacyModel): ResponseModel
    fun getPharmacy(token: String): ResponseModel
}