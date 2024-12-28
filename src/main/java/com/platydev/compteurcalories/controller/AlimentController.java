package com.platydev.compteurcalories.controller;

import com.platydev.compteurcalories.config.AppConstants;
import com.platydev.compteurcalories.dto.output.AlimentResponse;
import com.platydev.compteurcalories.service.AlimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlimentController {

    private final AlimentService alimentService;

    @Autowired
    public AlimentController(AlimentService alimentService) {
        this.alimentService = alimentService;
    }

    @GetMapping("/admin/aliments")
    public AlimentResponse getAll(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ALIMENTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        return alimentService.getAll(pageNumber, pageSize, sortBy, sortOrder);
    }

    @GetMapping("/api/aliments")
    public AlimentResponse getAllForUser(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ALIMENTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        return alimentService.getAllForUser(pageNumber, pageSize, sortBy, sortOrder);
    }
}
