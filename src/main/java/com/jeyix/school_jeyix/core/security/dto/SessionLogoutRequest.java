package com.jeyix.school_jeyix.core.security.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionLogoutRequest {
    @NotNull(message = "El sessionId es obligatorio")
    private Long sessionId;
}