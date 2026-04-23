package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.DailySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailySessionRepository extends JpaRepository<DailySession, Long> {
}
