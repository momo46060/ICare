package com.icare.controller

import com.icare.model.CenterStaffModel
import com.icare.model.ClinicModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.PharmacyModel
import com.icare.model.ResponseModel
import com.icare.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class UserController {

    @Autowired
    lateinit var userService: UserService


    @PostMapping("/patient_register")
    fun patientRegister(@RequestBody patient: PatientModel): ResponseModel {
        return userService.registerPatient(patient)
    }

    @PostMapping("/doctor_register")
    fun DoctorRegister(@RequestBody doctor: DoctorModel): ResponseModel {
        return userService.registerDoctor(doctor)
    }

    @PostMapping("/centerStaff_register")
    fun centerStaffRegister(@RequestBody centerStaff: CenterStaffModel): ResponseModel {
        return userService.registerCenterStaff(centerStaff)
    }


    @PostMapping("/add_clinic")
    fun addClinic(@RequestBody clinicModel: ClinicModel): ResponseModel {
        return userService.addClinic(clinicModel)
    }
    @PostMapping("/addPharmacy")
    fun addPharmacy(@RequestBody pharmacyModel: PharmacyModel): ResponseModel {
        return userService.addPhamacy(pharmacyModel)
    }
    @PostMapping("/getclinics")
    fun getClinics (@RequestBody token: String): ResponseModel {
    return userService.getClinics(token)
    }

    @PostMapping("/getPharmacy")
    fun getPharmacy(@RequestBody token: String): ResponseModel {
        return userService.getPharmacy(token)
    }
}

