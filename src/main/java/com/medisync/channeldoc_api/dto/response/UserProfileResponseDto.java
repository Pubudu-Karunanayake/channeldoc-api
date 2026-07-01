package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String profileImageUrl;
    private Set<UserRole> roles;
    private HospitalResponseDto hospital;
}
