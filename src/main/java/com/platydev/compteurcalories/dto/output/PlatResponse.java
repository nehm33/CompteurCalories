package com.platydev.compteurcalories.dto.output;

import com.platydev.compteurcalories.dto.PlatDTO;

import java.util.List;

public record PlatResponse(
        List<PlatDTO> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean lastPage
) {
}
