package com.icare.model


data class ConsultationModel(
    val consultationId: Long=0,
    val token: String="",
    val appointment: Appointment = Appointment(),
    val date: Long=0,
    val diagnosis: String="",
    val pharmacyId: Long=0,
    val medications: String="",
    val prescriptionsStatus: Short=0,
    val labCenterId: Long=0,
    val labTest: String="",
    val labTestStatus: Short=0,
    val imagingCenterId: Long=0,
    val imagingCenterTest: String="",
    val imagingCenterStatus: Short=0,
    val followUpDate: Long=0,
)