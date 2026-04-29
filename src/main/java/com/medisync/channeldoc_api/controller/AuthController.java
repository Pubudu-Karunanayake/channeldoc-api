package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.GoogleAuthRequestDto;
import com.medisync.channeldoc_api.dto.response.AuthResponseDto;
import com.medisync.channeldoc_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user via Google Sign-In.
     * Expects a Google ID token from the frontend, verifies it server-side,
     * finds or creates the user, and returns an application JWT.
     */
    @PostMapping("/google")
    public ResponseEntity<AuthResponseDto> authenticateWithGoogle(
            @Valid @RequestBody GoogleAuthRequestDto request) {
                System.out.println("I am in controller");
        AuthResponseDto response = authService.authenticateWithGoogle(request);
        return ResponseEntity.ok(response);
    }
}
