package com.platydev.compteurcalories.dto.output;

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
