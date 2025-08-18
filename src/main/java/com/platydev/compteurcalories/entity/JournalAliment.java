package com.platydev.compteurcalories.entity;

import com.platydev.compteurcalories.entity.security.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "composition_journal_aliment")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JournalAliment {

    @EmbeddedId
    private JournalAlimentId journalAlimentId;

    @ManyToOne
    @MapsId("alimentId")
    @JoinColumn(name = "aliment_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Aliment aliment;

    private float quantite;

    public JournalAliment(LocalDate date, Aliment aliment, User user, int repas, float quantite) {
        this.journalAlimentId = new JournalAlimentId(aliment.getId(), user.getId(), date, repas);
        this.aliment = aliment;
        this.quantite = quantite;
    }
}
