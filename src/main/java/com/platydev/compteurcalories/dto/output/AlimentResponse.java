package com.platydev.compteurcalories.dto.output;

import com.platydev.compteurcalories.dto.AlimentDTO;

import java.util.List;

public record AlimentResponse(List<AlimentDTO> content, Integer pageNumber, Integer pageSize,
                              Long totalElements, Integer totalPages, boolean lastPage) {
}
