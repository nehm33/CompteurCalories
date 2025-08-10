package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.PlatDTO;
import com.platydev.compteurcalories.dto.PlatWithoutRecetteDTO;
import com.platydev.compteurcalories.dto.output.PlatResponse;
import com.platydev.compteurcalories.entity.Plat;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.exception.ForbiddenException;
import com.platydev.compteurcalories.exception.NotFoundException;
import com.platydev.compteurcalories.infrastructure.PlatMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.PlatRepository;
import com.platydev.compteurcalories.repository.RecetteRepository;
import com.platydev.compteurcalories.service.PlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatServiceImpl implements PlatService {

    private final PlatRepository platRepository;
    private final AlimentRepository alimentRepository;
    private final RecetteRepository recetteRepository;
    private final PlatMapper platMapper;

    @Autowired
    public PlatServiceImpl(PlatRepository platRepository, AlimentRepository alimentRepository,
                           RecetteRepository recetteRepository, PlatMapper platMapper) {
        this.platRepository = platRepository;
        this.alimentRepository = alimentRepository;
        this.recetteRepository = recetteRepository;
        this.platMapper = platMapper;
    }

    @Override
    public PlatResponse find(Pageable pageable, String search) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Plat> platsPage;

        if (search != null && !search.trim().isEmpty()) {
            // Recherche avec filtre de nom pour l'utilisateur connecté
            platsPage = platRepository.findByAlimentUserIdAndAlimentNomContainingIgnoreCase(
                    currentUser.getId(), search.trim(), pageable);
        } else {
            // Récupérer tous les plats de l'utilisateur connecté
            platsPage = platRepository.findByAlimentUserId(currentUser.getId(), pageable);
        }

        // Utiliser le mapper pour convertir les entités en DTOs
        List<PlatWithoutRecetteDTO> platDTOs = platMapper.toPlatWithoutRecetteDTOList(platsPage.getContent());

        // Créer la réponse paginée avec le mapper
        return platMapper.toPlatResponse(platDTOs, platsPage);
    }

    @Override
    public PlatDTO findById(long platId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plat plat = findPlatAndCheckOwnership(platId, currentUser.getId());
        return platMapper.toDTO(plat);
    }

    @Override
    public void add(PlatDTO platDTO) {
        // Implémentation à venir
    }

    @Override
    public void update(long platId, PlatDTO platDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plat plat = findPlatAndCheckOwnership(platId, currentUser.getId());

        // Implémentation de la mise à jour à venir
    }

    @Override
    public void delete(long platId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plat plat = findPlatAndCheckOwnership(platId, currentUser.getId());

        alimentRepository.deleteById(plat.getAliment().getId());
    }

    /**
     * Vérifie que le plat existe et appartient à l'utilisateur spécifié
     * @param platId ID du plat à vérifier
     * @param userId ID de l'utilisateur
     * @return le plat si les vérifications passent
     * @throws NotFoundException si le plat n'existe pas
     * @throws ForbiddenException si le plat n'appartient pas à l'utilisateur
     */
    private Plat findPlatAndCheckOwnership(long platId, long userId) {
        // 1. Vérifier que le plat existe
        Plat plat = platRepository.findById(platId)
                .orElseThrow(() -> new NotFoundException("Plat non trouvé"));

        // 2. Vérifier que l'utilisateur a le droit d'y accéder
        if (!plat.getAliment().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Vous n'êtes pas autorisé à accéder à ce plat");
        }

        return plat;
    }
}