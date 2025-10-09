package com.jeyix.school_jeyix.features.enrollment.dto.enrollment.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSummary {
    private Long id;
    private BigDecimal amount;
    private LocalDate dueDate;
    private Boolean isPaid;
}