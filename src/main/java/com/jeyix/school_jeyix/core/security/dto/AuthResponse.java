package com.jeyix.school_jeyix.core.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private Long sessionId;
    private UserProfileResponse user;
}
