package com.platydev.compteurcalories.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record RecetteInputDTO(Long alimentId, Float quantite) { }
