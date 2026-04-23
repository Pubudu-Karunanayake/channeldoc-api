package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.MasterSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterScheduleRepository extends JpaRepository<MasterSchedule, Long> {
}
