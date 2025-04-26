package eg.edu.cu.csds.icare.core.domain.model

import com.icare.model.Appointment

data class Consultation(
    val token: String = "",
    val id: Long = 0,
    val appointment: Appointment = Appointment(),
    val dateTime: Long = 0,
    val diagnosis: String = "",
    val pharmacyId: Long = 1,
    val medications: String = "",
    val prescriptionStatusId: Short = 1,
    val labCenterId: Long = 1,
    val labTests: String = "",


    val labTestStatusId: Short = 1,
    val imagingCenterId: Long = 1,
    val imagingTests: String = "",
    val imgTestStatusId: Short = 1,
    val followUpdDate: Long = 0,
    val readOnly: Boolean = true,
)
