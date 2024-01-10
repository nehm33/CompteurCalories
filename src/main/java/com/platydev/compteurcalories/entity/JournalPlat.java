package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "composition_journal_plat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JournalPlat {

    @EmbeddedId
    private JournalPlatId journalPlatId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("nomPlat")
    @JoinColumn(name = "nom_plat")
    private Plat plat;

    private double portions;
}
