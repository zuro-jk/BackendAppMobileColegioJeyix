package com.jeyix.school_jeyix.features.payment.dto.customer.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentInitiationResponse {
    private String checkoutUrl;
}