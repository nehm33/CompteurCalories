package com.platydev.compteurcalories.controller;

import com.platydev.compteurcalories.config.AppConstants;
import com.platydev.compteurcalories.dto.output.AlimentDTO;
import com.platydev.compteurcalories.dto.output.AlimentResponse;
import com.platydev.compteurcalories.service.AlimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/aliments")
    public AlimentResponse getAllForUser(
            @RequestParam(name = "search") String search,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ALIMENTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        return alimentService.search(search, pageNumber, pageSize, sortBy,sortOrder);
    }

    @PostMapping("/api/aliments")
    public ResponseEntity<Void> add(@RequestBody AlimentDTO alimentDTO) {
        alimentService.add(alimentDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/api/aliments/{alimentId}")
    public ResponseEntity<Void> add(@RequestParam long alimentId) {
        alimentService.delete(alimentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
