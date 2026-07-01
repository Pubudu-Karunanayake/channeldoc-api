package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.service.UserService;
import org.springframework.stereotype.Service;

import com.medisync.channeldoc_api.dto.response.UserProfileResponseDto;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.service.strategy.UserProfileStrategyFactory;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileStrategyFactory strategyFactory;

    @Override
    public UserProfileResponseDto getCurrentUserProfile(User user) {
        return strategyFactory.getStrategy(user.getRoles()).getProfile(user);
    }
}
