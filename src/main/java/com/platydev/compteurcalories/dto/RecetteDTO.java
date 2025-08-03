package com.platydev.compteurcalories.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record RecetteDTO(Long alimentId, String alimentNom, Float alimentCalories, String alimentUnite, Float quantite) { }
