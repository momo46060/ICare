package com.icare.service

import com.icare.model.Users
import com.icare.repository.Repository
import com.icare.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserSevice {

    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var jpaRepository: Repository

    fun register(user: Users): Boolean {
            jpaRepository.save(user)
        println(
            jpaRepository.save(user)
        )

        return true
    }

}