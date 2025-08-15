package com.platydev.compteurcalories.dto.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record PlatWithoutRecetteDTO(Long id, Long alimentId, String nom, Float calories) { }
