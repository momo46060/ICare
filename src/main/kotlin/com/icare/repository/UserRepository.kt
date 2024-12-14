package com.icare.repository

import com.icare.model.Users

interface UserRepository {

    fun register(user: Users): Boolean



}