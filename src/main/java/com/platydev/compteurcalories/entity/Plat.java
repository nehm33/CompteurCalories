package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "nomPlat", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<JournalPlat> journalPlatList;
}
