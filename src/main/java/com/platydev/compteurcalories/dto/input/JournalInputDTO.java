package com.platydev.compteurcalories.dto.input;

import com.platydev.compteurcalories.dto.output.JournalAlimentDTO;

import java.time.LocalDate;
import java.util.List;

public record JournalInputDTO(LocalDate date, Integer repas, List<JournalAlimentDTO> alimentQuantites) {
}
