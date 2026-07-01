package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.HospitalRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalResponseDto;

import java.util.List;

public interface HospitalService {
    HospitalResponseDto createHospital(HospitalRequestDto requestDto);
    
    List<HospitalResponseDto> getAllHospitals();

    HospitalResponseDto getHospitalById(Long id);
}
