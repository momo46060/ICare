package com.icare.model

data class ResponseModel(
    val status: Short,
    val message: String="",
    val data: Any? = null
)