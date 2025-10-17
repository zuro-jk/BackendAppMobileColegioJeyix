package com.jeyix.school_jeyix.features.parent.dto.parent.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailSummary {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String profileImageUrl;
    private boolean enabled;
    private boolean emailVerified;
}