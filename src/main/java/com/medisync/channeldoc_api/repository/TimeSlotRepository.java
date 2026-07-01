package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByDailySessionIdOrderBySlotTimeAsc(Long dailySessionId);
}
