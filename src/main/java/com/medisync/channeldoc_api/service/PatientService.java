package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.PatientProfileRequestDto;
import com.medisync.channeldoc_api.dto.response.PatientProfileResponseDto;
import com.medisync.channeldoc_api.model.User;

public interface PatientService {
    PatientProfileResponseDto createPatientProfile(PatientProfileRequestDto request, User authenticatedUser);
}
