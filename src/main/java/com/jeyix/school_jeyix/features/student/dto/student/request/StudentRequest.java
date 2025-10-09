package com.jeyix.school_jeyix.features.student.dto.student.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
    private Long userId;
    private Long parentId;
    private String gradeLevel;
    private String section;
}
