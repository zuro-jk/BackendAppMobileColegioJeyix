package com.jeyix.school_jeyix.features.enrollment.dto.enrollment.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentRequest {

    @NotNull(message = "El ID del estudiante no puede ser nulo.")
    private Long studentId;

    @NotBlank(message = "El año académico no puede estar vacío.")
    private String academicYear;

    @NotNull(message = "El monto total no puede ser nulo.")
    @Positive(message = "El monto total debe ser mayor que cero.")
    private BigDecimal totalAmount;

    @NotNull(message = "El número de cuotas no puede ser nulo.")
    @Min(value = 1, message = "Debe haber al menos una cuota.")
    private Integer numberOfInstallments;
}