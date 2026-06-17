package com.medisync.channeldoc_api.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.medisync.channeldoc_api.dto.request.PatientRegistrationRequestDto;
import com.medisync.channeldoc_api.exception.UserAlreadyExistsException;
import com.medisync.channeldoc_api.model.PatientProfile;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.AuthProvider;
import com.medisync.channeldoc_api.model.enums.UserRole;
import com.medisync.channeldoc_api.repository.PatientProfileRepository;
import com.medisync.channeldoc_api.repository.UserRepository;
import com.medisync.channeldoc_api.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;

    @Override
    @Transactional
    public User registerPatientViaGoogle(GoogleIdToken.Payload payload, PatientRegistrationRequestDto request) {
        String email = payload.getEmail();

        // 1. Validate email uniqueness
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User already registered. Please log in");
        }

        // 2. Create and persist User entity
        User newUser = User.builder()
                .googleId(payload.getSubject())
                .email(email)
                .fullName(request.getName()) // Use name from DTO instead of Google's name
                .profileImageUrl((String) payload.get("picture"))
                .authProvider(AuthProvider.GOOGLE)
                .roles(Set.of(UserRole.ROLE_PATIENT))
                .isActive(true)
                .build();

        User savedUser = userRepository.save(newUser);
        log.info("Created new patient user via Google Sign-Up: {}", savedUser.getEmail());

        // 3. Create and persist PatientProfile entity
        PatientProfile patientProfile = PatientProfile.builder()
                .user(savedUser)
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .contactNumber(request.getContactNumber())
                .build();

        patientProfileRepository.save(patientProfile);
        log.info("Created patient profile for user: {}", savedUser.getEmail());

        return savedUser;
    }
}

