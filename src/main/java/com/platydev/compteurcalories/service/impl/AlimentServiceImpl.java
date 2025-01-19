package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.output.AlimentDTO;
import com.platydev.compteurcalories.dto.output.AlimentResponse;
import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.exception.ApiException;
import com.platydev.compteurcalories.infrastructure.AlimentMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.service.AlimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlimentServiceImpl implements AlimentService {

    private static final User ADMIN = User.builder().id(1).build();

    private final AlimentRepository alimentRepository;

    private final AlimentMapper alimentMapper;

    @Autowired
    public AlimentServiceImpl(AlimentRepository alimentRepository, AlimentMapper alimentMapper) {
        this.alimentRepository = alimentRepository;
        this.alimentMapper = alimentMapper;
    }

    @Override
    public AlimentResponse getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Aliment> alimentPage = alimentRepository.findAll(pageDetails);

        List<Aliment> categories = alimentPage.getContent();
        if (categories.isEmpty()) {
            throw new ApiException("No aliment created until now");
        }
        List<AlimentDTO> alimentDTOS = categories.stream()
                .map(alimentMapper::toDTO)
                .toList();

        return alimentMapper.toAlimentResponse(alimentDTOS, alimentPage);
    }

    @Override
    public AlimentResponse getAllForUser(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Aliment> alimentPage = alimentRepository.findByUserOrUser(user, ADMIN, pageDetails);

        List<Aliment> categories = alimentPage.getContent();
        if (categories.isEmpty()) {
            throw new ApiException("No aliment created until now");
        }
        List<AlimentDTO> alimentDTOS = categories.stream()
                .map(alimentMapper::toDTO)
                .toList();

        return alimentMapper.toAlimentResponse(alimentDTOS, alimentPage);
    }

    @Override
    public AlimentResponse search(String word, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public void add(AlimentDTO alimentDTO) {
        if (exist(alimentDTO.nom())) {
            throw new ApiException("This aliment already exists");
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Aliment aliment = alimentMapper.toEntity(alimentDTO);
        aliment.setUser(user);
        if (aliment.getCodeBarre() != null) {
            aliment.getCodeBarre().setAliment(aliment);
        }
        alimentRepository.save(aliment);
    }

    @Override
    public AlimentDTO update(long alimentId, AlimentDTO alimentDTO) {
        return null;
    }

    @Override
    public void delete(long alimentId) {

    }

    @Override
    public boolean exist(String alimentName) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Aliment> alimentOptional = alimentRepository.findByNomAndByUser(alimentName, user);
        return alimentOptional.isPresent();
    }
}
