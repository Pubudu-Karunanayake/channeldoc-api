package com.medisync.channeldoc_api.model;

import com.medisync.channeldoc_api.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String fullName;
  private String email;
  private String profileImageUrl;

  @ManyToOne
  @JoinColumn(name = "hospital_id")
  private Hospital hospital;

  @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Set<UserRole> roles;
}
