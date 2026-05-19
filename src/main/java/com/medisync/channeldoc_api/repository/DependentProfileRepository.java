package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.DependentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DependentProfileRepository extends JpaRepository<DependentProfile, Long> {
    
}
