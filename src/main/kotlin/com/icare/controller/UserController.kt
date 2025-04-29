package com.icare.controller

import com.icare.model.*
import com.icare.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/userApi")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/loginInfo")
    fun getLoginInfo(@RequestBody request: TokenRequest): ResponseModel =
        userService.getLoginInfo(request)

    @PostMapping("/patientRegister")
    fun patientRegister(@RequestBody patient: PatientModel): ResponseModel {
        return userService.registerPatient(patient)
    }

    @PostMapping("/doctorRegister")
    fun doctorRegister(@RequestBody doctor: DoctorModel): ResponseModel {
        return userService.registerDoctor(doctor)
    }

    @PostMapping("/centerStaffRegister")
    fun centerStaffRegister(@RequestBody centerStaff: CenterStaffModel): ResponseModel {
        return userService.registerCenterStaff(centerStaff)
    }

    @PostMapping("/pharmacistRegister")
    fun pharmacistRegister(@RequestBody pharmacists: PharmacistsModel): ResponseModel {
        return userService.registerPharmacist(pharmacists)
    }

    @PostMapping("/getPharmacists")
    fun getPharmacists(@RequestBody request: TokenRequest): ResponseModel {
        return userService.getPharmacists(request.token)
    }

    @PostMapping("/getClinicStaff")
    fun getClinicStaff(@RequestBody request: TokenRequest): ResponseModel {
        return userService.getClinicStaff(request.token)
    }

    @PostMapping("/getCenterStaff")
    fun getCenterStaff(@RequestBody request: TokenRequest): ResponseModel {
        return userService.getCenterStaff(request.token)
    }

    @PostMapping("/clinicStaffRegister")
    fun clinicStaffRegister(@RequestBody clinicStaffModel: ClinicStaffModel): ResponseModel {
        return userService.registerClinicStaff(clinicStaffModel)
    }
}

