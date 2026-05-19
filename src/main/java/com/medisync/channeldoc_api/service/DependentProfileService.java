package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.model.DependentProfile;
import com.medisync.channeldoc_api.dto.request.DependentProfileRequestDto;
import com.medisync.channeldoc_api.dto.response.DependentProfileResponseDto;
import com.medisync.channeldoc_api.model.User;

public interface DependentProfileService {
    DependentProfileResponseDto createDependentProfile(DependentProfileRequestDto request, User authenticatedUser);
}
