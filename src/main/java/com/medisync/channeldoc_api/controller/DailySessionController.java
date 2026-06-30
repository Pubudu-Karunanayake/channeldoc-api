package com.medisync.channeldoc_api.controller;

import com.medisync.channeldoc_api.dto.response.DailySessionResponseDto;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.service.DailySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/daily-sessions")
@RequiredArgsConstructor
public class DailySessionController {

    private final DailySessionService dailySessionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('HOSPITAL_MANAGEMENT', 'HOSPITAL_ADMIN')")
    public ResponseEntity<List<DailySessionResponseDto>> getDailySessions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long masterScheduleId,
            @AuthenticationPrincipal User user) {

        List<DailySessionResponseDto> sessions = dailySessionService.getDailySessions(date, masterScheduleId, user);
        return ResponseEntity.ok(sessions);
    }

    @PatchMapping("/{sessionId}/cancel")
    @PreAuthorize("hasRole('HOSPITAL_ADMIN')")
    public ResponseEntity<DailySessionResponseDto> cancelDailySession(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal User user) {

        DailySessionResponseDto response = dailySessionService.cancelDailySession(sessionId, user);
        return ResponseEntity.ok(response);
    }
}
