package com.platydev.compteurcalories.controller;

import com.platydev.compteurcalories.dto.input.JournalInputDTO;
import com.platydev.compteurcalories.dto.output.JournalDTO;
import com.platydev.compteurcalories.dto.output.JournalDetails;
import com.platydev.compteurcalories.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/journaux")
public class JournalController {

    private final JournalService journalService;

    @Autowired
    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    /**
     * Récupère le résumé des calories par repas pour une date donnée
     * @param dateStr Date au format yyyyMMdd
     * @return JournalDTO avec les calories de chaque repas
     */
    @GetMapping("/{date}")
    public JournalDTO getJournalByDate(@PathVariable("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return journalService.getJournalByDate(date);
    }

    /**
     * Ajoute des aliments à un repas spécifique
     * @param dateStr Date au format yyyyMMdd
     * @param repas Numéro du repas (1=petit-déjeuner, 2=déjeuner, 3=dîner)
     * @param journalInputDTO Données du journal à ajouter
     * @return ResponseEntity avec statut CREATED
     */
    @PostMapping("/{date}/repas/{repas}")
    public ResponseEntity<Void> addJournalEntry(
            @PathVariable("date") String dateStr,
            @PathVariable int repas,
            @RequestBody JournalInputDTO journalInputDTO) {

        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        journalService.addJournalEntry(date, repas, journalInputDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Modifie les aliments d'un repas spécifique
     * @param dateStr Date au format yyyyMMdd
     * @param repas Numéro du repas (1=petit-déjeuner, 2=déjeuner, 3=dîner)
     * @param journalInputDTO Nouvelles données du journal
     * @return ResponseEntity avec statut NO_CONTENT
     */
    @PutMapping("/{date}/repas/{repas}")
    public ResponseEntity<Void> updateJournalEntry(
            @PathVariable("date") String dateStr,
            @PathVariable int repas,
            @RequestBody JournalInputDTO journalInputDTO) {

        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        journalService.updateJournalEntry(date, repas, journalInputDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Récupère les détails complets de tous les repas d'une journée
     * @param dateStr Date au format yyyyMMdd
     * @return JournalDetails avec la liste des aliments et les totaux nutritionnels
     */
    @GetMapping("/{date}/details")
    public JournalDetails getJournalDetails(@PathVariable("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return journalService.getJournalDetails(date);
    }

    /**
     * Récupère les détails d'un repas spécifique
     * @param dateStr Date au format yyyyMMdd
     * @param repas Numéro du repas (1=petit-déjeuner, 2=déjeuner, 3=dîner)
     * @return JournalDetails avec la liste des aliments et les totaux nutritionnels du repas
     */
    @GetMapping("/{date}/repas/{repas}/details")
    public JournalDetails getJournalMealDetails(
            @PathVariable("date") String dateStr,
            @PathVariable int repas) {

        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return journalService.getJournalMealDetails(date, repas);
    }
}