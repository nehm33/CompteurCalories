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
    private String nomPlat;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("nomAliment")
    private String nomAliment;

    private double quantite;

    public Recette(String nomPlat, String nomAliment, double quantite) {
        this.nomPlat = nomPlat;
        this.nomAliment = nomAliment;
        this.quantite = quantite;
    }
}
