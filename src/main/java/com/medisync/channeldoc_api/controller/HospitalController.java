package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.HospitalRequestDto;
import com.medisync.channeldoc_api.dto.request.HospitalUpdateRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalResponseDto;
import com.medisync.channeldoc_api.service.HospitalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import com.medisync.channeldoc_api.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.medisync.channeldoc_api.dto.response.HospitalStaffResponseDto;
@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping("/my-hospital")
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    public ResponseEntity<HospitalResponseDto> getMyHospitalDetails(@AuthenticationPrincipal User user) {
        if (user.getHospital() == null) {
            throw new IllegalStateException("User is not associated with any hospital");
        }
        HospitalResponseDto hospital = hospitalService.getHospitalById(user.getHospital().getId());
        return ResponseEntity.ok(hospital);
    }

    @PutMapping("/my-hospital")
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    public ResponseEntity<HospitalResponseDto> updateMyHospitalDetails(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody HospitalUpdateRequestDto request) {
        if (user.getHospital() == null) {
            throw new IllegalStateException("User is not associated with any hospital");
        }
        HospitalResponseDto updatedHospital = hospitalService.updateHospital(user.getHospital().getId(), request);
        return ResponseEntity.ok(updatedHospital);
    }

}
