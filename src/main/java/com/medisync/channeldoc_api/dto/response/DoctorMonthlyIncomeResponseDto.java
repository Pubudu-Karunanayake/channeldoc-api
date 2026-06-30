package com.medisync.channeldoc_api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoctorMonthlyIncomeResponseDto {
    private String hospitalName;
    private Long hospitalId;
    private Long totalAppointments;
    private Double totalRevenue;
    private Double hospitalSharePercentage;
    private Double hospitalDeductionAmount;
    private Double doctorNetIncome;

    /**
     * Constructor used by JPQL projection to map raw aggregation values and compute derived financial fields.
     */
    public DoctorMonthlyIncomeResponseDto(String hospitalName, Long hospitalId, Long totalAppointments,
                                          Double consultationFee, Double hospitalSharePercentage) {
        this.hospitalName = hospitalName;
        this.hospitalId = hospitalId;
        this.totalAppointments = totalAppointments;
        this.hospitalSharePercentage = hospitalSharePercentage != null ? hospitalSharePercentage : 0.0;

        // Compute derived fields
        this.totalRevenue = this.totalAppointments * (consultationFee != null ? consultationFee : 0.0);
        this.hospitalDeductionAmount = this.totalRevenue * (this.hospitalSharePercentage / 100.0);
        this.doctorNetIncome = this.totalRevenue - this.hospitalDeductionAmount;
    }
}
