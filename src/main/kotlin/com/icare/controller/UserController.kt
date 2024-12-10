package com.icare.controller

import com.icare.model.User
import com.icare.service.UserSevice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    lateinit var userService:UserSevice

    @PostMapping("/register")
    fun register(@RequestBody user: User): Boolean{
        return userService.register(user)
    }

}