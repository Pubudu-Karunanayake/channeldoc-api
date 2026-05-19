package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.request.PatientProfileRequestDto;
import com.medisync.channeldoc_api.dto.response.PatientProfileResponseDto;
import com.medisync.channeldoc_api.model.PatientProfile;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.repository.PatientProfileRepository;
import com.medisync.channeldoc_api.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientProfileRepository patientProfileRepository;

    @Override
    @Transactional
    public PatientProfileResponseDto createPatientProfile(PatientProfileRequestDto request, User authenticatedUser) {
        // Prevent duplicate profile creation
        if (patientProfileRepository.existsByUserId(authenticatedUser.getId())) {
            throw new IllegalArgumentException("Patient profile already exists for this user");
        }

        PatientProfile patientProfile = PatientProfile.builder()
                .user(authenticatedUser)
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .contactNumber(request.getContactNumber())
                .build();

        PatientProfile savedProfile = patientProfileRepository.save(patientProfile);
        log.info("Created patient profile for user: {}", authenticatedUser.getEmail());

        return PatientProfileResponseDto.builder()
                .userId(authenticatedUser.getId())
                .fullName(authenticatedUser.getFullName())
                .email(authenticatedUser.getEmail())
                .profileImageUrl(authenticatedUser.getProfileImageUrl())
                .dateOfBirth(savedProfile.getDateOfBirth())
                .gender(savedProfile.getGender())
                .contactNumber(savedProfile.getContactNumber())
                .build();
    }
}
