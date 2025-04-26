package com.icare.repository

import com.icare.model.Appointment
import com.icare.model.ClinicModel
import com.icare.model.ConsultationModel
import com.icare.model.DoctorModel
import com.icare.utils.FAILED
import com.icare.utils.OK
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ClinicRepositoryImpl : ClinicRepository {

    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate

    override fun addClinic(clinic: ClinicModel): Short {
        val sql = """
            MERGE INTO Clinics AS target
            USING (VALUES ('${clinic.clinicID}',
                '${clinic.clinicName}',
                '${clinic.clinicType}',
                '${clinic.phone}',
                '${clinic.clinicLocaltion}',
                '${clinic.isOpen}'
            )) AS source (ClinicID,Clinic_Name, ClinicType, Phone, ClinicLocation, IsOpen)
            ON target.ClinicID = source.clinicID  
            WHEN MATCHED THEN
                UPDATE SET 
                    Clinic_Name = source.Clinic_Name, 
                    ClinicType = source.ClinicType,
                    Phone = source.Phone,
                    ClinicLocation = source.ClinicLocation,
                    IsOpen = source.IsOpen
            WHEN NOT MATCHED THEN
                INSERT (Clinic_Name, ClinicType, Phone, ClinicLocation, IsOpen)
                VALUES (source.Clinic_Name, source.ClinicType, source.Phone, source.ClinicLocation, source.IsOpen);

        """.trimIndent()
        try {
            iCareJdbcTemplate.update(sql)
            return OK
        } catch (e: Exception) {
            println(e.stackTrace)
            println(e.message)
            return FAILED
        }
    }

    override fun getClinics(): List<ClinicModel> {
        val sql = """select * from Clinics""".trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            ClinicModel(
                clinicID = rs.getInt("ClinicID"),
                clinicName = rs.getString("Clinic_Name"),
                clinicType = rs.getString("ClinicType"),
                phone = rs.getString("Phone"),
                clinicLocaltion = rs.getString("ClinicLocation"),
                isOpen = rs.getBoolean("IsOpen")
            )
        }
    }

    override fun getDoctors(): List<DoctorModel> {
        val sql = """
            select D.DoctorID,U.FirstName,U.LastName,U.Email,U.IsActive,D.Specialization,
                   D.From_Time,D.To_Time,D.ClinicID,U.phone,D.Price,D.Rating
            from Doctors D
            join Users U
            on D.DoctorID = U.UserID
        """.trimIndent()

        return iCareJdbcTemplate.query(sql) { rs, _ ->

            DoctorModel(
                doctorID = rs.getString("DoctorID"),
                fname = rs.getString("FirstName"),
                lname = rs.getString("LastName"),
                email = rs.getString("Email"),
                isActive = rs.getBoolean("IsActive"),
                specialization = rs.getString("Specialization"),
                fromTime = rs.getLong("From_Time"),
                toTime = rs.getLong("To_Time"),
                clinicId = rs.getString("ClinicID"),
                phoneNumber = rs.getString("phone"),
                price = rs.getDouble("Price"),
                rating = rs.getDouble("Rating")
            )
        }
    }

    override fun consultation(consultation: ConsultationModel): Short {
        val sql = """
            MERGE INTO Consultations AS target
            USING (VALUES ('${consultation.appointment.doctorId}',
                '${consultation.appointment.patientId}',
                '${consultation.date}',
                '${consultation.diagnosis}',
                '${consultation.pharmacyId}',
                '${consultation.medications}',
                '${consultation.prescriptionsStatus}',
                '${consultation.labCenterId}',
                '${consultation.labTest}',
                '${consultation.labTestStatus}',
                '${consultation.imagingCenterId}',
                '${consultation.imagingCenterTest}',
                '${consultation.imagingCenterStatus}',
                '${consultation.followUpDate}'
            )) AS source (DoctorID, PatientID, Date, Diagnosis, PharmacyID, Medications, PrescriptionsStatus, LabCenterID, LabTest, LabTestStatus, ImagingCenterID, ImagingCenterTest, ImagingCenterStatus, FollowUpDate)
            ON target.DoctorID = source.DoctorID AND target.PatientID = source.PatientID  
            WHEN MATCHED THEN
                UPDATE SET 
                    Date = source.Date,
                    Diagnosis = source.Diagnosis,
                    PharmacyID = source.PharmacyID,
                    Medications = source.Medications,
                    PrescriptionsStatus = source.PrescriptionsStatus,
                    LabCenterID = source.LabCenterID,
                    LabTest = source.LabTest,
                    LabTestStatus = source.LabTestStatus,
                    ImagingCenterID = source.ImagingCenterID,
                    ImagingCenterTest = source.ImagingCenterTest,
                    ImagingCenterStatus = source.ImagingCenterStatus,
                    FollowUpDate = source.FollowUpDate
            WHEN NOT MATCHED THEN
                INSERT (DoctorID, PatientID, Date, Diagnosis, PharmacyID, Medications, PrescriptionsStatus, LabCenterID, LabTest, LabTestStatus, ImagingCenterID, ImagingCenterTest, ImagingCenterStatus, FollowUpDate)
                VALUES (source.DoctorID, source.PatientID, source.Date, source.Diagnosis, source.PharmacyID, source.Medications, source.PrescriptionsStatus, source.LabCenterID, source.LabTest, source.LabTestStatus, source.ImagingCenterID, source.ImagingCenterTest, source.ImagingCenterStatus, source.FollowUpDate);
        """.trimIndent()
        try {
            iCareJdbcTemplate.update(sql)
            return OK
        } catch (e: Exception) {
            println(e.stackTrace)
            println(e.message)
            return FAILED
        }
    }

    override fun getConsultationsByPrescriptionStatus(status: Short): List<ConsultationModel> {
        val sql = """
          SELECT c.*,
       d.FirstName as 'doctor_first_name',
       d.LastName as doctor_last_name,
       p.FirstName as 'patient_first_name'
     , p.LastName as 'patient_last_name'
FROM Consultations c
JOIN Users d ON c.DoctorID = d.UserID
join Users p on c.PatientID = p.UserID
where c.PrescriptionStatus = 1;  
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            ConsultationModel(
                appointment = Appointment(
                    token = rs.getString("Token"),
                    patientId = rs.getString("PatientID"),
                    doctorId = rs.getString("DoctorID"),
                    appointmentTime = rs.getLong("Date"),
                    appointmentId = rs.getLong("AppointmentID"),
                    statusId = rs.getShort("StatusID"),
                    doctorSpecialty = rs.getString("doctor_specialty"),
                    patientName = "${rs.getString("patient_first_name")} ${rs.getString("patient_last_name")}",
                    doctorName = "${rs.getString("doctor_first_name")} ${rs.getString("doctor_last_name")}"
                ),
                diagnosis = rs.getString("Diagnosis"),
                pharmacyId = rs.getLong("PharmacyID"),
                medications = rs.getString("Medications"),
                prescriptionsStatus = rs.getShort("PrescriptionsStatus"),
                labCenterId = rs.getLong("LabCenterID"),
                labTest = rs.getString("LabTest"),
                labTestStatus = rs.getShort("LabTestStatus"),
                imagingCenterId = rs.getLong("ImagingCenterID"),
                imagingCenterTest = rs.getString("ImagingCenterTest"),
                imagingCenterStatus = rs.getShort("ImagingCenterStatus"),
                followUpDate = rs.getLong("FollowUpDate"),
                date = rs.getDate("Date").time,
            )
        }
    }

    override fun getConsultationsByLabTestStatus(status: Short): List<ConsultationModel> {
        TODO("Not yet implemented")
    }

    override fun getConsultationsByImaginingTestStatus(status: Short): List<ConsultationModel> {
        TODO("Not yet implemented")
    }

}