package com.jeyix.school_jeyix.features.enrollment.dto.enrollment.response;

import java.math.BigDecimal;
import java.util.List;

import com.jeyix.school_jeyix.features.enrollment.enums.EnrollmentStatus;
import com.jeyix.school_jeyix.features.parent.dto.parent.response.StudentSummary;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentResponse {
    private Long id;
    private StudentSummary student;
    private String academicYear;
    private BigDecimal totalAmount;
    private EnrollmentStatus status;
    private List<PaymentSummary> payments;
}