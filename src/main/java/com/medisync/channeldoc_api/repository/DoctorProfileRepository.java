package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.DoctorProfile;
import com.medisync.channeldoc_api.model.enums.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    boolean existsBySlmcNumber(String slmcNumber);

    Page<DoctorProfile> findBySpecialization(Specialization specialization, Pageable pageable);
}
