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
public class Recette {

    @EmbeddedId
    private RecetteId recetteId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("platId")
    @JoinColumn(name = "plat_id")
    private Plat plat;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("alimentId")
    @JoinColumn(name = "aliment_id")
    private Aliment aliment;

    private double quantite;

    public Recette(Plat plat, Aliment aliment, double quantite) {
        this.recetteId = new RecetteId(plat.getId(), aliment.getId());
        this.plat = plat;
        this.aliment = aliment;
        this.quantite = quantite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recette recette)) return false;

        if (Double.compare(getQuantite(), recette.getQuantite()) != 0) return false;
        if (!getRecetteId().equals(recette.getRecetteId())) return false;
        if (!getPlat().equals(recette.getPlat())) return false;
        return getAliment().equals(recette.getAliment());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getRecetteId().hashCode();
        result = 31 * result + getPlat().hashCode();
        result = 31 * result + getAliment().hashCode();
        temp = Double.doubleToLongBits(getQuantite());
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
