package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.JournalInputDTO;
import com.platydev.compteurcalories.dto.output.JournalAlimentDTO;
import com.platydev.compteurcalories.dto.output.JournalDTO;
import com.platydev.compteurcalories.dto.output.JournalDetails;
import com.platydev.compteurcalories.dto.output.NutrientTotals;
import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.Journal;
import com.platydev.compteurcalories.entity.JournalAliment;
import com.platydev.compteurcalories.entity.JournalId;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.exception.ApiException;
import com.platydev.compteurcalories.exception.ForbiddenException;
import com.platydev.compteurcalories.exception.NotFoundException;
import com.platydev.compteurcalories.infrastructure.JournalAlimentMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.JournalAlimentRepository;
import com.platydev.compteurcalories.repository.JournalRepository;
import com.platydev.compteurcalories.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JournalServiceImpl implements JournalService {

    private final JournalRepository journalRepository;
    private final JournalAlimentRepository journalAlimentRepository;
    private final AlimentRepository alimentRepository;
    private final NutritionalCalculator nutritionalCalculator;
    private final JournalAlimentMapper journalAlimentMapper;

    @Autowired
    public JournalServiceImpl(JournalRepository journalRepository,
                              JournalAlimentRepository journalAlimentRepository,
                              AlimentRepository alimentRepository,
                              NutritionalCalculator nutritionalCalculator,
                              JournalAlimentMapper journalAlimentMapper) {
        this.journalRepository = journalRepository;
        this.journalAlimentRepository = journalAlimentRepository;
        this.alimentRepository = alimentRepository;
        this.nutritionalCalculator = nutritionalCalculator;
        this.journalAlimentMapper = journalAlimentMapper;
    }

    @Override
    public JournalDTO getJournalByDate(LocalDate date) {
        User user = getCurrentUser();

        // Récupérer tous les journaux de l'utilisateur pour cette date
        List<Journal> journaux = journalRepository.findByJournalId_UserIdAndJournalId_Date(user.getId(), date);

        // Calculer les calories par repas
        Float breakfast = journaux.stream()
                .filter(j -> j.getJournalId().getRepas() == 1)
                .map(Journal::getCalories)
                .reduce(0f, Float::sum);

        Float lunch = journaux.stream()
                .filter(j -> j.getJournalId().getRepas() == 2)
                .map(Journal::getCalories)
                .reduce(0f, Float::sum);

        Float diner = journaux.stream()
                .filter(j -> j.getJournalId().getRepas() == 3)
                .map(Journal::getCalories)
                .reduce(0f, Float::sum);

        return new JournalDTO(
                breakfast > 0 ? breakfast : null,
                lunch > 0 ? lunch : null,
                diner > 0 ? diner : null
        );
    }

    @Override
    @Transactional
    public void addJournalEntry(LocalDate date, int repas, JournalInputDTO journalInputDTO) {
        User user = getCurrentUser();
        validateRepasNumber(repas);

        // Vérifier que le repas n'existe pas déjà
        if (journalRepository.existsById(new JournalId(user.getId(), date, repas))) {
            throw new ApiException("Un journal existe déjà pour cette date et ce repas. Utilisez PUT pour le modifier.");
        }

        // Valider et récupérer les aliments
        Map<Aliment, Float> alimentsQuantites = validateAndGetAliments(journalInputDTO.alimentQuantites(), user);

        // Calculer les calories totales du repas
        float caloriesTotal = nutritionalCalculator.calculateTotalCalories(alimentsQuantites);

        // Créer l'entrée Journal
        Journal journal = new Journal(date, null, user, repas, caloriesTotal);
        journalRepository.save(journal);

        // Créer les entrées JournalAliment
        List<JournalAliment> journalAliments = new ArrayList<>();
        for (Map.Entry<Aliment, Float> entry : alimentsQuantites.entrySet()) {
            JournalAliment journalAliment = new JournalAliment(
                    date, entry.getKey(), user, repas, entry.getValue()
            );
            journalAliments.add(journalAliment);
        }

        journalAlimentRepository.saveAll(journalAliments);
    }

    @Override
    @Transactional
    public void updateJournalEntry(LocalDate date, int repas, JournalInputDTO journalInputDTO) {
        User user = getCurrentUser();
        validateRepasNumber(repas);

        JournalId journalId = new JournalId(user.getId(), date, repas);

        // Vérifier que le journal existe
        if (!journalRepository.existsById(journalId)) {
            throw new NotFoundException("Aucun journal trouvé pour cette date et ce repas");
        }

        // Supprimer les anciennes entrées JournalAliment pour ce repas
        List<JournalAliment> anciennesEntrees = journalAlimentRepository
                .findByJournalAlimentId_UserIdAndJournalAlimentId_DateAndJournalAlimentId_Repas(
                        user.getId(), date, repas);

        journalAlimentRepository.deleteAll(anciennesEntrees);

        // Valider et récupérer les nouveaux aliments
        Map<Aliment, Float> alimentsQuantites = validateAndGetAliments(journalInputDTO.alimentQuantites(), user);

        // Calculer les nouvelles calories totales
        float caloriesTotal = nutritionalCalculator.calculateTotalCalories(alimentsQuantites);

        // Mettre à jour le Journal
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new NotFoundException("Journal non trouvé"));
        journal.setCalories(caloriesTotal);
        journalRepository.save(journal);

        // Créer les nouvelles entrées JournalAliment
        List<JournalAliment> nouveauxJournalAliments = new ArrayList<>();
        for (Map.Entry<Aliment, Float> entry : alimentsQuantites.entrySet()) {
            JournalAliment journalAliment = new JournalAliment(
                    date, entry.getKey(), user, repas, entry.getValue()
            );
            nouveauxJournalAliments.add(journalAliment);
        }

        journalAlimentRepository.saveAll(nouveauxJournalAliments);
    }

    @Override
    public JournalDetails getJournalDetails(LocalDate date) {
        User user = getCurrentUser();

        // Récupérer tous les JournalAliment de l'utilisateur pour cette date
        List<JournalAliment> journalAliments = journalAlimentRepository
                .findByJournalAlimentId_UserIdAndJournalAlimentId_Date(user.getId(), date);

        if (journalAliments.isEmpty()) {
            return new JournalDetails(date, null, Collections.emptyList(), new NutrientTotals());
        }

        // Convertir en JournalAlimentDTO avec le mapper
        List<JournalAlimentDTO> alimentDTOs = journalAlimentMapper.toDTOList(journalAliments);

        // Calculer les totaux nutritionnels
        Map<Aliment, Float> alimentsQuantites = journalAliments.stream()
                .collect(Collectors.toMap(
                        JournalAliment::getAliment,
                        JournalAliment::getQuantite,
                        Float::sum
                ));

        NutrientTotals nutrients = nutritionalCalculator.calculateTotals(alimentsQuantites);

        return new JournalDetails(date, null, alimentDTOs, nutrients);
    }

    @Override
    public JournalDetails getJournalMealDetails(LocalDate date, int repas) {
        User user = getCurrentUser();
        validateRepasNumber(repas);

        // Récupérer les JournalAliment pour ce repas spécifique
        List<JournalAliment> journalAliments = journalAlimentRepository
                .findByJournalAlimentId_UserIdAndJournalAlimentId_DateAndJournalAlimentId_Repas(
                        user.getId(), date, repas);

        if (journalAliments.isEmpty()) {
            return new JournalDetails(date, repas, Collections.emptyList(), new NutrientTotals());
        }

        // Convertir en JournalAlimentDTO avec le mapper
        List<JournalAlimentDTO> alimentDTOs = journalAlimentMapper.toDTOList(journalAliments);

        // Calculer les totaux nutritionnels pour ce repas
        Map<Aliment, Float> alimentsQuantites = journalAliments.stream()
                .collect(Collectors.toMap(
                        JournalAliment::getAliment,
                        JournalAliment::getQuantite,
                        Float::sum
                ));

        NutrientTotals nutrients = nutritionalCalculator.calculateTotals(alimentsQuantites);

        return new JournalDetails(date, repas, alimentDTOs, nutrients);
    }

    /**
     * Récupère l'utilisateur actuellement connecté
     */
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Valide que le numéro de repas est correct (1, 2 ou 3)
     */
    private void validateRepasNumber(int repas) {
        if (repas < 1 || repas > 3) {
            throw new ApiException("Le numéro de repas doit être 1 (petit-déjeuner), 2 (déjeuner) ou 3 (dîner)");
        }
    }

    /**
     * Valide les aliments et leurs quantités, et retourne une Map agrégée
     */
    private Map<Aliment, Float> validateAndGetAliments(List<JournalAlimentDTO> alimentQuantites, User user) {
        if (alimentQuantites == null || alimentQuantites.isEmpty()) {
            throw new ApiException("La liste des aliments ne peut pas être vide");
        }

        Map<Aliment, Float> alimentsQuantitesMap = new HashMap<>();

        for (JournalAlimentDTO alimentDTO : alimentQuantites) {
            // Validation de la quantité
            if (alimentDTO.quantite() <= 0) {
                throw new ApiException("La quantité doit être positive pour l'aliment ID: " + alimentDTO.alimentId());
            }

            // Récupération de l'aliment
            Aliment aliment = alimentRepository.findById(alimentDTO.alimentId())
                    .orElseThrow(() -> new NotFoundException("Aliment avec l'ID " + alimentDTO.alimentId() + " non trouvé"));

            // Vérification des droits d'accès
            if (!aliment.getUser().equals(user) && aliment.getUser().getId() != 1L) {
                throw new ForbiddenException("Vous n'avez pas accès à l'aliment avec l'ID " + alimentDTO.alimentId());
            }

            // Agrégation des quantités si l'aliment est déjà présent
            alimentsQuantitesMap.merge(aliment, alimentDTO.quantite(), Float::sum);
        }

        return alimentsQuantitesMap;
    }
}