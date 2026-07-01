package com.medisync.channeldoc_api.repository;

import com.medisync.channeldoc_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.medisync.channeldoc_api.model.enums.UserRole;
import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH u.hospital WHERE u.hospital.id = :hospitalId AND r IN :roles")
    List<User> findStaffByHospitalIdAndRoles(@Param("hospitalId") Long hospitalId, @Param("roles") Collection<UserRole> roles);
}
