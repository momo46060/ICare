package com.icare.controller

import com.icare.model.CenterStaffModel
import com.icare.model.DoctorModel
import com.icare.model.PatientModel
import com.icare.model.PharmacistsModel
import com.icare.model.ResponseModel
import com.icare.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/userApi")
class UserController {

    @Autowired
    lateinit var userService: UserService


    @PostMapping("/patient_register")
    fun patientRegister(@RequestBody patient: PatientModel): ResponseModel {
        return userService.registerPatient(patient)
    }

    @PostMapping("/doctor_register")
    fun DoctorRegister(@RequestBody doctor: DoctorModel): ResponseModel {
        println(doctor)
        return userService.registerDoctor(doctor)
    }

    @PostMapping("/centerStaff_register")
    fun centerStaffRegister(@RequestBody centerStaff: CenterStaffModel): ResponseModel {
        return userService.registerCenterStaff(centerStaff)
    }

    @PostMapping("/pharmacist_register")
    fun pharmacistRegister(@RequestBody pharmacists: PharmacistsModel): ResponseModel {
        return userService.registerPharmaciest(pharmacists)
    }


}

