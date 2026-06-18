package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSearchResponseDto {
    private Long id;
    private String doctorName;
    private Specialization specialization;
    private String hospitalName;
    private String profileImageUrl;
}
