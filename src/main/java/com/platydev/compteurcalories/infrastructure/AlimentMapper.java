package com.platydev.compteurcalories.infrastructure;

import com.platydev.compteurcalories.dto.output.AlimentDTO;
import com.platydev.compteurcalories.dto.output.AlimentResponse;
import com.platydev.compteurcalories.entity.Aliment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AlimentMapper {

    @Mapping(target = "codeBarre", source = "aliment.codeBarre.codeBarre")
    AlimentDTO toDTO(Aliment aliment);

    @Mapping(target = "content", source = "alimentDTOS")
    @Mapping(target = "pageNumber", expression = "java(page.getNumber())")
    @Mapping(target = "pageSize", expression = "java(page.getSize())")
    @Mapping(target = "totalElements", expression = "java(page.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(page.getTotalPages())")
    @Mapping(target = "lastPage", expression = "java(page.isLast())")
    AlimentResponse toAlimentResponse(List<AlimentDTO> alimentDTOS, Page<Aliment> page);
}
