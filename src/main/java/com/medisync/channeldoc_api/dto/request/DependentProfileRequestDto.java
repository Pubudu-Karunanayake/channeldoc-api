package com.medisync.channeldoc_api.dto.request;

import com.medisync.channeldoc_api.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DependentProfileRequestDto {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;
}
