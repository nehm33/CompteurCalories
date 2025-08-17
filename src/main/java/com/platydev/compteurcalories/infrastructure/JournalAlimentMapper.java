package com.platydev.compteurcalories.infrastructure;

import com.platydev.compteurcalories.dto.output.JournalAlimentDTO;
import com.platydev.compteurcalories.entity.JournalAliment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JournalAlimentMapper {

    @Mapping(target = "alimentId", source = "aliment.id")
    @Mapping(target = "quantite", source = "quantite")
    JournalAlimentDTO toDTO(JournalAliment journalAliment);

    List<JournalAlimentDTO> toDTOList(List<JournalAliment> journalAliments);
}