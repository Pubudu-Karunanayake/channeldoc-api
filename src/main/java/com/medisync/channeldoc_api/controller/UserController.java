package com.medisync.channeldoc_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medisync.channeldoc_api.dto.response.UserProfileResponseDto;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponseDto> getCurrentUserProfile(@AuthenticationPrincipal User user) {
        UserProfileResponseDto profile = userService.getCurrentUserProfile(user);
        return ResponseEntity.ok(profile);
    }
}
