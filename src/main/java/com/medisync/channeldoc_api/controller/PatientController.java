package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.PatientProfileRequestDto;
import com.medisync.channeldoc_api.dto.response.PatientProfileResponseDto;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientProfileResponseDto> createPatientProfile(
            @Valid @RequestBody PatientProfileRequestDto request,
            @AuthenticationPrincipal User authenticatedUser) {
        PatientProfileResponseDto response = patientService.createPatientProfile(request, authenticatedUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
