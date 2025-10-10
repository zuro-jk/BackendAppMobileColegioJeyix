package com.jeyix.school_jeyix.core.security.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshRequest {
    @NotNull(message = "El ID de la sesión no puede ser nulo.")
    private Long sessionId;
}
