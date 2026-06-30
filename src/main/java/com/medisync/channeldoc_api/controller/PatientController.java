package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medisync.channeldoc_api.dto.response.PatientAppointmentHistoryResponseDto;
import com.medisync.channeldoc_api.dto.response.RestPage;
import com.medisync.channeldoc_api.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/my-appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<RestPage<PatientAppointmentHistoryResponseDto>> getMyAppointmentHistory(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 10, sort = "appointmentDate", direction = Sort.Direction.DESC) Pageable pageable) {
        RestPage<PatientAppointmentHistoryResponseDto> history = patientService.getMyAppointmentHistory(user, pageable);
        return ResponseEntity.ok(history);
    }
}

