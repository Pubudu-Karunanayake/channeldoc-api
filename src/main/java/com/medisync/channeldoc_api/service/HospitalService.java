package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.HospitalRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalResponseDto;

public interface HospitalService {
    HospitalResponseDto createHospital(HospitalRequestDto requestDto);
}
