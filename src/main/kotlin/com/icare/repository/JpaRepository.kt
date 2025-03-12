package com.icare.repository

import com.icare.model.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface Repository : JpaRepository<Users, String>{

    @Query("select * from users where national_id = :nationalId",nativeQuery = true)
    fun searchByNationalId(@Param("nationalId")nationalId: String): List<Users>
}