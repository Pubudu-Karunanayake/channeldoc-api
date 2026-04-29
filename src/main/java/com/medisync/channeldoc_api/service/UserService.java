package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.HospitalAdminRequestDto;
import com.medisync.channeldoc_api.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto createHospitalAdmin(HospitalAdminRequestDto request);
}
