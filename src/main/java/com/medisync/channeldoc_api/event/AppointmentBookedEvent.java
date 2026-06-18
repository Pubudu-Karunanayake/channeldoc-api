package com.medisync.channeldoc_api.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * POJO event published after a booking transaction commits.
 * Contains all data needed by the email listener — no entity references,
 * ensuring complete decoupling from the persistence layer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentBookedEvent {
    private String appointmentNumber;
    private String patientEmail;
    private String patientName;
    private String doctorName;
    private String hospitalName;
    private LocalDate appointmentDate;
    private LocalTime slotTime;
    private Double consultationFee;
}
