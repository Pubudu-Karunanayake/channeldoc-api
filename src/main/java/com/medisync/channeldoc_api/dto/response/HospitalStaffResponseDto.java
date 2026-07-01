package com.medisync.channeldoc_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalStaffResponseDto {
    private List<UserProfileResponseDto> admins;
    private List<UserProfileResponseDto> management;
}
