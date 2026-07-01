package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.MasterSchedule;
import com.medisync.channeldoc_api.model.enums.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterScheduleRepository extends JpaRepository<MasterSchedule, Long> {
    List<MasterSchedule> findByDayAndIsActiveTrue(Day day);

    List<MasterSchedule> findByDoctorIdAndIsActiveTrue(Long doctorId);

    List<MasterSchedule> findByHospitalIdAndIsActiveTrue(Long hospitalId);
}

