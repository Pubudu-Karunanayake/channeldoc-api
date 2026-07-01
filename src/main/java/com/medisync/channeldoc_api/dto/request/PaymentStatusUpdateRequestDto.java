package com.medisync.channeldoc_api.dto.request;

import com.medisync.channeldoc_api.model.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentStatusUpdateRequestDto {
    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;
}
