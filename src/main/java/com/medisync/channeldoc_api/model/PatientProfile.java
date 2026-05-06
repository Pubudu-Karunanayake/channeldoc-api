package com.medisync.channeldoc_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.medisync.channeldoc_api.model.enums.Gender;

@Entity
@Table(name = "patient_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfile {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    private String contactNumber;
}
