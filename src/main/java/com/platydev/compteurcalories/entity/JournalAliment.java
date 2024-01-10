package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "composition_journal_aliment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JournalAliment {

    @EmbeddedId
    private JournalAlimentId journalAlimentId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("nomAliment")
    @JoinColumn(name = "nom_aliment")
    private Aliment aliment;

    private double quantite;
}
