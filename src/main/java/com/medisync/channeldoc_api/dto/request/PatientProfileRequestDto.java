package com.medisync.channeldoc_api.dto.request;

import com.medisync.channeldoc_api.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientProfileRequestDto {
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;
}
