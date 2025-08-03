package com.platydev.compteurcalories.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record PlatDTO(Long id, Float nbPortions, Long alimentId, List<RecetteDTO> recettes) { }
