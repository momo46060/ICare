package com.icare.controller

import com.google.firebase.FirebaseApp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class Test {

//    @Autowired
//    lateinit var fireBaseInitializing: FirebaseApp

    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate

    @GetMapping("/test")
    fun testFun(): Map<String, String> {
        println(
            iCareJdbcTemplate.query(
                "SELECT * FROM C_User",
                { rs, rowNum ->
                    println("rowNum: $rowNum")
                    println("rs: ${rs.getString("first_name")}")
                }
            )
        )
        val myMap = mapOf(
            "key2" to "value2",
            "key3" to "value3"
        )
        return myMap
    }

}


