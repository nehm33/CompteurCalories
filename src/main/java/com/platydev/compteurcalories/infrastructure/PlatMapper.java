package com.platydev.compteurcalories.infrastructure;

import com.platydev.compteurcalories.dto.output.PlatDTO;
import com.platydev.compteurcalories.dto.output.PlatResponse;
import com.platydev.compteurcalories.dto.output.PlatWithoutRecetteDTO;
import com.platydev.compteurcalories.entity.Plat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RecetteMapper.class})
public interface PlatMapper {

    @Mapping(target = "id", source = "plat.id")
    @Mapping(target = "nbPortions", source = "plat.nbPortions")
    @Mapping(target = "alimentId", source = "plat.aliment.id")
    @Mapping(target = "nom", source = "plat.aliment.nom")
    @Mapping(target = "calories", source = "plat.aliment.calories")
    @Mapping(target = "recettes", source = "plat.recettes")
    PlatDTO toDTO(Plat plat);

    @Mapping(target = "id", source = "plat.id")
    @Mapping(target = "alimentId", source = "plat.aliment.id")
    @Mapping(target = "nom", source = "plat.aliment.nom")
    @Mapping(target = "calories", source = "plat.aliment.calories")
    PlatWithoutRecetteDTO toPlatWithoutRecetteDTO(Plat plat);

    List<PlatWithoutRecetteDTO> toPlatWithoutRecetteDTOList(List<Plat> plats);

    @Mapping(target = "content", source = "platDTOS")
    @Mapping(target = "pageNumber", expression = "java(page.getNumber())")
    @Mapping(target = "pageSize", expression = "java(page.getSize())")
    @Mapping(target = "totalElements", expression = "java(page.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(page.getTotalPages())")
    @Mapping(target = "lastPage", expression = "java(page.isLast())")
    PlatResponse toPlatResponse(List<PlatWithoutRecetteDTO> platDTOS, Page<Plat> page);
}
