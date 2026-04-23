package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
}
