package com.jeyix.school_jeyix.features.student.dto.student.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
    @NotNull(message = "El ID de usuario no puede ser nulo.")
    private Long userId;

    @NotNull(message = "El ID del padre no puede ser nulo.")
    private Long parentId;

    @NotBlank(message = "El grado no puede estar vacío.")
    @Size(max = 50, message = "El grado no puede exceder los 50 caracteres.")
    private String gradeLevel;

    @NotBlank(message = "La sección no puede estar vacía.")
    @Size(max = 20, message = "La sección no puede exceder los 20 caracteres.")
    private String section;
}
