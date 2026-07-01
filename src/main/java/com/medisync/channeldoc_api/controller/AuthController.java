package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.GoogleAuthRequestDto;
import com.medisync.channeldoc_api.dto.request.PatientRegistrationRequestDto;
import com.medisync.channeldoc_api.dto.response.AuthResponseDto;
import com.medisync.channeldoc_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;
import java.util.Map;

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
        AuthResponseDto response = authService.authenticateWithGoogle(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Registers a new patient via Google Sign-Up.
     * Expects a Google ID token and additional patient profile details (DOB, gender, contact number).
     * Verifies the token, delegates user and profile creation to the service layer,
     * and returns an application JWT.
     */
    @PostMapping("/google/signup")
    public ResponseEntity<AuthResponseDto> registerPatientWithGoogle(
            @Valid @RequestBody PatientRegistrationRequestDto request) {
        AuthResponseDto response = authService.registerPatientWithGoogle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Logs out the user by invalidating their JWT token.
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            authService.logout(token);
        }
        
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}

