package com.icare.repository

import com.icare.model.*


interface UserRepository {

    fun getLoginInfo(uid:String): Users?

    fun registerPatient(patient: PatientModel): Short
    fun registerDoctor(doctor: DoctorModel): Short
    fun insertUser(user: Users): Boolean
    fun registerCenterStaff(centerStaff: CenterStaffModel): Short
    fun registerPharmacist(pharmacists: PharmacistsModel): Short
    fun getPharmacists(): List<PharmacistsModel>
    fun getClinicStaff(): List<ClinicStaffModel>
    fun getCenterStaff(): List<CenterStaffModel>
    fun registerClinicStaff(clinicStaff: ClinicStaffModel): Short

}