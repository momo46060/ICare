package com.icare.repository

import com.icare.model.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface Repository : JpaRepository<Users,String> {

    @Query(value = "SELECT * FROM users WHERE f_name = :userName", nativeQuery = true)
    fun findByUsername(@Param("userName") userName: String): Users?


}