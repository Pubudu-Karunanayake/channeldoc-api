package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.HospitalManagementRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalManagementResponseDto;
import com.medisync.channeldoc_api.service.HospitalManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hospital-management")
@RequiredArgsConstructor
public class HospitalManagementController {

    private final HospitalManagementService hospitalManagementService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<HospitalManagementResponseDto> createHospitalManagement(@Valid @RequestBody HospitalManagementRequestDto request) {
        HospitalManagementResponseDto createdManagement = hospitalManagementService.createHospitalManagement(request);
        return new ResponseEntity<>(createdManagement, HttpStatus.CREATED);
    }
}
