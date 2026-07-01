package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.response.UserProfileResponseDto;
import com.medisync.channeldoc_api.model.User;

public interface UserService {
    UserProfileResponseDto getCurrentUserProfile(User user);
}
