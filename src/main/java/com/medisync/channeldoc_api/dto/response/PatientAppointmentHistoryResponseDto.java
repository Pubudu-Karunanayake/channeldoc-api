package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.AppointmentStatus;
import com.medisync.channeldoc_api.model.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientAppointmentHistoryResponseDto {
    private String appointmentNumber;
    private LocalDate appointmentDate;
    private AppointmentStatus status;
    private String doctorName;
    private Specialization doctorSpecialization;
    private String hospitalName;
    private LocalTime sessionStartTime;
    private LocalTime sessionEndTime;
    private Double totalPaidAmount;
}
