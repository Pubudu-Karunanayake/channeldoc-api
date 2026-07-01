package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.Appointment;
import com.medisync.channeldoc_api.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Finds all non-cancelled appointments linked to a specific DailySession
     * through the Appointment → TimeSlot → DailySession relationship chain.
     */
    @Query("SELECT a FROM Appointment a WHERE a.timeSlot.dailySession.id = :dailySessionId AND a.status <> com.medisync.channeldoc_api.model.enums.AppointmentStatus.CANCELLED")
    List<Appointment> findActiveAppointmentsByDailySessionId(@Param("dailySessionId") Long dailySessionId);

    /**
     * Counts active (non-cancelled) appointments for a specific doctor
     * at a specific hospital on a given date.
     */
    long countByDoctorIdAndHospitalIdAndAppointmentDateAndStatusNot(
            Long doctorId, Long hospitalId, LocalDate appointmentDate, AppointmentStatus status);

    /**
     * Calculates the monthly income for a specific doctor, grouped by hospital.
     */
    @Query("SELECT NEW com.medisync.channeldoc_api.dto.response.DoctorMonthlyIncomeResponseDto(" +
           "  h.name, h.id, COUNT(a), ms.consultationFee, ms.hospitalSharePercentage" +
           ") " +
           "FROM Appointment a " +
           "JOIN a.timeSlot ts " +
           "JOIN ts.dailySession ds " +
           "JOIN ds.masterSchedule ms " +
           "JOIN ms.hospital h " +
           "WHERE a.doctor.id = :doctorId " +
           "  AND a.status <> com.medisync.channeldoc_api.model.enums.AppointmentStatus.CANCELLED " +
           "  AND YEAR(a.appointmentDate) = :year " +
           "  AND MONTH(a.appointmentDate) = :month " +
           "GROUP BY h.id, h.name, ms.consultationFee, ms.hospitalSharePercentage")
    List<com.medisync.channeldoc_api.dto.response.DoctorMonthlyIncomeResponseDto> findMonthlyIncomeByDoctor(
            @Param("doctorId") Long doctorId, @Param("year") int year, @Param("month") int month);

    /**
     * Calculates the monthly income for a specific hospital, grouped by doctor.
     */
    @Query("SELECT NEW com.medisync.channeldoc_api.dto.response.HospitalMonthlyIncomeResponseDto(" +
           "  u.fullName, d.id, COUNT(a), ms.consultationFee, ms.hospitalSharePercentage" +
           ") " +
           "FROM Appointment a " +
           "JOIN a.timeSlot ts " +
           "JOIN ts.dailySession ds " +
           "JOIN ds.masterSchedule ms " +
           "JOIN ms.doctor d " +
           "JOIN d.user u " +
           "WHERE a.hospital.id = :hospitalId " +
           "  AND a.status <> com.medisync.channeldoc_api.model.enums.AppointmentStatus.CANCELLED " +
           "  AND YEAR(a.appointmentDate) = :year " +
           "  AND MONTH(a.appointmentDate) = :month " +
           "GROUP BY d.id, u.fullName, ms.consultationFee, ms.hospitalSharePercentage")
    List<com.medisync.channeldoc_api.dto.response.HospitalMonthlyIncomeResponseDto> findMonthlyIncomeByHospital(
            @Param("hospitalId") Long hospitalId, @Param("year") int year, @Param("month") int month);

    /**
     * Retrieves the complete, paginated history of appointments for a specific patient.
     */
    @Query("SELECT NEW com.medisync.channeldoc_api.dto.response.PatientAppointmentHistoryResponseDto(" +
           "  a.appointmentNumber, a.appointmentDate, a.status, " +
           "  du.fullName, d.specialization, h.name, " +
           "  ms.startTime, ms.endTime, a.paymentAmount" +
           ") " +
           "FROM Appointment a " +
           "JOIN a.doctor d " +
           "JOIN d.user du " +
           "JOIN a.hospital h " +
           "JOIN a.timeSlot ts " +
           "JOIN ts.dailySession ds " +
           "JOIN ds.masterSchedule ms " +
           "WHERE a.patient.id = :patientId")
    org.springframework.data.domain.Page<com.medisync.channeldoc_api.dto.response.PatientAppointmentHistoryResponseDto> findAppointmentHistoryByPatientId(
            @Param("patientId") Long patientId, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.timeSlot ts JOIN FETCH a.patient p WHERE ts.dailySession.id = :sessionId")
    List<Appointment> findAppointmentsByDailySessionId(@Param("sessionId") Long sessionId);
}

