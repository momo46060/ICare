package com.icare.repository

import com.icare.model.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class UserRepositoryImpl : UserRepository {

    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate

    override fun register(user: Users) : Boolean{
        val sqlInsert = """ insert into C_User([user_id],[natioanl_id],[pass_word],[first_name],[last_name],[email],[address],[phone],[role_id])
        values ('${user.userId}','${user.nationalId}','${user.passWord}','${user.fName}','${user.lName}',' ',' ',' ',0);""".trimIndent()
            try {
                iCareJdbcTemplate.update(sqlInsert)
                return true
            }catch(e: Exception) {
                println(e.message)
                return false
            }
    }


}