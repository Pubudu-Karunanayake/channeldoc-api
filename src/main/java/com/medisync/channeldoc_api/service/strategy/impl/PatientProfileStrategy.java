package com.medisync.channeldoc_api.service.strategy.impl;

import com.medisync.channeldoc_api.dto.response.HospitalResponseDto;
import com.medisync.channeldoc_api.dto.response.PatientProfileResponseDto;
import com.medisync.channeldoc_api.dto.response.UserProfileResponseDto;
import com.medisync.channeldoc_api.exception.ResourceNotFoundException;
import com.medisync.channeldoc_api.model.PatientProfile;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.UserRole;
import com.medisync.channeldoc_api.repository.PatientProfileRepository;
import com.medisync.channeldoc_api.service.strategy.UserProfileStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(2)
@RequiredArgsConstructor
public class PatientProfileStrategy implements UserProfileStrategy {

    private final PatientProfileRepository patientProfileRepository;

    @Override
    public boolean supports(Set<UserRole> roles) {
        return roles != null && roles.contains(UserRole.ROLE_PATIENT);
    }

    @Override
    public UserProfileResponseDto getProfile(User user) {
        PatientProfile patientProfile = patientProfileRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found for user id: " + user.getId()));

        HospitalResponseDto hospitalDto = null;
        if (user.getHospital() != null) {
            hospitalDto = HospitalResponseDto.builder()
                    .id(user.getHospital().getId())
                    .name(user.getHospital().getName())
                    .address(user.getHospital().getAddress())
                    .contactNumber(user.getHospital().getContactNumber())
                    .build();
        }

        return PatientProfileResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .roles(user.getRoles())
                .hospital(hospitalDto)
                .dateOfBirth(patientProfile.getDateOfBirth())
                .gender(patientProfile.getGender())
                .contactNumber(patientProfile.getContactNumber())
                .build();
    }
}
