package com.jeyix.school_jeyix.features.parent.dto.parent.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentDetailResponse {
    private Long id;
    private UserDetailSummary user;
    private List<StudentSummary> children;
}
