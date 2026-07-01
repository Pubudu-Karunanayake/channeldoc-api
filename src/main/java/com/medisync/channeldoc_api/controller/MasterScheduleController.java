package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.MasterScheduleRequestDto;
import com.medisync.channeldoc_api.dto.response.MasterScheduleResponseDto;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.service.MasterScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/master-schedules")
@RequiredArgsConstructor
public class MasterScheduleController {

    private final MasterScheduleService masterScheduleService;

    @PostMapping
    //@PreAuthorize("hasRole('HOSPITAL_MANAGEMENT')")
    @PreAuthorize("hasAnyRole('HOSPITAL_MANAGEMENT', 'SUPER_ADMIN')")
    public ResponseEntity<MasterScheduleResponseDto> createMasterSchedule(
            @Valid @RequestBody MasterScheduleRequestDto request,
            @AuthenticationPrincipal User user) {
        MasterScheduleResponseDto response = masterScheduleService.createMasterSchedule(request, user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAnyRole('HOSPITAL_MANAGEMENT', 'HOSPITAL_ADMIN')")
    public ResponseEntity<List<MasterScheduleResponseDto>> getMasterSchedulesByHospital(
            @PathVariable Long hospitalId,
            @AuthenticationPrincipal User user) {
        List<MasterScheduleResponseDto> response = masterScheduleService.getSchedulesByHospitalId(hospitalId, user);
        return ResponseEntity.ok(response);
    }
}
