package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "plats")
@NoArgsConstructor
@Getter
@Setter
public class Plat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(max = 100)
    private String nom;

    @Column(name = "portions")
    @Positive
    private double nbPortions;

    @OneToMany(mappedBy = "plat", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Recette> recettes;

    @OneToMany(mappedBy = "plat", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<JournalPlat> journalPlatList;

    public Plat(String nom, double nbPortions) {
        this.nom = nom;
        this.nbPortions = nbPortions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plat plat)) return false;

        if (Double.compare(getNbPortions(), plat.getNbPortions()) != 0) return false;
        return getNom().equals(plat.getNom());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getNom().hashCode();
        temp = Double.doubleToLongBits(getNbPortions());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Plat{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", nbPortions=" + nbPortions +
                '}';
    }
}
