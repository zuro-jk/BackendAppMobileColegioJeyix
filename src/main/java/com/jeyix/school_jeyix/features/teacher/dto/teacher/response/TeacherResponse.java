package com.jeyix.school_jeyix.features.teacher.dto.teacher.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {
    private Long id;
    private String username;
    private String fullName;
    private String specialty;
    private String course;
}
