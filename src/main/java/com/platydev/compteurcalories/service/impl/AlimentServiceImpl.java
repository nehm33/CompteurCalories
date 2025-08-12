package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.AlimentInputDTO;
import com.platydev.compteurcalories.dto.output.AlimentDTO;
import com.platydev.compteurcalories.dto.output.AlimentResponse;
import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.CodeBarre;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.exception.ApiException;
import com.platydev.compteurcalories.exception.ForbiddenException;
import com.platydev.compteurcalories.exception.NotFoundException;
import com.platydev.compteurcalories.infrastructure.AlimentMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.CodeBarreRepository;
import com.platydev.compteurcalories.service.AlimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlimentServiceImpl implements AlimentService {

    private final AlimentRepository alimentRepository;

    private final CodeBarreRepository codeBarreRepository;

    private final AlimentMapper alimentMapper;

    @Autowired
    public AlimentServiceImpl(AlimentRepository alimentRepository, CodeBarreRepository codeBarreRepository, AlimentMapper alimentMapper) {
        this.alimentRepository = alimentRepository;
        this.codeBarreRepository = codeBarreRepository;
        this.alimentMapper = alimentMapper;
    }

    @Override
    public AlimentResponse getAll(Pageable pageable) {
        Page<Aliment> alimentPage = alimentRepository.findAll(pageable);

        List<Aliment> aliments = alimentPage.getContent();
        if (aliments.isEmpty()) {
            throw new ApiException("No aliment created until now");
        }
        List<AlimentDTO> alimentDTOS = aliments.stream()
                .map(alimentMapper::toDTO)
                .toList();

        return alimentMapper.toAlimentResponse(alimentDTOS, alimentPage);
    }

    @Override
    public AlimentResponse find(Pageable pageable, String word) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<Aliment> alimentPage;
        if (word != null) {
            alimentPage = alimentRepository.findUserAlimentsByNomOrCodeBarre(pageable, "%" + word + "%", user);
        } else {
            alimentPage = alimentRepository.findUserAliments(pageable, user);
        }

        List<AlimentDTO> alimentDTOS = alimentPage.getContent().stream()
                .map(alimentMapper::toDTO)
                .toList();

        return alimentMapper.toAlimentResponse(alimentDTOS, alimentPage);
    }

    @Override
    public AlimentDTO findById(long alimentId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Aliment aliment = alimentRepository.findById(alimentId).orElseThrow(() -> new NotFoundException("Aliment with id " + alimentId + " not found"));

        if (!aliment.getUser().equals(user) && aliment.getUser().getId() != 1L) {
            throw new ForbiddenException("The given aliment is not yours");
        }

        return alimentMapper.toDTO(aliment);
    }

    @Override
    public void add(AlimentInputDTO alimentDTO) {
        if (existsByName(alimentDTO.nom())) {
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
    public void update(long alimentId, AlimentInputDTO alimentDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        existsByIdAndByUser(alimentId, user);

        Aliment aliment = alimentMapper.toEntity(alimentDTO);
        aliment.setUser(user);
        aliment.setId(alimentId);

        if (aliment.getCodeBarre() != null) {
            aliment.getCodeBarre().setAliment(aliment);
        }
        Aliment updatedAliment = alimentRepository.save(aliment);
        alimentMapper.toDTO(updatedAliment);

        Optional<CodeBarre> codeBarreOptional = codeBarreRepository.findByAlimentId(alimentId);
        if (codeBarreOptional.isEmpty() && aliment.getCodeBarre() != null) {
            codeBarreRepository.save(aliment.getCodeBarre());
        }
    }

    @Override
    public void delete(long alimentId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        existsByIdAndByUser(alimentId, user);
        alimentRepository.deleteById(alimentId);
    }

    @Override
    public boolean existsByName(String alimentName) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Aliment> alimentOptional = alimentRepository.findByNomAndUser(alimentName, user);
        return alimentOptional.isPresent();
    }

    private void existsByIdAndByUser(long alimentId, User user) {
        Aliment aliment = alimentRepository.findById(alimentId)
                .orElseThrow(() -> new NotFoundException("Aliment with id " + alimentId+ " not found"));

        if (!user.equals(aliment.getUser())) {
            throw new ForbiddenException("The given aliment is not yours");
        }
    }
}
