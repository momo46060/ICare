package com.icare.repository

import com.icare.model.Appointment
import com.icare.model.ClinicModel
import com.icare.model.ConsultationModel
import com.icare.model.DoctorModel
import com.icare.model.DoctorSchedule
import com.icare.model.TimeSlot
import com.icare.utils.FAILED
import com.icare.utils.OK
import com.icare.utils.divide
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

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

    override fun getDoctorSchedule(uid: String): DoctorSchedule {
        val infoSql = """
            SELECT Price, From_Time, To_Time
            FROM Doctors
            INNER JOIN Users on UserID = DoctorID
            WHERE DoctorID = '$uid'
        """.trimIndent()
        val doctor = iCareJdbcTemplate.queryForObject(infoSql) { rs, _ ->
            DoctorModel(
                doctorID = uid,
                price = rs.getDouble("Price"),
                fromTime = rs.getLong("From_Time"),
                toTime = rs.getLong("To_Time")
            )
        } ?: DoctorModel()

        val patientsSql = """
            select count(*) from Appointments
            where DoctorID='$uid'
        """.trimIndent()
        val totalPatients = iCareJdbcTemplate.queryForObject(patientsSql) { rs, _ ->
            rs.getLong(1)
        } ?: 0

        val confirmedSql = """
            select count(*) from Appointments
            where DoctorID='$uid'
            and StatusID = 2
        """.trimIndent()
        val confirmed = iCareJdbcTemplate.queryForObject(confirmedSql) { rs, _ ->
            rs.getLong(1)
        } ?: 0

        val todaySql = """
            select count(*) from Appointments
            where DoctorID='$uid'
            and CAST(AppointmentDate AS DATE) = CAST(GETDATE() AS DATE)
        """.trimIndent()
        val todayAppointments = iCareJdbcTemplate.queryForObject(todaySql) { rs, _ ->
            rs.getLong(1)
        } ?: 0
        val timeSlotsCount = TimeSlot(startTime = doctor.fromTime, endTime = doctor.toTime).divide(
            slotDurationMinutes = 30,
        ).count()
        val availableSlots = (timeSlotsCount - todayAppointments).toShort()

        val appointmentsSql = """
            select P.FirstName + ' ' + P.LastName as 'PatientName', D.FirstName + ' ' + D.LastName as 'DoctorName',
             D2.Specialization,A.*
            from Appointments A
                     inner join Users P on P.UserID = A.PatientID
                     inner join Users D on D.UserID = A.DoctorID
                     inner join Doctors D2 on D2.DoctorID = A.DoctorID
            where A.DoctorID = '$uid'
        """.trimIndent()

        val appointments =
            iCareJdbcTemplate.query(appointmentsSql) { rs, _ ->
                Appointment(
                    appointmentId = rs.getLong("AppointmentID"),
                    patientId = rs.getString("PatientID"),
                    doctorId = rs.getString("DoctorID"),
                    appointmentTime = rs.getDate("AppointmentDate").time,
                    statusId = rs.getShort("StatusID"),
                    doctorSpecialty = rs.getString("Specialization"),
                    patientName = rs.getString("PatientName"),
                    doctorName = rs.getString("DoctorName"),
                )
            }

        return DoctorSchedule(
            totalPatients = totalPatients,
            confirmed = confirmed,
            price = doctor.price,
            availableSlots = availableSlots,
            appointments = appointments
        )
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
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val zoneId = ZoneId.systemDefault() // أو حدد المنطقة الزمنية لو عايز ZoneId.of("Africa/Cairo")

        val formattedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(consultation.date), zoneId)
            .format(formatter)
         val followUpDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(consultation.followUpDate), zoneId)
            .format(formatter)
        val sql = """
            MERGE INTO Consultations AS target
            USING (VALUES (
            '${consultation.consultationId}',
            '${consultation.appointment.doctorId}',
                '${consultation.appointment.patientId}',
                '${formattedDate}',
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
                '${followUpDate}'
            )) AS source (consultationId,DoctorID, PatientID, ConDateTime, Diagnosis, PharmacyID, Medications, PrescriptionStatus, LabCenterID, LabTests, LabTestStatus, ImagingCenterID, ImagingCenterTests, ImagingTestStatus, FollowUpDate)
            ON target.consultationId = source.consultationId   
            WHEN MATCHED THEN
                UPDATE SET 
                    ConDateTime = source.ConDateTime,
                    Diagnosis = source.Diagnosis,
                    PharmacyID = source.PharmacyID,
                    Medications = source.Medications,
                    PrescriptionStatus = source.PrescriptionStatus,
                    LabCenterID = source.LabCenterID,
                    LabTests = source.LabTests,
                    LabTestStatus = source.LabTestStatus,
                    ImagingCenterID = source.ImagingCenterID,
                    ImagingCenterTests = source.ImagingCenterTests,
                    ImagingTestStatus = source.ImagingTestStatus,
                    FollowUpDate = source.FollowUpDate
            WHEN NOT MATCHED THEN
                INSERT (DoctorID, PatientID, ConDateTime, Diagnosis, PharmacyID, Medications, PrescriptionStatus, LabCenterID, LabTests, LabTestStatus, ImagingCenterID, ImagingCenterTests, ImagingTestStatus, FollowUpDate)
                VALUES (source.DoctorID, source.PatientID, source.ConDateTime, source.Diagnosis, source.PharmacyID, source.Medications, source.PrescriptionStatus, source.LabCenterID, source.LabTests, source.LabTestStatus, source.ImagingCenterID, source.ImagingCenterTests, source.ImagingTestStatus, source.FollowUpDate);
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
       doc.Specialization,
       d.FirstName as 'doctor_first_name',
       d.LastName as doctor_last_name,
       p.FirstName as 'patient_first_name'
        , p.LastName as 'patient_last_name'
FROM Consultations c
         JOIN Users d ON c.DoctorID = d.UserID
         join Users p on c.PatientID = p.UserID
         join Doctors doc on doc.DoctorID = c.DoctorID
where c.PrescriptionStatus =  ${status};  
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            ConsultationModel(
                consultationId = rs.getLong("consultationId"),
                appointment = Appointment(
                    patientId = rs.getString("PatientID"),
                    doctorId = rs.getString("DoctorID"),
                    doctorSpecialty = rs.getString("Specialization"),
                  /*  statusId = rs.getShort("StatusID"), */
                    patientName = "${rs.getString("patient_first_name")} ${rs.getString("patient_last_name")}",
                    doctorName = "${rs.getString("doctor_first_name")} ${rs.getString("doctor_last_name")}"
             ),
                diagnosis = rs.getString("Diagnosis"),
                pharmacyId = rs.getLong("PharmacyID"),
                medications = rs.getString("Medications"),
                prescriptionsStatus = rs.getShort("PrescriptionStatus"),
                labCenterId = rs.getLong("LabCenterID"),
                labTest = rs.getString("LabTests"),
                labTestStatus = rs.getShort("LabTestStatus"),
                imagingCenterId = rs.getLong("ImagingCenterID"),
                imagingCenterTest = rs.getString("ImagingCenterTests"),
                imagingCenterStatus = rs.getShort("ImagingTestStatus"),
                followUpDate = rs.getDate("FollowUpDate").time,
                date = rs.getDate("ConDateTime").time,
            )
        }
    }

    override fun getConsultationsByLabTestStatus(status: Short): List<ConsultationModel> {
        val sql = """
      SELECT c.*,
       doc.Specialization,
       d.FirstName as 'doctor_first_name',
       d.LastName as doctor_last_name,
       p.FirstName as 'patient_first_name'
        , p.LastName as 'patient_last_name'
