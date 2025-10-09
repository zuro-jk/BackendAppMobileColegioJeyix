package com.jeyix.school_jeyix.features.payment.dto.customer.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentInitiationRequest {
    @NotNull(message = "El ID del pago no puede ser nulo.")
    private Long paymentId;
}