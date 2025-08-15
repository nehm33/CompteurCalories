package com.platydev.compteurcalories.controller;

import com.platydev.compteurcalories.dto.input.PlatInputDTO;
import com.platydev.compteurcalories.dto.output.PlatDTO;
import com.platydev.compteurcalories.dto.output.PlatResponse;
import com.platydev.compteurcalories.service.PlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plats")
public class PlatController {

    private final PlatService platService;

    @Autowired
    public PlatController(PlatService platService) {
        this.platService = platService;
    }

    @GetMapping
    public PlatResponse getAllForUser(
            @RequestParam(name = "search", required = false) String search,
            @PageableDefault Pageable pageable
    ) {
        return platService.find(pageable, search);
    }

    @GetMapping("/{platId}")
    public PlatDTO get(@PathVariable long platId) {
        return platService.findById(platId);
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody PlatInputDTO platDTO) {
        platService.add(platDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{platId}")
    public ResponseEntity<Void> update(@PathVariable long platId, @RequestBody PlatInputDTO platDTO) {
        platService.update(platId, platDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{platId}")
    public ResponseEntity<Void> delete(@PathVariable long platId) {
        platService.delete(platId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}