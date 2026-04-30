package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.request.DoctorRequestDto;
import com.medisync.channeldoc_api.dto.response.DoctorResponseDto;
import com.medisync.channeldoc_api.exception.ResourceNotFoundException;
import com.medisync.channeldoc_api.model.DoctorProfile;
import com.medisync.channeldoc_api.model.Hospital;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.AuthProvider;
import com.medisync.channeldoc_api.model.enums.UserRole;
import com.medisync.channeldoc_api.repository.DoctorProfileRepository;
import com.medisync.channeldoc_api.repository.HospitalRepository;
import com.medisync.channeldoc_api.repository.UserRepository;
import com.medisync.channeldoc_api.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Override
    @Transactional
    public DoctorResponseDto createDoctor(DoctorRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        if (doctorProfileRepository.existsBySlmcNumber(request.getSlmcNumber())) {
            throw new IllegalArgumentException("Doctor already exists with this SLMC number");
        }

        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + request.getHospitalId()));

        User newUser = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .roles(Set.of(UserRole.ROLE_DOCTOR))
                .hospital(hospital)
                .authProvider(AuthProvider.LOCAL)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(newUser);

        DoctorProfile doctorProfile = DoctorProfile.builder()
                .user(savedUser)
                .slmcNumber(request.getSlmcNumber())
                .specialization(request.getSpecialization())
                .build();

        DoctorProfile savedProfile = doctorProfileRepository.save(doctorProfile);

        return DoctorResponseDto.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .roles(savedUser.getRoles())
                .hospitalId(savedUser.getHospital() != null ? savedUser.getHospital().getId() : null)
                .isActive(savedUser.getIsActive())
                .slmcNumber(savedProfile.getSlmcNumber())
                .specialization(savedProfile.getSpecialization())
                .build();
    }
}
