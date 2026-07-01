package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DoctorProfileResponseDto extends UserProfileResponseDto {
    private String slmcNumber;
    private Specialization specialization;
}
