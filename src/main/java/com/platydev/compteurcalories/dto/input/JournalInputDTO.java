package com.platydev.compteurcalories.dto.input;

import com.platydev.compteurcalories.dto.output.JournalAlimentDTO;

import java.util.List;

public record JournalInputDTO(List<JournalAlimentDTO> alimentQuantites) {
}
