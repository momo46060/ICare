package com.icare.model

import jakarta.persistence.Entity
import jakarta.persistence.Id


@Entity
class Users (
    @Id
    val userId: String = "",
    val fName: String = "",
    val lName: String = "",
    val nationalId: String = "",
    val passWord: String = "",
)