package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.request.AppointmentBookingRequestDto;
import com.medisync.channeldoc_api.dto.response.AppointmentResponseDto;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.service.AppointmentBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentBookingService appointmentBookingService;

    @PostMapping("/book")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseDto> bookAppointment(
            @Valid @RequestBody AppointmentBookingRequestDto request,
            @AuthenticationPrincipal User user) {
        AppointmentResponseDto response = appointmentBookingService.bookAppointment(request, user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @org.springframework.web.bind.annotation.PatchMapping("/{appointmentId}/payment-status")
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    public ResponseEntity<AppointmentResponseDto> updatePaymentStatus(
            @org.springframework.web.bind.annotation.PathVariable Long appointmentId,
            @Valid @RequestBody com.medisync.channeldoc_api.dto.request.PaymentStatusUpdateRequestDto request,
            @AuthenticationPrincipal User user) {
        AppointmentResponseDto response = appointmentBookingService.updatePaymentStatus(appointmentId, request, user);
        return ResponseEntity.ok(response);
    }
}
