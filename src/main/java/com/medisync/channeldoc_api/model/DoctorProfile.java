package com.medisync.channeldoc_api.model;

import com.medisync.channeldoc_api.model.enums.Specialization;
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

    @Column(nullable = false, unique = true)
    private String slmcNumber;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;
}
