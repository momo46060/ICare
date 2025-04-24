package com.icare.service

import com.icare.model.CenterStaffModel
import com.icare.model.ClinicModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.PharmacistsModel
import com.icare.model.PharmacyModel
import com.icare.model.ResponseModel
import com.icare.model.TokenRequest
import com.icare.model.Users
import com.icare.repository.UserRepository
import com.icare.utils.EMPTY_LIST
import com.icare.utils.FAILED
import com.icare.utils.INVALID_TOKEN
import com.icare.utils.INVALID_USER
import com.icare.utils.OK
import com.icare.utils.getUid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var repository: UserRepository

    override fun getLoginInfo(request: TokenRequest): ResponseModel {
        return getUid(request.token)?.let { uid ->
            repository.getLoginInfo(uid)?.let { users ->
                ResponseModel(status = OK, data = users, message = "")
            } ?: ResponseModel(status = INVALID_USER)
        } ?: ResponseModel(status = INVALID_TOKEN)
    }

    override fun registerPatient(patient: PatientModel): ResponseModel {
        return ResponseModel(repository.registerPatient(patient), "")
    }

    override fun registerDoctor(doctor: DoctorModel): ResponseModel {
        return ResponseModel(repository.registerDoctor(doctor), "")
    }

    override fun registerCenterStaff(centerStaffModel: CenterStaffModel): ResponseModel {
        return repository.registerCenterStaff(centerStaffModel)
    }

    override fun registerPharmaciest(pharmaciests: PharmacistsModel): ResponseModel {
        return ResponseModel(repository.registerPharmaciest(pharmaciests), "")
    }


}