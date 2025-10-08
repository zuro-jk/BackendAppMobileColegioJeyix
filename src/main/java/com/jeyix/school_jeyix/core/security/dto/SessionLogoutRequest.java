package com.jeyix.school_jeyix.core.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionLogoutRequest {
    @NotBlank(message = "El sessionId es obligatorio")
    private String sessionId;
}