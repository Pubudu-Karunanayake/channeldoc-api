package com.medisync.channeldoc_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.medisync.channeldoc_api.model.enums.Gender;

@Entity
@Table(name = "dependent_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DependentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User guarantor; 

    @Column(nullable = false)
    private String fullName;
    
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
