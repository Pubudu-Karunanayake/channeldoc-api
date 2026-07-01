package com.medisync.channeldoc_api.service.strategy;

import com.medisync.channeldoc_api.model.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserProfileStrategyFactory {

    private final List<UserProfileStrategy> strategies;

    public UserProfileStrategy getStrategy(Set<UserRole> roles) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(roles))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No suitable profile strategy found for roles: " + roles));
    }
}
