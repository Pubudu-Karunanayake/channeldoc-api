package com.medisync.channeldoc_api.model;

import com.medisync.channeldoc_api.model.enums.Day;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Types;
import java.time.LocalTime;

import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "master_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctor;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "day")
    private Day day;

    private LocalTime startTime;
    private LocalTime endTime;
    private Integer timePerPatient;
    private Double consultationFee;
}
