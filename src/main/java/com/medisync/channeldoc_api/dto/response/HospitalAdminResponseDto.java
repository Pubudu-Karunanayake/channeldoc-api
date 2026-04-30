package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class HospitalAdminResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String profileImageUrl;
    private Set<UserRole> roles;
    private Long hospitalId;
}
