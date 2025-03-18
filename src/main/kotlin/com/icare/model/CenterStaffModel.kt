package com.icare.model


data class CenterStaffModel (
    val token:String = "",
    val StaffID:String="",
    val LabCenterID :Int=0,
    val RoleID :Int=0,
    val FirstName:String="",
    val LastName:String="",
    val Email:String="",
    val BirthDate:String="",
    val Gender:String="",
    val IsActive:Boolean=false,
)