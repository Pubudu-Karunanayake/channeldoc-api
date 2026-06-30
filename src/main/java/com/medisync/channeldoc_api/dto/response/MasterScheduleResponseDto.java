package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterScheduleResponseDto {
    private Long id;
    private Long hospitalId;
    private Long doctorId;
    private Day day;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer timePerPatient;
    private Double consultationFee;
    private Double hospitalSharePercentage;
}
