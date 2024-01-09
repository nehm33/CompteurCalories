package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "composition_journal_aliment")
@NoArgsConstructor
@Getter
@Setter
public class JournalAliment {

    @EmbeddedId
    private JournalAlimentId journalAlimentId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("nomAliment")
    private String nomAliment;

    @MapsId("date")
    private Date date;

    private double quantite;

    public JournalAliment(String nomAliment, Date date, double quantite) {
        this.nomAliment = nomAliment;
        this.date = date;
        this.quantite = quantite;
    }
}
