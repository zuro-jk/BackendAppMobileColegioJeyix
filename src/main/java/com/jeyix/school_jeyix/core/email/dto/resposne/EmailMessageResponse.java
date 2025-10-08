package com.jeyix.school_jeyix.core.email.dto.resposne;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailMessageResponse {
    private boolean success;
    private String message;
}