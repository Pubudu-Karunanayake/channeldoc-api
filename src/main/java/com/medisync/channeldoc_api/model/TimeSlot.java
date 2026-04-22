package com.medisync.channeldoc_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "time_slots", indexes = @Index(name = "idx_daily_session", columnList = "daily_session_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctor;

    @ManyToOne
    @JoinColumn(name = "daily_session_id")
    private DailySession dailySession;

    private LocalTime slotTime;
    private Boolean isBooked;

    @Version
    private Integer version;
}
