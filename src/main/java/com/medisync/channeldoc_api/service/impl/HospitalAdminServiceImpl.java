package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.request.HospitalAdminRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalAdminResponseDto;
import com.medisync.channeldoc_api.exception.ResourceNotFoundException;
import com.medisync.channeldoc_api.model.Hospital;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.AuthProvider;
import com.medisync.channeldoc_api.model.enums.UserRole;
import com.medisync.channeldoc_api.repository.HospitalRepository;
import com.medisync.channeldoc_api.repository.UserRepository;
import com.medisync.channeldoc_api.service.HospitalAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class HospitalAdminServiceImpl implements HospitalAdminService {

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final com.medisync.channeldoc_api.repository.AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public HospitalAdminResponseDto createHospitalAdmin(HospitalAdminRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + request.getHospitalId()));

        User newUser = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .roles(Set.of(UserRole.ROLE_HOSPITAL_ADMIN))
                .hospital(hospital)
                .authProvider(AuthProvider.LOCAL)
                .build();

        User savedUser = userRepository.save(newUser);

        return HospitalAdminResponseDto.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .roles(savedUser.getRoles())
                .hospitalId(savedUser.getHospital() != null ? savedUser.getHospital().getId() : null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<com.medisync.channeldoc_api.dto.response.HospitalMonthlyIncomeResponseDto> getHospitalMonthlyIncomeAnalysis(User user, int year, int month) {
        if (user.getHospital() == null) {
            throw new IllegalArgumentException("User is not associated with any hospital");
        }

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }

        java.util.List<com.medisync.channeldoc_api.dto.response.HospitalMonthlyIncomeResponseDto> analysis = appointmentRepository.findMonthlyIncomeByHospital(
                user.getHospital().getId(), year, month);

        return analysis.stream()
                .sorted(java.util.Comparator.comparing(
                        com.medisync.channeldoc_api.dto.response.HospitalMonthlyIncomeResponseDto::getTotalHospitalIncome).reversed())
                .toList();
    }
}
