package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.request.DependentProfileRequestDto;
import com.medisync.channeldoc_api.dto.response.DependentProfileResponseDto;
import com.medisync.channeldoc_api.model.DependentProfile;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.repository.DependentProfileRepository;
import com.medisync.channeldoc_api.service.DependentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DependentProfileServiceImpl implements DependentProfileService {

    private final DependentProfileRepository dependentProfileRepository;

    @Override
    @Transactional
    public DependentProfileResponseDto createDependentProfile(DependentProfileRequestDto request, User authenticatedUser) {
        
        DependentProfile dependentProfile = DependentProfile.builder()
                .guarantor(authenticatedUser)
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .build();
                
        DependentProfile savedProfile = dependentProfileRepository.save(dependentProfile);

        return DependentProfileResponseDto.builder()
                .id(savedProfile.getId())
                .guarantorId(savedProfile.getGuarantor().getId())
                .fullName(savedProfile.getFullName())
                .dateOfBirth(savedProfile.getDateOfBirth())
                .gender(savedProfile.getGender())
                .build();
    }
}
