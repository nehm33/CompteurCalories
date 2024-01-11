package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recettes")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recette recette = (Recette) o;

        if (Double.compare(quantite, recette.quantite) != 0) return false;
        if (!plat.equals(recette.plat)) return false;
        return aliment.equals(recette.aliment);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = plat.hashCode();
        result = 31 * result + aliment.hashCode();
        temp = Double.doubleToLongBits(quantite);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Recette{" +
                "plat=" + plat +
                ", aliment=" + aliment +
                ", quantite=" + quantite +
                '}';
    }
}
