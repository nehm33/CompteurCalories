package com.platydev.compteurcalories.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record RecetteInputDTO(
        @NotNull(message = "L'ID de l'aliment est obligatoire pour chaque recette")
        @Positive(message = "L'ID de l'aliment doit être positif")
        Long alimentId,

        @NotNull(message = "La quantité est obligatoire pour chaque recette")
        @Positive(message = "La quantité doit être supérieure à 0")
        Float quantite
) { }