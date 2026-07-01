package com.medisync.channeldoc_api.service.strategy;

import com.medisync.channeldoc_api.dto.response.UserProfileResponseDto;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.UserRole;

import java.util.Set;

public interface UserProfileStrategy {
    boolean supports(Set<UserRole> roles);
    UserProfileResponseDto getProfile(User user);
}
