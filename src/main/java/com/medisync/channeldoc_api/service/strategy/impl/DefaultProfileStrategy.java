package com.medisync.channeldoc_api.service.strategy.impl;

import com.medisync.channeldoc_api.dto.response.HospitalResponseDto;
import com.medisync.channeldoc_api.dto.response.UserProfileResponseDto;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.UserRole;
import com.medisync.channeldoc_api.service.strategy.UserProfileStrategy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(3)
public class DefaultProfileStrategy implements UserProfileStrategy {

    @Override
    public boolean supports(Set<UserRole> roles) {
        // Fallback strategy for all other users (Admin/Management)
        return true;
    }

    @Override
    public UserProfileResponseDto getProfile(User user) {
        HospitalResponseDto hospitalDto = null;
        if (user.getHospital() != null) {
            hospitalDto = HospitalResponseDto.builder()
                    .id(user.getHospital().getId())
                    .name(user.getHospital().getName())
                    .address(user.getHospital().getAddress())
                    .contactNumber(user.getHospital().getContactNumber())
                    .build();
        }

        return UserProfileResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .roles(user.getRoles())
                .hospital(hospitalDto)
                .build();
    }
}
