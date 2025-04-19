package com.icare.repository

import com.icare.model.PharmacistsModel
import com.icare.model.PharmacyModel

interface PharmacyRepository {
    fun addPharmacy(pharmacy: PharmacyModel): Short
    fun getPharmacy(): List<PharmacyModel>
    fun getPharmaciest(): List<PharmacistsModel>
}