package com.medisync.channeldoc_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAvailabilityResponseDto {
    private Long dailySessionId;
    private LocalDate sessionDate;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalContactNumber;
}
