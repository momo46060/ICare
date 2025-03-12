package com.icare.controller

import com.icare.model.PatientModel
import com.icare.model.ResponseModel
import com.icare.service.UserSevice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class UserController {

    @Autowired
    lateinit var userService: UserSevice


    @PostMapping("/patient_register")
    fun register(@RequestBody patient: PatientModel): ResponseModel{
        return userService.registerUser(patient)
    }


}

