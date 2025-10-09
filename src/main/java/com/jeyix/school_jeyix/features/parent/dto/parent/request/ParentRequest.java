package com.jeyix.school_jeyix.features.parent.dto.parent.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParentRequest {
    private Long userId;
    private String phoneNumber;
}
