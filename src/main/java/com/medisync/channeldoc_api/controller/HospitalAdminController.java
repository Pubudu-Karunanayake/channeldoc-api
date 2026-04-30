package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.HospitalAdminRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalAdminResponseDto;
import com.medisync.channeldoc_api.service.HospitalAdminService;
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
@RequestMapping("/api/hospital-admins")
@RequiredArgsConstructor
public class HospitalAdminController {

    private final HospitalAdminService hospitalAdminService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<HospitalAdminResponseDto> createHospitalAdmin(@Valid @RequestBody HospitalAdminRequestDto request) {
        HospitalAdminResponseDto createdAdmin = hospitalAdminService.createHospitalAdmin(request);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }
}
