package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.AlimentInputDTO;
import com.platydev.compteurcalories.dto.output.AlimentDTO;
import com.platydev.compteurcalories.dto.output.AlimentResponse;
import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.exception.ApiException;
import com.platydev.compteurcalories.exception.ForbiddenException;
import com.platydev.compteurcalories.exception.NotFoundException;
import com.platydev.compteurcalories.infrastructure.AlimentMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.CodeBarreRepository;
import com.platydev.compteurcalories.service.AlimentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Page<Aliment> alimentPage = alimentRepository.findAllByPlatIsNull(pageable);

        List<Aliment> aliments = alimentPage.getContent();
        if (aliments.isEmpty()) {
            throw new ApiException("Aucun aliment créé jusqu'à maintenant");
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
            alimentPage = alimentRepository.findUserAlimentsByNomOrCodeBarre(pageable, "%" + word.trim().toUpperCase() + "%", user.getId());
        } else {
            alimentPage = alimentRepository.findUserAliments(pageable, user.getId());
        }

        List<AlimentDTO> alimentDTOS = alimentPage.getContent().stream()
                .map(alimentMapper::toDTO)
                .toList();

        return alimentMapper.toAlimentResponse(alimentDTOS, alimentPage);
    }

    @Override
    public AlimentDTO findById(long alimentId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Aliment aliment = alimentRepository.findById(alimentId)
                .orElseThrow(() -> new NotFoundException("Aliment avec l'ID " + alimentId + " non trouvé"));

        // Vérifier que ce n'est pas un plat
        if (aliment.getPlat() != null) {
            throw new ApiException("L'ID fourni correspond à un plat, pas à un aliment");
        }

        if (!aliment.getUserId().equals(user.getId()) && aliment.getUserId() != 1L) {
            throw new ForbiddenException("Cet aliment ne vous appartient pas");
        }

        return alimentMapper.toDTO(aliment);
    }

    @Override
    @Transactional
    public void add(@Valid AlimentInputDTO alimentDTO) {
        if (existsByName(alimentDTO.nom())) {
            throw new ApiException("Cet aliment existe déjà");
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Aliment aliment = alimentMapper.toEntity(alimentDTO);
        aliment.setUserId(user.getId());
        if (aliment.getCodeBarre() != null) {
            aliment.getCodeBarre().setAliment(aliment);
        }
        alimentRepository.save(aliment);
    }

    @Override
    @Transactional
    public void update(long alimentId, @Valid AlimentInputDTO alimentDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        existsByIdAndByUser(alimentId, user);

        Aliment aliment = alimentMapper.toEntity(alimentDTO);
        aliment.setUserId(user.getId());
        aliment.setId(alimentId);

        codeBarreRepository.findByAlimentId(alimentId).ifPresent(codeBarreRepository::delete);

        if (aliment.getCodeBarre() != null) {
            aliment.getCodeBarre().setAliment(aliment);
        }

        alimentRepository.save(aliment);
    }

    @Override
    @Transactional
    public void delete(long alimentId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        existsByIdAndByUser(alimentId, user);
        alimentRepository.deleteById(alimentId);
    }

    @Override
    public boolean existsByName(String alimentName) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Aliment> alimentOptional = alimentRepository.findByNomAndUserAndPlatIsNull(alimentName, user.getId());
        return alimentOptional.isPresent();
    }



    /**
     * Vérifie qu'un aliment existe, appartient à l'utilisateur et n'est pas un plat
     * @param alimentId l'ID de l'aliment à vérifier
     * @param user l'utilisateur connecté
     * @throws NotFoundException si l'aliment n'existe pas
     * @throws ForbiddenException si l'aliment n'appartient pas à l'utilisateur
     * @throws ApiException si l'ID correspond à un plat
     */
    private void existsByIdAndByUser(long alimentId, User user) {
        Aliment aliment = alimentRepository.findById(alimentId)
                .orElseThrow(() -> new NotFoundException("Aliment avec l'ID " + alimentId + " non trouvé"));

        // Vérifier que ce n'est pas un plat
        if (aliment.getPlat() != null) {
            throw new ApiException("L'ID fourni correspond à un plat, pas à un aliment");
        }

        if (!user.getId().equals(aliment.getUserId())) {
            throw new ForbiddenException("Cet aliment ne vous appartient pas");
        }
    }
}