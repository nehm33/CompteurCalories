package com.platydev.compteurcalories.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record PlatInputDTO(
        @NotNull(message = "Le nombre de portions est obligatoire")
        @Positive(message = "Le nombre de portions doit être supérieur à 0")
        Float nbPortions,

        @NotBlank(message = "Le nom du plat est obligatoire")
        @Size(max = 255, message = "Le nom du plat ne peut pas dépasser 255 caractères")
        String nom,

        @NotNull(message = "La liste des recettes est obligatoire")
        @NotEmpty(message = "Un plat doit contenir au moins une recette")
        @Valid
        List<RecetteInputDTO> recettes
) { }