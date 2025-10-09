package com.jeyix.school_jeyix.features.payment.dto.customer.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentPreferenceResponse {
    private String preferenceId;
    private String publicKey;
}