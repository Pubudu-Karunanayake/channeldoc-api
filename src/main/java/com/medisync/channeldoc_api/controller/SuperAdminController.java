package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.HospitalRequestDto;
import com.medisync.channeldoc_api.dto.request.HospitalStaffUpdateRequestDto;
import com.medisync.channeldoc_api.dto.request.HospitalUpdateRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalResponseDto;
import com.medisync.channeldoc_api.dto.response.HospitalStaffResponseDto;
import com.medisync.channeldoc_api.dto.response.UserProfileResponseDto;
import com.medisync.channeldoc_api.service.HospitalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/hospitals")
@RequiredArgsConstructor
public class SuperAdminController {

    private final HospitalService hospitalService;

        @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<HospitalResponseDto> createHospital(@Valid @RequestBody HospitalRequestDto requestDto) {
        HospitalResponseDto responseDto = hospitalService.createHospital(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<java.util.List<HospitalResponseDto>> getAllHospitals() {
        java.util.List<HospitalResponseDto> hospitals = hospitalService.getAllHospitals();
        return ResponseEntity.ok(hospitals);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<HospitalResponseDto> getHospitalById(@PathVariable Long id) {
        HospitalResponseDto hospital = hospitalService.getHospitalById(id);
        return ResponseEntity.ok(hospital);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<HospitalResponseDto> updateHospital(
            @PathVariable Long id,
            @Valid @RequestBody HospitalUpdateRequestDto request) {
        HospitalResponseDto updatedHospital = hospitalService.updateHospital(id, request);
        return ResponseEntity.ok(updatedHospital);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        hospitalService.deleteHospital(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/staff/{staffId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserProfileResponseDto> updateHospitalStaff(
            @PathVariable Long staffId,
            @Valid @RequestBody HospitalStaffUpdateRequestDto request) {
        UserProfileResponseDto updatedStaff = hospitalService.updateHospitalStaff(staffId, request);
        return ResponseEntity.ok(updatedStaff);
    }

    @GetMapping("/{hospitalId}/staff")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<HospitalStaffResponseDto> getHospitalStaff(@PathVariable Long hospitalId) {
        HospitalStaffResponseDto staff = hospitalService.getHospitalStaff(hospitalId);
        return ResponseEntity.ok(staff);
    }
}
