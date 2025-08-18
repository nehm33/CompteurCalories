package com.platydev.compteurcalories.entity;

import com.platydev.compteurcalories.entity.security.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "journal")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Journal {

    @EmbeddedId
    private JournalId journalId;

    private float calories;

    public Journal(LocalDate date, User user, int repas, float calories) {
        this.journalId = new JournalId(user.getId(), date, repas);
        this.calories = calories;
    }
}
