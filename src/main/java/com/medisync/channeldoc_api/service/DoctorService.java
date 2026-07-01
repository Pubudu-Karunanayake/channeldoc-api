package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.DoctorRequestDto;
import com.medisync.channeldoc_api.dto.response.DoctorResponseDto;
import com.medisync.channeldoc_api.dto.response.DoctorSearchResponseDto;
import com.medisync.channeldoc_api.dto.response.DoctorTimetableResponseDto;
import com.medisync.channeldoc_api.dto.response.RestPage;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.Specialization;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DoctorService {
    DoctorResponseDto createDoctor(DoctorRequestDto request);

    RestPage<DoctorSearchResponseDto> searchBySpecialization(Specialization specialization, Pageable pageable);

    List<DoctorSearchResponseDto> searchByName(String name);

    List<DoctorTimetableResponseDto> getMyTimetable(User user);

    List<com.medisync.channeldoc_api.dto.response.DoctorMonthlyIncomeResponseDto> getMonthlyIncomeAnalysis(User user, int year, int month);

    List<com.medisync.channeldoc_api.dto.response.DoctorAvailabilityResponseDto> getDoctorAvailability(Long doctorId);
}

