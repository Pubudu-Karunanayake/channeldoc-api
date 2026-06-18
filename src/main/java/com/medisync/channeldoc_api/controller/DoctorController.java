package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.DoctorRequestDto;
import com.medisync.channeldoc_api.dto.response.DoctorResponseDto;
import com.medisync.channeldoc_api.dto.response.DoctorSearchResponseDto;
import com.medisync.channeldoc_api.dto.response.RestPage;
import com.medisync.channeldoc_api.model.enums.Specialization;
import com.medisync.channeldoc_api.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    //@PreAuthorize("hasRole('HOSPITAL_MANAGEMENT')")
    @PreAuthorize("hasAnyRole('HOSPITAL_MANAGEMENT', 'SUPER_ADMIN')")
    public ResponseEntity<DoctorResponseDto> createDoctor(@Valid @RequestBody DoctorRequestDto request) {
        DoctorResponseDto createdDoctor = doctorService.createDoctor(request);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<RestPage<DoctorSearchResponseDto>> searchBySpecialization(
            @RequestParam Specialization specialization,
            @PageableDefault(size = 10) Pageable pageable) {
        RestPage<DoctorSearchResponseDto> results = doctorService.searchBySpecialization(specialization, pageable);
        return ResponseEntity.ok(results);
    }
}
