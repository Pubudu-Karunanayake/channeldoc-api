package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.HospitalRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalResponseDto;

import java.util.List;

import com.medisync.channeldoc_api.dto.response.HospitalStaffResponseDto;

public interface HospitalService {
    HospitalResponseDto createHospital(HospitalRequestDto requestDto);
    
    List<HospitalResponseDto> getAllHospitals();

    HospitalResponseDto getHospitalById(Long id);

    HospitalResponseDto updateHospital(Long id, com.medisync.channeldoc_api.dto.request.HospitalUpdateRequestDto request);

    void deleteHospital(Long id);

    HospitalStaffResponseDto getHospitalStaff(Long hospitalId);
}
