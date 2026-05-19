package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PatientProfileResponseDto {
    private Long userId;
    private String fullName;
    private String email;
    private String profileImageUrl;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String contactNumber;
}
