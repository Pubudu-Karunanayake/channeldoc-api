package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.HospitalAdminRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalAdminResponseDto;

import com.medisync.channeldoc_api.model.User;

import java.util.List;

public interface HospitalAdminService {
    HospitalAdminResponseDto createHospitalAdmin(HospitalAdminRequestDto request);

    List<com.medisync.channeldoc_api.dto.response.HospitalMonthlyIncomeResponseDto> getHospitalMonthlyIncomeAnalysis(User user, int year, int month);
}
