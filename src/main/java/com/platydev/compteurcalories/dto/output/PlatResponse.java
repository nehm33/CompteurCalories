package com.platydev.compteurcalories.dto.output;

import com.platydev.compteurcalories.dto.PlatWithoutRecetteDTO;

import java.util.List;

public record PlatResponse(
        List<PlatWithoutRecetteDTO> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean lastPage
) {
}
