package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recettes")
@NoArgsConstructor
@Getter
@Setter
public class Recette {

    @EmbeddedId
    private RecetteId recetteId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("nomPlat")
    @JoinColumn(name = "nom_plat")
    private Plat plat;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("nomAliment")
    @JoinColumn(name = "nom_aliment")
    private Aliment aliment;

    private double quantite;

    public Recette(Plat plat, Aliment aliment, double quantite) {
        this.plat = plat;
        this.aliment = aliment;
        this.quantite = quantite;
    }
}
