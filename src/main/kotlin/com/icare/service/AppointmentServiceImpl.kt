package com.icare.service



import com.icare.model.Appointment
import com.icare.model.ResponseModel
import com.icare.repository.AppointmentsRepository
import com.icare.utils.EMPTY_LIST
import com.icare.utils.INVALID_TOKEN
import com.icare.utils.OK
import com.icare.utils.getUid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AppointmentServiceImpl: AppointmentService {
    @Autowired
    lateinit var repository: AppointmentsRepository
    override fun bookAppointment(Appointments: Appointment): ResponseModel {
        return ResponseModel(repository.insertAppointments(Appointments),"")
    }

    override fun getPatientAppointments(token: String): ResponseModel {
        if (getUid(token) == null) {
            return ResponseModel(status= INVALID_TOKEN, message = "Unauthorized")
        }else if (repository.getPatientAppointments(getUid(token)!!).isEmpty()){
            return ResponseModel(status= EMPTY_LIST, data = listOf<Appointment>())
        }else{
            return ResponseModel(status=OK,data = repository.getPatientAppointments(getUid(token)!!))
        }
    }

    override fun getAppointmentsByStatus(status: Short,token: String): ResponseModel {
        if (getUid(token) == null) {
            return ResponseModel(status= INVALID_TOKEN, message = "Unauthorized")
        }else if (repository.getAppointmentsByStatus(status).isEmpty()){
            return ResponseModel(status= EMPTY_LIST, data = listOf<Appointment>())
        }else{
            return ResponseModel(status=OK,data = repository.getAppointmentsByStatus(status))
        }
    }


}