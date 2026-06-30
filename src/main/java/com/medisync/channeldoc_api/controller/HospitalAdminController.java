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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.medisync.channeldoc_api.model.User;
import java.util.List;
import com.medisync.channeldoc_api.dto.response.HospitalMonthlyIncomeResponseDto;

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

    @GetMapping("/income-analysis")
    @PreAuthorize("hasAnyRole('HOSPITAL_ADMIN', 'HOSPITAL_MANAGEMENT')")
    public ResponseEntity<List<HospitalMonthlyIncomeResponseDto>> getHospitalMonthlyIncomeAnalysis(
            @AuthenticationPrincipal User user,
            @RequestParam int year,
            @RequestParam int month) {
        List<HospitalMonthlyIncomeResponseDto> analysis = hospitalAdminService.getHospitalMonthlyIncomeAnalysis(user, year, month);
        return ResponseEntity.ok(analysis);
    }
}
