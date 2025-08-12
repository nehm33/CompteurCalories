package com.platydev.compteurcalories.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record PlatInputDTO(Float nbPortions, String nom, List<RecetteInputDTO> recettes) { }
