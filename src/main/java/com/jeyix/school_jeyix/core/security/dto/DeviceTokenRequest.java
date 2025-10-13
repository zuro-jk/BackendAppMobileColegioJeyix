package com.jeyix.school_jeyix.core.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceTokenRequest {
    @NotBlank(message = "El token del dispositivo no puede estar vacío.")
    private String deviceToken;
}