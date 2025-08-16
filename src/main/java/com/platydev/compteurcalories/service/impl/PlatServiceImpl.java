package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.PlatInputDTO;
import com.platydev.compteurcalories.dto.input.RecetteInputDTO;
import com.platydev.compteurcalories.dto.output.NutrientTotals;
import com.platydev.compteurcalories.dto.output.PlatDTO;
import com.platydev.compteurcalories.dto.output.PlatResponse;
import com.platydev.compteurcalories.dto.output.PlatWithoutRecetteDTO;
import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.Plat;
import com.platydev.compteurcalories.entity.Recette;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.exception.ApiException;
import com.platydev.compteurcalories.exception.ForbiddenException;
import com.platydev.compteurcalories.exception.NotFoundException;
import com.platydev.compteurcalories.infrastructure.PlatMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.PlatRepository;
import com.platydev.compteurcalories.repository.RecetteRepository;
import com.platydev.compteurcalories.service.PlatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PlatServiceImpl implements PlatService {

    private final PlatRepository platRepository;
    private final AlimentRepository alimentRepository;
    private final RecetteRepository recetteRepository;
    private final PlatMapper platMapper;
    private final NutritionalCalculator nutritionalCalculator;

    @Autowired
    public PlatServiceImpl(PlatRepository platRepository, AlimentRepository alimentRepository,
                           RecetteRepository recetteRepository, PlatMapper platMapper,
                           NutritionalCalculator nutritionalCalculator) {
        this.platRepository = platRepository;
        this.alimentRepository = alimentRepository;
        this.recetteRepository = recetteRepository;
        this.platMapper = platMapper;
        this.nutritionalCalculator = nutritionalCalculator;
    }

    @Override
    public PlatResponse find(Pageable pageable, String search) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Plat> platsPage;

        if (search != null && !search.trim().isEmpty()) {
            // Recherche avec filtre de nom pour l'utilisateur connecté
            platsPage = platRepository.findByAlimentUserIdAndAlimentNomContainingIgnoreCase(
                    pageable, currentUser.getId(), "%" + search.trim().toUpperCase() + "%");
        } else {
            // Récupérer tous les plats de l'utilisateur connecté
            platsPage = platRepository.findByAlimentUserId(pageable, currentUser.getId());
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
    @Transactional
    public void add(@Valid PlatInputDTO platDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<Aliment, Float> alimentsQuantites = validateAndAggregateRecettes(platDTO, currentUser);

        NutrientTotals totals = nutritionalCalculator.calculateTotals(alimentsQuantites);
        Aliment alimentPlat = nutritionalCalculator.createPlatAliment(platDTO, totals, currentUser);

        Plat plat = new Plat();
        plat.setNbPortions(platDTO.nbPortions());
        plat.setAliment(alimentPlat);
        alimentPlat.setPlat(plat);

        Aliment savedAliment = alimentRepository.save(alimentPlat);
        saveRecettes(savedAliment.getPlat(), alimentsQuantites);
    }

    @Override
    @Transactional
    public void update(long platId, @Valid PlatInputDTO platDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plat platExistant = findPlatAndCheckOwnership(platId, currentUser.getId());

        Map<Aliment, Float> nouvellesRecettes = validateAndAggregateRecettes(platDTO, currentUser);
        List<Recette> recettesExistantes = recetteRepository.findAllByRecetteId_PlatId(platId);

        if (areRecettesIdentical(recettesExistantes, nouvellesRecettes)) {
            updatePlatDataOnly(platExistant, platDTO, nouvellesRecettes);
        } else {
            updatePlatWithNewRecettes(platExistant, platDTO, nouvellesRecettes, recettesExistantes);
        }
    }

    @Override
    @Transactional
    public void delete(long platId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plat plat = findPlatAndCheckOwnership(platId, currentUser.getId());

        alimentRepository.deleteById(plat.getAliment().getId());
    }

    /**
     * Valide et agrège les recettes d'un PlatInputDTO
     * Méthode commune utilisée par add() et update()
     * Note: Les validations de base (non null, > 0) sont maintenant gérées par les annotations @Valid
     */
    private Map<Aliment, Float> validateAndAggregateRecettes(PlatInputDTO platDTO, User currentUser) {
        // Agrégation des recettes par Aliment avec vérification des accès
        Map<Aliment, Float> alimentsQuantites = new HashMap<>();

        for (RecetteInputDTO recetteDTO : platDTO.recettes()) {
            // Récupération de l'aliment
            Aliment aliment = alimentRepository.findById(recetteDTO.alimentId())
                    .orElseThrow(() -> new NotFoundException("Aliment avec l'ID " + recetteDTO.alimentId() + " non trouvé"));

            // Vérifier que l'utilisateur a accès à cet aliment
            if (!aliment.getUser().equals(currentUser) && aliment.getUser().getId() != 1L) {
                throw new ForbiddenException("Vous n'avez pas accès à l'aliment avec l'ID " + recetteDTO.alimentId());
            }

            // Vérifier que ce n'est pas un plat (on ne peut pas mettre un plat dans un plat)
            if (aliment.getPlat() != null) {
                throw new ApiException("Impossible d'utiliser un plat comme ingrédient d'un autre plat (ID: " + recetteDTO.alimentId() + ")");
            }

            // Agrégation : additionner les quantités si l'aliment existe déjà
            alimentsQuantites.merge(aliment, recetteDTO.quantite(), Float::sum);
        }

        return alimentsQuantites;
    }

    /**
     * Sauvegarde les recettes associées à un plat
     * Méthode commune utilisée par add() et updatePlatWithNewRecettes()
     */
    private void saveRecettes(Plat plat, Map<Aliment, Float> alimentsQuantites) {
        List<Recette> recettes = new ArrayList<>();
        for (Map.Entry<Aliment, Float> entry : alimentsQuantites.entrySet()) {
            Aliment alimentRecette = entry.getKey();
            Float quantiteTotale = entry.getValue();

            Recette recette = new Recette(plat, alimentRecette, quantiteTotale);
            recettes.add(recette);
        }

        recetteRepository.saveAll(recettes);
    }

    /**
     * Compare deux maps de recettes pour voir si elles sont identiques
     */
    private boolean areRecettesIdentical(List<Recette> existantes, Map<Aliment, Float> nouvelles) {
        if (existantes.size() != nouvelles.size()) {
            return false;
        }

        for (Recette recette : existantes) {
            Aliment aliment = recette.getAliment();
            Float quantiteExistante = recette.getQuantite();
            Float quantiteNouvelle = nouvelles.get(aliment);

            if (quantiteNouvelle == null || !Objects.equals(quantiteExistante, quantiteNouvelle)) {
                return false;
            }
        }

        return true;
    }

    private void updatePlatDataOnly(Plat platExistant, PlatInputDTO platDTO, Map<Aliment, Float> alimentsQuantites) {
        Aliment alimentExistant = platExistant.getAliment();
        alimentExistant.setNom(platDTO.nom());

        if (platExistant.getNbPortions() != platDTO.nbPortions()) {
            platExistant.setNbPortions(platDTO.nbPortions());

            NutrientTotals totals = nutritionalCalculator.calculateTotals(alimentsQuantites);
            nutritionalCalculator.updateAlimentWithTotals(alimentExistant, totals, platDTO.nbPortions());
        }

        alimentRepository.save(alimentExistant);
    }

    private void updatePlatWithNewRecettes(Plat platExistant, PlatInputDTO platDTO,
                                           Map<Aliment, Float> nouvellesRecettes, List<Recette> anciennesRecettes) {
        recetteRepository.deleteAll(anciennesRecettes);
        platExistant.setNbPortions(platDTO.nbPortions());

        NutrientTotals totals = nutritionalCalculator.calculateTotals(nouvellesRecettes);
        Aliment alimentExistant = platExistant.getAliment();
        alimentExistant.setNom(platDTO.nom());
        nutritionalCalculator.updateAlimentWithTotals(alimentExistant, totals, platDTO.nbPortions());

        alimentRepository.save(alimentExistant);
        saveRecettes(platExistant, nouvellesRecettes);
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