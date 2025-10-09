package com.jeyix.school_jeyix.features.student.dto.student.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Long id;
    private String username;
    private String fullName;
    private String gradeLevel;
    private String section;
    private Long parentId;
    private String parentName;
}
