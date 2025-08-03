package com.platydev.compteurcalories.service;

import com.platydev.compteurcalories.dto.PlatDTO;
import com.platydev.compteurcalories.dto.output.PlatResponse;
import org.springframework.data.domain.Pageable;

public interface PlatService {

    PlatResponse find(Pageable pageable, String search);

    PlatDTO findById(long platId);

    void add(PlatDTO platDTO);

    void update(long platId, PlatDTO platDTO);

    void delete(long platId);
}
