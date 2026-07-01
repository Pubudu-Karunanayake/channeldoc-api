package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.DailySession;
import com.medisync.channeldoc_api.model.MasterSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DailySessionRepository extends JpaRepository<DailySession, Long> {
    boolean existsByMasterScheduleAndSessionDate(MasterSchedule masterSchedule, LocalDate sessionDate);

    java.util.List<DailySession> findByHospitalAndSessionDate(com.medisync.channeldoc_api.model.Hospital hospital, LocalDate sessionDate);

    java.util.Optional<DailySession> findByMasterScheduleIdAndSessionDateAndHospital(Long masterScheduleId, LocalDate sessionDate, com.medisync.channeldoc_api.model.Hospital hospital);

    @org.springframework.data.jpa.repository.Query("SELECT ds FROM DailySession ds JOIN FETCH ds.hospital WHERE ds.doctor.id = :doctorId AND ds.sessionDate BETWEEN :startDate AND :endDate AND ds.status = :status ORDER BY ds.sessionDate ASC")
    java.util.List<DailySession> findAvailableSessionsForDoctor(@org.springframework.data.repository.query.Param("doctorId") Long doctorId, @org.springframework.data.repository.query.Param("startDate") LocalDate startDate, @org.springframework.data.repository.query.Param("endDate") LocalDate endDate, @org.springframework.data.repository.query.Param("status") com.medisync.channeldoc_api.model.enums.SessionStatus status);
}

