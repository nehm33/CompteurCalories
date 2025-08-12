package com.platydev.compteurcalories.dto.output;

import java.util.List;

public record AlimentResponse(List<AlimentDTO> content, Integer pageNumber, Integer pageSize,
                              Long totalElements, Integer totalPages, boolean lastPage) {
}
