package com.icare.repository

import com.icare.model.Appointment
import com.icare.utils.FAILED
import com.icare.utils.INVALID_TOKEN
import com.icare.utils.OK
import com.icare.utils.getImage
import com.icare.utils.getUid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class AppointmentRepositoryImpl: AppointmentsRepository {
    @Autowired
    lateinit var iCareJdbcTemplate: JdbcTemplate
    override fun insertAppointments(appointment: Appointment): Short {
        val sql = """
    MERGE INTO Appointments AS target
    USING (
        SELECT ? AS PatientID, ? AS DoctorID, ? AS AppointmentDate, ? As StatusID
    ) AS source
    ON target.PatientID = source.PatientID 
       AND target.DoctorID = source.DoctorID
       AND target.AppointmentDate = source.AppointmentDate
       AND target.StatusID = source.StatusID
    WHEN MATCHED THEN
        UPDATE SET 
            AppointmentDate = source.AppointmentDate -- ممكن تتعدل لو عندك حقل آخر
    WHEN NOT MATCHED BY TARGET THEN
        INSERT (PatientID, DoctorID, AppointmentDate, StatusID)
        VALUES (source.PatientID, source.DoctorID, source.AppointmentDate, source.StatusID);
""".trimIndent()
        if (getUid(appointment.token) == null){
            return INVALID_TOKEN
        }else{
            try {

                iCareJdbcTemplate.update(sql,
                    getUid(appointment.token),
                    appointment.doctorId,
                    appointment.appointmentTime,
                    appointment.statusId)
                return OK
            }catch (e:Exception){
                e.printStackTrace()
                return FAILED
            }
        }

    }

    override fun getPatientAppointments(uid: String): List<Appointment> {
        val sql="""
            SELECT a.PatientId       as patientId,
                   a.DoctorId        as doctorId,
                   a.AppointmentDate as appointmentTime,
                  
                   a.AppointmentID   as appointmentId,
                   a.StatusID        as statusId,
                   d.Specialization  as doctorSpecialty,
                   up.FirstName+' '+up.LastName            as patientName,
                   ud.FirstName+' '+ud.LastName            as doctorName
            FROM Appointments a
                     INNER JOIN Doctors d ON a.DoctorID = d.DoctorID
                     INNER JOIN Users up ON a.PatientId = up.UserID
                     INNER JOIN Users ud ON a.DoctorId = ud.UserID
            where a.patientId = '${uid}';
        """.trimIndent()
        try {
            return iCareJdbcTemplate.query(sql) { rs, _ ->
                Appointment(

                    patientId=rs.getString("patientId"),
                    doctorId=rs.getString("doctorId"),
                    appointmentTime=rs.getDate("appointmentTime").time,

                    appointmentId=rs.getLong("appointmentId"),
                    statusId=rs.getShort("statusId"),
                    doctorSpecialty=rs.getString("doctorSpecialty"),
                    patientName=rs.getString("patientName"),
                    doctorName=rs.getString("doctorName"),
                    doctorImage=getImage(rs.getString("doctorId")) ?:"",
                    patientImage=getImage(rs.getString("patientId")) ?:"",
                )
            }
        }catch (e:Exception){
            e.printStackTrace()
            return listOf()
        }

    }

    override fun getAppointmentsByStatus(status: Int): List<Appointment> {
        val sql="""
            SELECT a.PatientId       as patientId,
                   a.DoctorId        as doctorId,
                   a.AppointmentDate as appointmentTime,
                  
                   a.AppointmentID              as appointmentId,
                   a.StatusID        as statusId,
                   d.Specialization       as doctorSpecialty,
                   up.FirstName+' '+up.LastName            as patientName,
                   ud.FirstName+' '+ud.LastName            as doctorName
            FROM Appointments a
                     INNER JOIN Doctors d ON a.DoctorID = d.DoctorID
                     INNER JOIN Users up ON a.PatientId = up.UserID
                     INNER JOIN Users ud ON a.DoctorId = ud.UserID
            where status = ${status} ;
        """.trimIndent()
        try {
            return iCareJdbcTemplate.query(sql) { rs, _ ->
                Appointment(
                    token =rs.getString("patientId"),
                    doctorId=rs.getString("doctorId"),
                    appointmentTime=rs.getLong("appointmentTime"),

                    appointmentId=rs.getLong("appointmentId"),
                    statusId=rs.getShort("statusId"),
                    doctorSpecialty=rs.getString("doctorSpecialty"),
                    patientName=rs.getString("patientName"),
                    doctorName=rs.getString("doctorName"),
                    doctorImage=getImage(rs.getString("doctorId")) ?:"",
                    patientImage=getImage(rs.getString("patientId")) ?:"",
                )
            }
        }catch (e:Exception){
            e.printStackTrace()
            return listOf()
        }
    }
}