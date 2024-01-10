package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "aliments")
@NoArgsConstructor
@Getter
@Setter
public class Aliment {

    @Id
    private String nom;

    private double calories;

    private String unite;

    @Column(name = "Mat_Gras")
    private double matieresGrasses;

    @Column(name = "Mat_Gras_S")
    private double matieresGrassesSatures;

    @Column(name = "Mat_Gras_MI")
    private double matieresGrassesMonoInsaturees;

    @Column(name = "Mat_Gras_PI")
    private double matieresGrassesPolyInsaturees;

    @Column(name = "Mat_Gras_T")
    private double matieresGrassesTrans;

    private double proteines;

    private double glucides;

    private double sucre;

    private double fibres;

    private double sel;

    private double cholesterol;

    @Column(name = "vit_A")
    private double vitamineA;

    @Column(name = "vit_B1")
    private double vitamineB1;

    @Column(name = "vit_B2")
    private double vitamineB2;

    @Column(name = "vit_B3")
    private double vitamineB3;

    @Column(name = "vit_B5")
    private double vitamineB5;

    @Column(name = "vit_B6")
    private double vitamineB6;

    @Column(name = "vit_B8")
    private double vitamineB8;

    @Column(name = "vit_B11")
    private double vitamineB11;

    @Column(name = "vit_B12")
    private double vitamineB12;

    @Column(name = "vit_C")
    private double vitamineC;

    @Column(name = "vit_D")
    private double vitamineD;

    @Column(name = "vit_E")
    private double vitamineE;

    @Column(name = "vit_K")
    private double vitamineK;

    private double Ars;

    private double B;

    private double Ca;

    private double Cl;

    private double choline;

    private double Cr;

    private double Co;

    private double Cu;

    private double Fe;

    private double F;

    private double I;

    private double Mg;

    private double Mn;

    private double Mo;

    private double P;

    private double K;

    private double Rb;

    private double SiO;

    private double S;

    private double Se;

    private double V;

    private double Sn;

    private double Zn;

    @OneToOne(mappedBy = "aliment", cascade = CascadeType.ALL)
    private CodeBarre codeBarre;

    @OneToMany(mappedBy = "aliment", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Recette> recettes;

    @OneToMany(mappedBy = "aliment", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<JournalAliment> journalAlimentList;
}
