package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.DoctorRequestDto;
import com.medisync.channeldoc_api.dto.response.DoctorResponseDto;
import com.medisync.channeldoc_api.dto.response.DoctorSearchResponseDto;
import com.medisync.channeldoc_api.dto.response.DoctorTimetableResponseDto;
import com.medisync.channeldoc_api.dto.response.RestPage;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.Specialization;
import com.medisync.channeldoc_api.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/search/name")
    public ResponseEntity<List<DoctorSearchResponseDto>> searchByName(@RequestParam String name) {
        List<DoctorSearchResponseDto> results = doctorService.searchByName(name);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/my-timetable")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<DoctorTimetableResponseDto>> getMyTimetable(
            @AuthenticationPrincipal User user) {
        List<DoctorTimetableResponseDto> timetable = doctorService.getMyTimetable(user);
        return ResponseEntity.ok(timetable);
    }

    @GetMapping("/my-income-analysis")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<com.medisync.channeldoc_api.dto.response.DoctorMonthlyIncomeResponseDto>> getMonthlyIncomeAnalysis(
            @AuthenticationPrincipal User user,
            @RequestParam int year,
            @RequestParam int month) {
        List<com.medisync.channeldoc_api.dto.response.DoctorMonthlyIncomeResponseDto> analysis = doctorService.getMonthlyIncomeAnalysis(user, year, month);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/{doctorId}/availability")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<com.medisync.channeldoc_api.dto.response.DoctorAvailabilityResponseDto>> getDoctorAvailability(@PathVariable Long doctorId) {
        List<com.medisync.channeldoc_api.dto.response.DoctorAvailabilityResponseDto> availability = doctorService.getDoctorAvailability(doctorId);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAnyRole('HOSPITAL_MANAGEMENT', 'HOSPITAL_ADMIN')")
    public ResponseEntity<List<DoctorSearchResponseDto>> getDoctorsByHospitalId(
            @PathVariable Long hospitalId,
            @AuthenticationPrincipal User user) {
        List<DoctorSearchResponseDto> doctors = doctorService.getDoctorsByHospitalId(hospitalId, user);
        return ResponseEntity.ok(doctors);
    }
}

