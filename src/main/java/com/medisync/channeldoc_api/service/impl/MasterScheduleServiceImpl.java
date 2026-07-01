package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.request.MasterScheduleRequestDto;
import com.medisync.channeldoc_api.dto.response.MasterScheduleResponseDto;
import com.medisync.channeldoc_api.exception.ResourceNotFoundException;
import com.medisync.channeldoc_api.model.DoctorProfile;
import com.medisync.channeldoc_api.model.Hospital;
import com.medisync.channeldoc_api.model.MasterSchedule;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.repository.DoctorProfileRepository;
import com.medisync.channeldoc_api.repository.MasterScheduleRepository;
import com.medisync.channeldoc_api.service.MasterScheduleService;
import com.medisync.channeldoc_api.service.SessionGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class MasterScheduleServiceImpl implements MasterScheduleService {

    private final MasterScheduleRepository masterScheduleRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final SessionGenerationService sessionGenerationService;

    @Override
    @Transactional
    public MasterScheduleResponseDto createMasterSchedule(MasterScheduleRequestDto request, User user) {
        Hospital hospital = user.getHospital();
        if (hospital == null) {
            throw new IllegalArgumentException("User is not associated with any hospital");
        }

        DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        MasterSchedule masterSchedule = MasterSchedule.builder()
                .hospital(hospital)
                .doctor(doctor)
                .day(request.getDay())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .timePerPatient(request.getTimePerPatient())
                .consultationFee(request.getConsultationFee())
                .hospitalSharePercentage(request.getHospitalSharePercentage())
                .build();

        MasterSchedule savedSchedule = masterScheduleRepository.save(masterSchedule);

        // Trigger initial 14-day session generation
        sessionGenerationService.generateSessionsForSchedule(savedSchedule, 14);

        return mapToResponseDto(savedSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MasterScheduleResponseDto> getSchedulesByHospitalId(Long hospitalId, User user) {
        Hospital userHospital = user.getHospital();
        if (userHospital == null || !userHospital.getId().equals(hospitalId)) {
            throw new AccessDeniedException("You do not have permission to access schedules for this hospital.");
        }

        List<MasterSchedule> schedules = masterScheduleRepository.findByHospitalIdAndIsActiveTrue(hospitalId);

        return schedules.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private MasterScheduleResponseDto mapToResponseDto(MasterSchedule masterSchedule) {
        return MasterScheduleResponseDto.builder()
                .id(masterSchedule.getId())
                .hospitalId(masterSchedule.getHospital().getId())
                .doctorId(masterSchedule.getDoctor().getId())
                .day(masterSchedule.getDay())
                .startTime(masterSchedule.getStartTime())
                .endTime(masterSchedule.getEndTime())
                .timePerPatient(masterSchedule.getTimePerPatient())
                .consultationFee(masterSchedule.getConsultationFee())
                .hospitalSharePercentage(masterSchedule.getHospitalSharePercentage())
                .build();
    }
}

