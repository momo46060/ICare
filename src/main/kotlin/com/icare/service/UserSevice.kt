package com.icare.service

import com.icare.model.User
import com.icare.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserSevice {

    @Autowired
    lateinit var repository: UserRepository

    fun register(user: User): Boolean = repository.register(user)

}