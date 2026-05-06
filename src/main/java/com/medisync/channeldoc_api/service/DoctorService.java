package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.DoctorRequestDto;
import com.medisync.channeldoc_api.dto.response.DoctorResponseDto;

public interface DoctorService {
    DoctorResponseDto createDoctor(DoctorRequestDto request);
}
