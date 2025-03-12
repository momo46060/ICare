package com.icare.service

import com.icare.model.CenterStaffModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.ResponseModel
import com.icare.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl:UserService {

    @Autowired
    lateinit var repository: UserRepository

   override fun registerPatient(patient: PatientModel): ResponseModel {
           return repository.registerPatient(patient)
    }

    override fun registerDoctor(doctor: DoctorModel): ResponseModel {
        return repository.registerDoctor(doctor)
    }

    override fun registerCenterStaff(centerStaffModel: CenterStaffModel): ResponseModel {
        return repository.registerCenterStaff(centerStaffModel)
    }

}