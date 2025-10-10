package com.jeyix.school_jeyix.features.teacher.dto.teacher.request;

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
public class TeacherRequest {
    @NotNull(message = "El ID de usuario no puede ser nulo.")
    private Long userId;

    @NotBlank(message = "La especialidad no puede estar vacía.")
    @Size(max = 100, message = "La especialidad no puede exceder los 100 caracteres.")
    private String specialty;

    @NotBlank(message = "El curso no puede estar vacío.")
    @Size(max = 100, message = "El curso no puede exceder los 100 caracteres.")
    private String course;
}
