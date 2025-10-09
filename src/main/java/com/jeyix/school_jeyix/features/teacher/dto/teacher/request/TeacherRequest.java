package com.jeyix.school_jeyix.features.teacher.dto.teacher.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequest {
    private Long userId;
    private String specialty;
    private String course;
}
