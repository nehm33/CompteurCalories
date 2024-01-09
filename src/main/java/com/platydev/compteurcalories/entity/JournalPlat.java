package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "composition_journal_plat")
@NoArgsConstructor
@Getter
@Setter
public class JournalPlat {

    @EmbeddedId
    private JournalPlatId journalPlatId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("nomPlat")
    private String nomPlat;

    @MapsId("date")
    private Date date;

    private double nbPortions;

    public JournalPlat(String nomPlat, Date date, double nbPortions) {
        this.nomPlat = nomPlat;
        this.date = date;
        this.nbPortions = nbPortions;
    }
}
