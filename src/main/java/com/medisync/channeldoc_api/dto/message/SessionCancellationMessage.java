package com.medisync.channeldoc_api.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Message payload placed on the RabbitMQ queue for session cancellation notifications.
 *
 * <p>Contains only primitive/String data and Java 8 types — no JPA entity references,
 * ensuring complete decoupling from the persistence layer.
 * Mirrors the design pattern used by {@code AppointmentBookedEvent}.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionCancellationMessage {
    private String patientEmail;
    private String patientName;
    private String doctorName;
    private String hospitalName;
    private LocalDate sessionDate;
    private String appointmentNumber;
}
