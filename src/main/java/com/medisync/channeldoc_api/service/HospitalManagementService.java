package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.HospitalManagementRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalManagementResponseDto;

public interface HospitalManagementService {
    HospitalManagementResponseDto createHospitalManagement(HospitalManagementRequestDto request);
}