FROM Consultations c
         JOIN Users d ON c.DoctorID = d.UserID
         join Users p on c.PatientID = p.UserID
         join Doctors doc on doc.DoctorID = c.DoctorID
where c.LabTestStatus = $status; 
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            ConsultationModel(
                consultationId = rs.getLong("consultationId"),
                appointment = Appointment(
                    patientId = rs.getString("PatientID"),
                    doctorId = rs.getString("DoctorID"),
                    doctorSpecialty = rs.getString("Specialization"),
                    /*  statusId = rs.getShort("StatusID"), */
                    patientName = "${rs.getString("patient_first_name")} ${rs.getString("patient_last_name")}",
                    doctorName = "${rs.getString("doctor_first_name")} ${rs.getString("doctor_last_name")}"
                ),
                diagnosis = rs.getString("Diagnosis"),
                pharmacyId = rs.getLong("PharmacyID"),
                medications = rs.getString("Medications"),
                prescriptionsStatus = rs.getShort("PrescriptionStatus"),
                labCenterId = rs.getLong("LabCenterID"),
                labTest = rs.getString("LabTests"),
                labTestStatus = rs.getShort("LabTestStatus"),
                imagingCenterId = rs.getLong("ImagingCenterID"),
                imagingCenterTest = rs.getString("ImagingCenterTests"),
                imagingCenterStatus = rs.getShort("ImagingTestStatus"),
                followUpDate = rs.getDate("FollowUpDate").time,
                date = rs.getDate("ConDateTime").time,
            )
        }

    }

    override fun getConsultationsByImaginingTestStatus(status: Short): List<ConsultationModel> {
        val sql = """
         SELECT c.*,
       doc.Specialization,
       d.FirstName as 'doctor_first_name',
       d.LastName as doctor_last_name,
       p.FirstName as 'patient_first_name'
        , p.LastName as 'patient_last_name'
FROM Consultations c
         JOIN Users d ON c.DoctorID = d.UserID
         join Users p on c.PatientID = p.UserID
         join Doctors doc on doc.DoctorID = c.DoctorID
where c.ImagingTestStatus = $status;
        """.trimIndent()
        return iCareJdbcTemplate.query(sql) { rs, _ ->
            ConsultationModel(
                consultationId = rs.getLong("consultationId"),
                appointment = Appointment(
                    patientId = rs.getString("PatientID"),
                    doctorId = rs.getString("DoctorID"),
                    doctorSpecialty = rs.getString("Specialization"),
                    /*  statusId = rs.getShort("StatusID"), */
                    patientName = "${rs.getString("patient_first_name")} ${rs.getString("patient_last_name")}",
                    doctorName = "${rs.getString("doctor_first_name")} ${rs.getString("doctor_last_name")}"
                ),
                diagnosis = rs.getString("Diagnosis"),
                pharmacyId = rs.getLong("PharmacyID"),
                medications = rs.getString("Medications"),
                prescriptionsStatus = rs.getShort("PrescriptionStatus"),
                labCenterId = rs.getLong("LabCenterID"),
                labTest = rs.getString("LabTests"),
                labTestStatus = rs.getShort("LabTestStatus"),
                imagingCenterId = rs.getLong("ImagingCenterID"),
                imagingCenterTest = rs.getString("ImagingCenterTests"),
                imagingCenterStatus = rs.getShort("ImagingTestStatus"),
                followUpDate = rs.getDate("FollowUpDate").time,
                date = rs.getDate("ConDateTime").time,
            )
        }
    }

}