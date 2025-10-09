package com.jeyix.school_jeyix.features.parent.dto.parent.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummary {
    private Long id;
    private String username;
    private String email;
    private String fullName;
}