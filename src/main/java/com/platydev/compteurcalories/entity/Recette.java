package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "recettes")
@NoArgsConstructor
@Data
public class Recette {

    @EmbeddedId
    private RecetteId recetteId;

    @ManyToOne
    @MapsId("platId")
    @JoinColumn(name = "plat_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Plat plat;

    @ManyToOne
    @MapsId("alimentId")
    @JoinColumn(name = "aliment_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Aliment aliment;

    @Positive
    private float quantite;

    public Recette(Plat plat, Aliment aliment, float quantite) {
        this.recetteId = new RecetteId(plat.getId(), aliment.getId());
        this.plat = plat;
        this.aliment = aliment;
        this.quantite = quantite;
    }
}
