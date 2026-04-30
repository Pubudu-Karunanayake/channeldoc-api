package com.medisync.channeldoc_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DoctorRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Hospital ID is required")
    private Long hospitalId;

    @NotBlank(message = "SLMC Number is required")
    private String slmcNumber;

    @NotBlank(message = "Specialization is required")
    private String specialization;
}
