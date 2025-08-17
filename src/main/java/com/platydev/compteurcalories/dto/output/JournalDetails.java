package com.platydev.compteurcalories.dto.output;

import java.time.LocalDate;
import java.util.List;

public record JournalDetails(LocalDate date, Integer repas, List<JournalAlimentDTO> alimentQuantites,
                             NutrientTotals nutrients) {
}
