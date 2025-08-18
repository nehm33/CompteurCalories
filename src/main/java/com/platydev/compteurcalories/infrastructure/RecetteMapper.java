package com.platydev.compteurcalories.infrastructure;

import com.platydev.compteurcalories.dto.output.RecetteDTO;
import com.platydev.compteurcalories.entity.Recette;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RecetteMapper {

    @Mapping(target = "alimentId", source = "recette.aliment.id")
    @Mapping(target = "alimentNom", source = "recette.aliment.nom")
    @Mapping(target = "alimentCalories", source = "recette.aliment.calories")
    @Mapping(target = "alimentUnite", source = "recette.aliment.unite")
    @Mapping(target = "quantite", source = "recette.quantite")
    RecetteDTO toDTO(Recette recette);
}
