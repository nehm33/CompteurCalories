package com.platydev.compteurcalories.service;

import com.platydev.compteurcalories.dto.input.PlatInputDTO;
import com.platydev.compteurcalories.dto.output.PlatDTO;
import com.platydev.compteurcalories.dto.output.PlatResponse;
import org.springframework.data.domain.Pageable;

public interface PlatService {

    PlatResponse find(Pageable pageable, String search);

    PlatDTO findById(long platId);

    void add(PlatInputDTO platDTO);

    void update(long platId, PlatInputDTO platDTO);

    void delete(long platId);
}
