package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.request.HospitalRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalResponseDto;
import com.medisync.channeldoc_api.model.Hospital;
import com.medisync.channeldoc_api.repository.HospitalRepository;
import com.medisync.channeldoc_api.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.medisync.channeldoc_api.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final com.medisync.channeldoc_api.repository.UserRepository userRepository;
    private final com.medisync.channeldoc_api.service.strategy.UserProfileStrategyFactory userProfileStrategyFactory;

    @Override
    public HospitalResponseDto createHospital(HospitalRequestDto requestDto) {
        Hospital hospital = Hospital.builder()
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .contactNumber(requestDto.getContactNumber())
                .build();

        Hospital savedHospital = hospitalRepository.save(hospital);

        return HospitalResponseDto.builder()
                .id(savedHospital.getId())
                .name(savedHospital.getName())
                .address(savedHospital.getAddress())
                .contactNumber(savedHospital.getContactNumber())
                .build();
    }

    @Override
    public java.util.List<HospitalResponseDto> getAllHospitals() {
        return hospitalRepository.findAll().stream()
                .map(hospital -> HospitalResponseDto.builder()
                        .id(hospital.getId())
                        .name(hospital.getName())
                        .address(hospital.getAddress())
                        .contactNumber(hospital.getContactNumber())
                        .build())
                .toList();
    }

    @Override
    public HospitalResponseDto getHospitalById(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));

        return HospitalResponseDto.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .contactNumber(hospital.getContactNumber())
                .build();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public HospitalResponseDto updateHospital(Long id, com.medisync.channeldoc_api.dto.request.HospitalUpdateRequestDto request) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));

        hospital.setName(request.getName());
        hospital.setAddress(request.getAddress());
        hospital.setContactNumber(request.getContactNumber());

        Hospital updatedHospital = hospitalRepository.save(hospital);

        return HospitalResponseDto.builder()
                .id(updatedHospital.getId())
                .name(updatedHospital.getName())
                .address(updatedHospital.getAddress())
                .contactNumber(updatedHospital.getContactNumber())
                .build();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteHospital(Long id) {
        if (!hospitalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hospital not found with id: " + id);
        }
        hospitalRepository.deleteById(id);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public com.medisync.channeldoc_api.dto.response.HospitalStaffResponseDto getHospitalStaff(Long hospitalId) {
        if (!hospitalRepository.existsById(hospitalId)) {
            throw new ResourceNotFoundException("Hospital not found with id: " + hospitalId);
        }

        java.util.List<com.medisync.channeldoc_api.model.User> staff = userRepository.findStaffByHospitalIdAndRoles(
                hospitalId,
                java.util.Arrays.asList(
                        com.medisync.channeldoc_api.model.enums.UserRole.ROLE_HOSPITAL_ADMIN,
                        com.medisync.channeldoc_api.model.enums.UserRole.ROLE_HOSPITAL_MANAGEMENT
                )
        );

        java.util.Map<Boolean, java.util.List<com.medisync.channeldoc_api.dto.response.UserProfileResponseDto>> partitionedStaff = staff.stream()
                .map(user -> userProfileStrategyFactory.getStrategy(user.getRoles()).getProfile(user))
                .collect(java.util.stream.Collectors.partitioningBy(
                        dto -> dto.getRoles().contains(com.medisync.channeldoc_api.model.enums.UserRole.ROLE_HOSPITAL_ADMIN)
                ));

        return com.medisync.channeldoc_api.dto.response.HospitalStaffResponseDto.builder()
                .admins(partitionedStaff.get(true))
                .management(partitionedStaff.get(false))
                .build();
    }
}
