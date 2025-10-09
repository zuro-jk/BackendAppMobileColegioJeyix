package com.jeyix.school_jeyix.features.parent.dto.parent.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParentRequest {

    @NotNull(message = "El ID de usuario no puede ser nulo.")
    private Long userId;

}
