package com.icare.repository

import com.icare.model.User

interface UserRepository {

    fun register(user: User): Boolean



}