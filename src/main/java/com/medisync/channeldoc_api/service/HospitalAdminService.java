package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.HospitalAdminRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalAdminResponseDto;

public interface HospitalAdminService {
    HospitalAdminResponseDto createHospitalAdmin(HospitalAdminRequestDto request);
}
