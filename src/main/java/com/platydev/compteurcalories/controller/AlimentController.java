package com.platydev.compteurcalories.controller;

import com.platydev.compteurcalories.dto.input.AlimentInputDTO;
import com.platydev.compteurcalories.dto.output.AlimentDTO;
import com.platydev.compteurcalories.dto.output.AlimentResponse;
import com.platydev.compteurcalories.service.AlimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public AlimentResponse getAll(@PageableDefault Pageable pageable) {
        return alimentService.getAll(pageable);
    }

    @GetMapping("/api/aliments")
    public AlimentResponse getAllForUser(
            @RequestParam(name = "search", required = false) String search,
            @PageableDefault Pageable pageable
    ) {
        return alimentService.find(pageable, search);
    }

    @GetMapping("/api/aliments/{alimentId}")
    public AlimentDTO get(@PathVariable long alimentId) {
        return alimentService.findById(alimentId);
    }

    @PostMapping("/api/aliments")
    public ResponseEntity<Void> add(@RequestBody AlimentInputDTO alimentDTO) {
        alimentService.add(alimentDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/api/aliments/{alimentId}")
    public ResponseEntity<Void> update(@PathVariable long alimentId, @RequestBody AlimentInputDTO alimentDTO) {
        alimentService.update(alimentId, alimentDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/api/aliments/{alimentId}")
    public ResponseEntity<Void> delete(@PathVariable long alimentId) {
        alimentService.delete(alimentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
