package com.medisync.channeldoc_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalResponseDto {
    private Long id;
    private String name;
    private String address;
    private String contactNumber;
}
