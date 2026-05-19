package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DependentProfileResponseDto {
    private Long id;
    private Long guarantorId;
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
}
