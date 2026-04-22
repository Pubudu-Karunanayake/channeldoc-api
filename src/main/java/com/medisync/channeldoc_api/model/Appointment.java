package com.medisync.channeldoc_api.model;

import com.medisync.channeldoc_api.model.enums.AppointmentStatus;
import com.medisync.channeldoc_api.model.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "appointments", indexes = {
        @Index(name = "idx_patient", columnList = "patient_id"),
        @Index(name = "idx_doctor", columnList = "doctor_id"),
        @Index(name = "idx_hospital_doctor", columnList = "hospital_id, doctor_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientProfile patient;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "status")
    private AppointmentStatus status;

    private LocalDate appointmentDate;
    private Double paymentAmount;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
