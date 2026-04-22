package com.medisync.channeldoc_api.model;

import com.medisync.channeldoc_api.model.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Types;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "daily_sessions", indexes = {
    @Index(name = "idx_hospital_doctor_date", columnList = "hospital_id, doctor_id, session_date"),
    @Index(name = "idx_doctor_date", columnList = "doctor_id, session_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailySession {
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
    @JoinColumn(name = "master_schedule_id")
    private MasterSchedule masterSchedule;

    private LocalDate sessionDate;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "status")
    private SessionStatus status;
}
