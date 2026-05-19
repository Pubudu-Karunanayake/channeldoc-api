package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.DependentProfileRequestDto;
import com.medisync.channeldoc_api.dto.response.DependentProfileResponseDto;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.service.DependentProfileService;
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
@RequestMapping("/api/dependents")
@RequiredArgsConstructor
public class DependentProfileController {

    private final DependentProfileService dependentProfileService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<DependentProfileResponseDto> createDependentProfile(
            @Valid @RequestBody DependentProfileRequestDto request,
            @AuthenticationPrincipal User authenticatedUser) {
        DependentProfileResponseDto response = dependentProfileService.createDependentProfile(request, authenticatedUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
