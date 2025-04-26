package com.icare.model

import java.util.Date

data class ConsultationModel(
    val token: String,
    val doctorId: String,
    val patientId: String,
    val date: Date,
    val diagnosis: String,
    val pharmacyId: String,
    val medications: String,
    val prescriptionsStatus: String,
    val labCenterId: String,
    val labTest: String,
    val labTestStatus: String,
    val imagingCenterId: String,
    val imagingCenterTest: String,
    val imagingCenterStatus: String,
    val followUpDate: Date,
)