package com.platydev.compteurcalories.entity;

import com.platydev.compteurcalories.entity.security.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "journal")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Journal {

    @EmbeddedId
    private JournalId journalId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    private float calories;

    public Journal(LocalDate date, Aliment aliment, User user, int repas, float calories) {
        this.journalId = new JournalId(user.getId(), date, repas);
        this.user = user;
        this.calories = calories;
    }
}
