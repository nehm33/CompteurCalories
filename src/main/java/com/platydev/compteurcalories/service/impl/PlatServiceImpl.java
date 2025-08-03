package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.PlatDTO;
import com.platydev.compteurcalories.dto.output.PlatResponse;
import com.platydev.compteurcalories.infrastructure.PlatMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.PlatRepository;
import com.platydev.compteurcalories.repository.RecetteRepository;
import com.platydev.compteurcalories.service.PlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlatServiceImpl implements PlatService {

    private final PlatRepository platRepository;

    private final AlimentRepository alimentRepository;

    private final RecetteRepository recetteRepository;

    private final PlatMapper platMapper;

    @Autowired
    public PlatServiceImpl(PlatRepository platRepository, AlimentRepository alimentRepository, RecetteRepository recetteRepository, PlatMapper platMapper) {
        this.platRepository = platRepository;
        this.alimentRepository = alimentRepository;
        this.recetteRepository = recetteRepository;
        this.platMapper = platMapper;
    }

    @Override
    public PlatResponse find(Pageable pageable, String search) {
        return null;
    }

    @Override
    public PlatDTO findById(long platId) {
        return null;
    }

    @Override
    public void add(PlatDTO platDTO) {

    }

    @Override
    public void update(long platId, PlatDTO platDTO) {

    }

    @Override
    public void delete(long platId) {

    }
}
