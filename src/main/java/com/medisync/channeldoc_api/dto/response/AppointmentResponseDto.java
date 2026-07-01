package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.AppointmentStatus;
import com.medisync.channeldoc_api.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDto {
    private String appointmentNumber;
    private String patientFullName;
    private String doctorName;
    private String hospitalName;
    private LocalDate appointmentDate;
    private LocalTime slotTime;
    private Double consultationFee;
    private AppointmentStatus status;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
}
