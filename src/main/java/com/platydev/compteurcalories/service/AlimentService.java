package com.platydev.compteurcalories.service;

import com.platydev.compteurcalories.dto.AlimentDTO;
import com.platydev.compteurcalories.dto.output.AlimentResponse;
import org.springframework.data.domain.Pageable;

public interface AlimentService {

    AlimentResponse getAll(Pageable pageable);

    AlimentResponse find(Pageable pageable, String word);

    AlimentDTO findById(long alimentId);

    void add(AlimentDTO alimentDTO);

    void update(long alimentId, AlimentDTO alimentDTO);

    void delete(long alimentId);

    boolean existsByName(String alimentName);
}
