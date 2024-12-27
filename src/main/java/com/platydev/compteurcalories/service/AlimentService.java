package com.platydev.compteurcalories.service;

import com.platydev.compteurcalories.dto.output.AlimentDTO;
import com.platydev.compteurcalories.dto.output.AlimentResponse;

public interface AlimentService {

    AlimentResponse getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    AlimentResponse getAllForUser(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    AlimentResponse search(String word, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    void add(AlimentDTO alimentDTO);
    AlimentDTO update(long alimentId, AlimentDTO alimentDTO);
    void delete(long alimentId);
}
