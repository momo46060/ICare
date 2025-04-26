package com.icare.service

import com.icare.model.ClinicModel
import com.icare.model.ConsultationModel
import com.icare.model.DoctorModel
import com.icare.model.DoctorSchedule
import com.icare.model.ResponseModel
import com.icare.model.TokenRequest
import com.icare.repository.ClinicRepository
import com.icare.utils.EMPTY_LIST
import com.icare.utils.FAILED
import com.icare.utils.INVALID_TOKEN
import com.icare.utils.OK
import com.icare.utils.getUid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClinicServiceImpl : ClinicService {
    @Autowired
    lateinit var repository: ClinicRepository

    override fun addClinic(clinic: ClinicModel): ResponseModel {
        return ResponseModel(repository.addClinic(clinic), "")
    }


    override fun getClinics(token: String): ResponseModel {
        val list = listOf<ClinicModel>()
        try {
            if (getUid(token) == null) {
                return ResponseModel(status = INVALID_TOKEN, data = listOf<ClinicModel>())
            } else if (repository.getClinics().isEmpty()) {
                return ResponseModel(status = EMPTY_LIST, data = listOf<ClinicModel>())
            } else {
                return ResponseModel(status = OK, data = repository.getClinics())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseModel(status = FAILED, data = listOf<ClinicModel>())
        }
    }

    override fun getDoctorSchedule(token: String): ResponseModel {
        return runCatching {
            getUid(token)?.let { uid ->
                ResponseModel(
                    status = OK,
                    data = repository.getDoctorSchedule(uid)
                )
            } ?: ResponseModel(status = INVALID_TOKEN, data = DoctorSchedule())
        }.getOrElse {
            ResponseModel(status = FAILED, data = DoctorSchedule())
        }
    }

    override fun getDoctors(token: String): ResponseModel {

        try {
            if (getUid(token) == null) {
                return ResponseModel(status = INVALID_TOKEN, data = listOf<DoctorModel>())
            } else if (repository.getDoctors().isEmpty()) {
                return ResponseModel(status = EMPTY_LIST, data = listOf<DoctorModel>())
            } else {
                return ResponseModel(status = OK, data = repository.getDoctors())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseModel(status = FAILED, data = listOf<DoctorModel>())
        }
    }

    override fun Consultation(consultationModel: ConsultationModel): ResponseModel {
        try {
            if (getUid(consultationModel.token) == null) {
                return ResponseModel(status = INVALID_TOKEN, data = listOf<DoctorModel>())
            } else {
                return ResponseModel(
                    status = repository.consultation(consultationModel),
                    data = repository.getDoctors()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseModel(status = FAILED, data = listOf<DoctorModel>())
        }
    }

    override fun getConsultationsByPrescriptionStatus(request: TokenRequest): ResponseModel {
        try {
            if (getUid(request.token) == null) {
                return ResponseModel(status = INVALID_TOKEN, data = listOf<ConsultationModel>())
            } else {
                return ResponseModel(
                    status = OK,
                    data = repository.getConsultationsByPrescriptionStatus(request.status)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseModel(status = FAILED, data = listOf<ConsultationModel>())
        }
    }

    override fun getConsultationsByLabTestStatus(request: TokenRequest): ResponseModel {
        try {
            if (getUid(request.token) == null) {
                return ResponseModel(status = INVALID_TOKEN, data = listOf<ConsultationModel>())
            } else {
                return ResponseModel(status = OK, data = repository.getConsultationsByLabTestStatus(request.status))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseModel(status = FAILED, data = listOf<ConsultationModel>())
        }
    }

    override fun getConsultationsByImaginingTestStatus(request: TokenRequest): ResponseModel {
        try {
            if (getUid(request.token) == null) {
                return ResponseModel(status = INVALID_TOKEN, data = listOf<ConsultationModel>())
            } else {
                return ResponseModel(
                    status = OK,
                    data = repository.getConsultationsByImaginingTestStatus(request.status)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseModel(status = FAILED, data = listOf<ConsultationModel>())
        }
    }
}