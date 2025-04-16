package com.icare.service

import com.icare.model.CenterStaffModel
import com.icare.model.ClinicModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.PharmacyModel
import com.icare.model.ResponseModel
import com.icare.repository.UserRepository
import com.icare.utils.EMPTY_LIST
import com.icare.utils.FAILED
import com.icare.utils.INVALID_TOKEN
import com.icare.utils.OK
import com.icare.utils.getUid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl:UserService {

    @Autowired
    lateinit var repository: UserRepository

   override fun registerPatient(patient: PatientModel): ResponseModel {
           return ResponseModel(repository.registerPatient(patient),"")
    }

    override fun registerDoctor(doctor: DoctorModel): ResponseModel {
        return ResponseModel(repository.registerDoctor(doctor),"")
    }

    override fun registerCenterStaff(centerStaffModel: CenterStaffModel): ResponseModel {
        return repository.registerCenterStaff(centerStaffModel)
    }





}