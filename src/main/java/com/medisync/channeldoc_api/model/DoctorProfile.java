package com.medisync.channeldoc_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_profiles", indexes = @Index(name = "idx_specialization", columnList = "specialization"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfile {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String slmcNumber;
    private String specialization;
}
