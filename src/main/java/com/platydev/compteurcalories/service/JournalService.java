package com.platydev.compteurcalories.service;

import com.platydev.compteurcalories.dto.input.JournalInputDTO;
import com.platydev.compteurcalories.dto.output.JournalDTO;
import com.platydev.compteurcalories.dto.output.JournalDetails;

import java.time.LocalDate;

public interface JournalService {

    /**
     * Récupère le résumé des calories par repas pour une date donnée
     * @param date Date du journal
     * @return JournalDTO avec les calories de chaque repas
     */
    JournalDTO getJournalByDate(LocalDate date);

    /**
     * Ajoute des aliments à un repas spécifique
     * @param date Date du journal
     * @param repas Numéro du repas (1=petit-déjeuner, 2=déjeuner, 3=dîner)
     * @param journalInputDTO Données du journal à ajouter
     */
    void addJournalEntry(LocalDate date, int repas, JournalInputDTO journalInputDTO);

    /**
     * Modifie les aliments d'un repas spécifique
     * @param date Date du journal
     * @param repas Numéro du repas (1=petit-déjeuner, 2=déjeuner, 3=dîner)
     * @param journalInputDTO Nouvelles données du journal
     */
    void updateJournalEntry(LocalDate date, int repas, JournalInputDTO journalInputDTO);

    /**
     * Récupère les détails complets de tous les repas d'une journée
     * @param date Date du journal
     * @return JournalDetails avec la liste des aliments et les totaux nutritionnels
     */
    JournalDetails getJournalDetails(LocalDate date);

    /**
     * Récupère les détails d'un repas spécifique
     * @param date Date du journal
     * @param repas Numéro du repas (1=petit-déjeuner, 2=déjeuner, 3=dîner)
     * @return JournalDetails avec la liste des aliments et les totaux nutritionnels du repas
     */
    JournalDetails getJournalMealDetails(LocalDate date, int repas);
}