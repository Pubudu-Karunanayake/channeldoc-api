package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.DoctorRequestDto;
import com.medisync.channeldoc_api.dto.response.DoctorResponseDto;
import com.medisync.channeldoc_api.service.DoctorService;
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
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    public ResponseEntity<DoctorResponseDto> createDoctor(@Valid @RequestBody DoctorRequestDto request) {
        DoctorResponseDto createdDoctor = doctorService.createDoctor(request);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }
}
