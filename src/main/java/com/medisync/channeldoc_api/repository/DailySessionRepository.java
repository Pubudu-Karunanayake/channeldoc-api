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
}

