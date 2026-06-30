package com.medisync.channeldoc_api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HospitalMonthlyIncomeResponseDto {
    private String doctorName;
    private Long doctorId;
    private Long totalAppointments;
    private Double totalRevenue;
    private Double hospitalSharePercentage;
    private Double totalHospitalIncome;

    /**
     * Constructor used by JPQL projection to map raw aggregation values and compute derived financial fields.
     */
    public HospitalMonthlyIncomeResponseDto(String doctorName, Long doctorId, Long totalAppointments,
                                            Double consultationFee, Double hospitalSharePercentage) {
        this.doctorName = doctorName;
        this.doctorId = doctorId;
        this.totalAppointments = totalAppointments;
        this.hospitalSharePercentage = hospitalSharePercentage != null ? hospitalSharePercentage : 0.0;

        // Compute derived fields
        this.totalRevenue = this.totalAppointments * (consultationFee != null ? consultationFee : 0.0);
        this.totalHospitalIncome = this.totalRevenue * (this.hospitalSharePercentage / 100.0);
    }
}
