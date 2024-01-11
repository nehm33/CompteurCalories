package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "plats")
@NoArgsConstructor
@Getter
@Setter
public class Plat {

    @Id
    private String nom;

    @Column(name = "portions")
    private double nbPortions;

    @OneToMany(mappedBy = "plat", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Recette> recettes;

    @OneToMany(mappedBy = "plat", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<JournalPlat> journalPlatList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plat plat = (Plat) o;

        if (Double.compare(nbPortions, plat.nbPortions) != 0) return false;
        return nom.equals(plat.nom);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = nom.hashCode();
        temp = Double.doubleToLongBits(nbPortions);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Plat{" +
                "nom='" + nom + '\'' +
                ", nbPortions=" + nbPortions +
                '}';
    }
}
