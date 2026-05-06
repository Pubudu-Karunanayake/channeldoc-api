package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.request.HospitalManagementRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalManagementResponseDto;
import com.medisync.channeldoc_api.exception.ResourceNotFoundException;
import com.medisync.channeldoc_api.model.Hospital;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.AuthProvider;
import com.medisync.channeldoc_api.model.enums.UserRole;
import com.medisync.channeldoc_api.repository.HospitalRepository;
import com.medisync.channeldoc_api.repository.UserRepository;
import com.medisync.channeldoc_api.service.HospitalManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class HospitalManagementServiceImpl implements HospitalManagementService {

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    @Transactional
    public HospitalManagementResponseDto createHospitalManagement(HospitalManagementRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + request.getHospitalId()));

        User newUser = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .roles(Set.of(UserRole.ROLE_HOSPITAL_MANAGEMENT))
                .hospital(hospital)
                .authProvider(AuthProvider.LOCAL)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(newUser);

        return HospitalManagementResponseDto.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .roles(savedUser.getRoles())
                .hospitalId(savedUser.getHospital() != null ? savedUser.getHospital().getId() : null)
                .isActive(savedUser.getIsActive())
                .build();
    }
}
