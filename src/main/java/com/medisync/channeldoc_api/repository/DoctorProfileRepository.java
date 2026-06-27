package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.DoctorProfile;
import com.medisync.channeldoc_api.model.enums.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    boolean existsBySlmcNumber(String slmcNumber);

    Page<DoctorProfile> findBySpecialization(Specialization specialization, Pageable pageable);

    @Query("SELECT dp FROM DoctorProfile dp " +
           "JOIN FETCH dp.user u " +
           "LEFT JOIN FETCH u.hospital " +
           "WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<DoctorProfile> searchByUserFullName(@Param("name") String name);
}
