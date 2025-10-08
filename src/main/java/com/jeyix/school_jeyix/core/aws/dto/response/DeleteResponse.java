package com.jeyix.school_jeyix.core.aws.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteResponse {
    private Long fileId;
    private Long deletedById;
    private String deletedByUsername;
}