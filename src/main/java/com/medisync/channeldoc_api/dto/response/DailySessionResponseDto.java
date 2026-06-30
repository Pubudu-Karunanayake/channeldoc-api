package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySessionResponseDto {
    private Long id;
    private Long hospitalId;
    private Long doctorId;
    private Long masterScheduleId;
    private LocalDate sessionDate;
    private SessionStatus status;
}
