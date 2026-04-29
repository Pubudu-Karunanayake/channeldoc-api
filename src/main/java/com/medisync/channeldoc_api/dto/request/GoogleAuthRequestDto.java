package com.medisync.channeldoc_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAuthRequestDto {

    @NotBlank(message = "Google ID token is required")
    private String idToken;
}
