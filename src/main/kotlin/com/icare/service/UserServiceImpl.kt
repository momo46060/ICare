package com.icare.service

import com.icare.model.*
import com.icare.repository.UserRepository
import com.icare.utils.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var repository: UserRepository

    override fun getLoginInfo(request: TokenRequest): ResponseModel =
        getUid(request.token)?.let { uid ->
            repository.getLoginInfo(uid)?.let { users ->
                ResponseModel(status = OK, data = users, message = "")
            } ?: ResponseModel(status = INVALID_USER, data = Users())
        } ?: ResponseModel(status = INVALID_TOKEN, data = Users())

    override fun registerPatient(patient: PatientModel): ResponseModel {
        return ResponseModel(repository.registerPatient(patient), "")
    }

    override fun registerDoctor(doctor: DoctorModel): ResponseModel {
        return ResponseModel(repository.registerDoctor(doctor), "")
    }

    override fun registerCenterStaff(centerStaffModel: CenterStaffModel): ResponseModel {
        return repository.registerCenterStaff(centerStaffModel)
    }

    override fun registerPharmacist(pharmacists: PharmacistsModel): ResponseModel {
        return ResponseModel(repository.registerPharmacist(pharmacists), "")
    }

    override fun getPharmacists(token: String): ResponseModel {
        try {
            if (getUid(token) == null) {
                return ResponseModel(status = INVALID_TOKEN)
            } else {
                return ResponseModel(status = OK, data = repository.getPharmacists())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseModel(status = FAILED)
        }
    }

    override fun getClinicStaff(token: String): ResponseModel =
        getUid(token)?.let { uid ->
            ResponseModel(status = OK, data = repository.getClinicStaff(), message = "")
        } ?: ResponseModel(status = INVALID_TOKEN, data = listOf(ClinicStaffModel()))

    override fun getCenterStaff(token: String): ResponseModel =
        getUid(token)?.let { uid ->
            ResponseModel(status = OK, data = repository.getCenterStaff(), message = "")
        } ?: ResponseModel(status = INVALID_TOKEN, data = listOf(CenterStaffModel()))

    override fun registerClinicStaff(clinicStaff: ClinicStaffModel): ResponseModel =
        ResponseModel(status = repository.registerClinicStaff(clinicStaff))
}