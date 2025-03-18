package com.icare.service

import com.icare.model.CenterStaffModel
import com.icare.model.ClinicModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.PharmacyModel
import com.icare.model.ResponseModel
import com.icare.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl:UserService {

    @Autowired
    lateinit var repository: UserRepository

   override fun registerPatient(patient: PatientModel): ResponseModel {
           return ResponseModel(repository.registerPatient(patient),null)
    }

    override fun registerDoctor(doctor: DoctorModel): ResponseModel {
        return ResponseModel(repository.registerDoctor(doctor),null)
    }

    override fun registerCenterStaff(centerStaffModel: CenterStaffModel): ResponseModel {
        return repository.registerCenterStaff(centerStaffModel)
    }

    override fun addClinic(clinic: ClinicModel): ResponseModel {
        return ResponseModel(repository.addClinic(clinic),null)
    }

    override fun addPhamacy(pharmacy: PharmacyModel): ResponseModel {
       return ResponseModel(repository.addPharmacy(pharmacy),null)
    }

}