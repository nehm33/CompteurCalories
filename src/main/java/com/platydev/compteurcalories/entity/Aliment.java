package com.platydev.compteurcalories.entity;

import com.platydev.compteurcalories.entity.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "aliments")
@Data
@NoArgsConstructor
public class Aliment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(max = 100)
    private String nom;

    @Positive
    private float calories;

    @NotBlank
    @Size(max = 2)
    private String unite;

    @Column(name = "Mat_Gras")
    @PositiveOrZero
    private float matieresGrasses;

    @Column(name = "Mat_Gras_S")
    @PositiveOrZero
    private float matieresGrassesSatures;

    @Column(name = "Mat_Gras_MI")
    @PositiveOrZero
    private float matieresGrassesMonoInsaturees;

    @Column(name = "Mat_Gras_PI")
    @PositiveOrZero
    private float matieresGrassesPolyInsaturees;

    @Column(name = "Mat_Gras_T")
    @PositiveOrZero
    private float matieresGrassesTrans;

    @PositiveOrZero
    private float proteines;

    @PositiveOrZero
    private float glucides;

    @PositiveOrZero
    private float sucre;

    @PositiveOrZero
    private float fibres;

    @PositiveOrZero
    private float sel;

    @PositiveOrZero
    private float cholesterol;

    @Column(name = "pro_vit_A")
    @PositiveOrZero
    private float provitamineA;

    @Column(name = "vit_A")
    @PositiveOrZero
    private float vitamineA;

    @Column(name = "vit_B1")
    @PositiveOrZero
    private float vitamineB1;

    @Column(name = "vit_B2")
    @PositiveOrZero
    private float vitamineB2;

    @Column(name = "vit_B3")
    @PositiveOrZero
    private float vitamineB3;

    @Column(name = "vit_B5")
    @PositiveOrZero
    private float vitamineB5;

    @Column(name = "vit_B6")
    @PositiveOrZero
    private float vitamineB6;

    @Column(name = "vit_B8")
    @PositiveOrZero
    private float vitamineB8;

    @Column(name = "vit_B9")
    @PositiveOrZero
    private float vitamineB9;

    @Column(name = "vit_B11")
    @PositiveOrZero
    private float vitamineB11;

    @Column(name = "vit_B12")
    @PositiveOrZero
    private float vitamineB12;

    @Column(name = "vit_C")
    @PositiveOrZero
    private float vitamineC;

    @Column(name = "vit_D")
    @PositiveOrZero
    private float vitamineD;

    @Column(name = "vit_E")
    @PositiveOrZero
    private float vitamineE;

    @Column(name = "vit_K1")
    @PositiveOrZero
    private float vitamineK1;

    @Column(name = "vit_K2")
    @PositiveOrZero
    private float vitamineK2;

    @PositiveOrZero
    private float Ars;

    @PositiveOrZero
    private float B;

    @PositiveOrZero
    private float Ca;

    @PositiveOrZero
    private float Cl;

    @PositiveOrZero
    private float choline;

    @PositiveOrZero
    private float Cr;

    @PositiveOrZero
    private float Co;

    @PositiveOrZero
    private float Cu;

    @PositiveOrZero
    private float Fe;

    @PositiveOrZero
    private float F;

    @PositiveOrZero
    private float I;

    @PositiveOrZero
    private float Mg;

    @PositiveOrZero
    private float Mn;

    @PositiveOrZero
    private float Mo;

    @PositiveOrZero
    private float Na;

    @PositiveOrZero
    private float P;

    @PositiveOrZero
    private float K;

    @PositiveOrZero
    private float Rb;

    @PositiveOrZero
    private float SiO;

    @PositiveOrZero
    private float S;

    @PositiveOrZero
    private float Se;

    @PositiveOrZero
    private float V;

    @PositiveOrZero
    private float Sn;

    @PositiveOrZero
    private float Zn;

    @OneToOne(mappedBy = "aliment", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CodeBarre codeBarre;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @OneToOne(mappedBy = "aliment", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Plat plat;

    @OneToMany(mappedBy = "aliment", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Recette> recettes;

    @OneToMany(mappedBy = "aliment", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<JournalAliment> journalAlimentList;

    public Aliment(String nom, float calories, String unite, float matieresGrasses, float matieresGrassesSatures, float matieresGrassesMonoInsaturees, float matieresGrassesPolyInsaturees, float matieresGrassesTrans, float proteines, float glucides, float sucre, float fibres, float sel, float cholesterol, float provitamineA, float vitamineA, float vitamineB1, float vitamineB2, float vitamineB3, float vitamineB5, float vitamineB6, float vitamineB8, float vitamineB9, float vitamineB11, float vitamineB12, float vitamineC, float vitamineD, float vitamineE, float vitamineK1, float vitamineK2, float ars, float b, float ca, float cl, float choline, float cr, float co, float cu, float fe, float f, float i, float mg, float mn, float mo, float na, float p, float k, float rb, float siO, float s, float se, float v, float sn, float zn) {
        this.nom = nom;
        this.calories = calories;
        this.unite = unite;
        this.matieresGrasses = matieresGrasses;
        this.matieresGrassesSatures = matieresGrassesSatures;
        this.matieresGrassesMonoInsaturees = matieresGrassesMonoInsaturees;
        this.matieresGrassesPolyInsaturees = matieresGrassesPolyInsaturees;
        this.matieresGrassesTrans = matieresGrassesTrans;
        this.proteines = proteines;
        this.glucides = glucides;
        this.sucre = sucre;
        this.fibres = fibres;
        this.sel = sel;
        this.cholesterol = cholesterol;
        this.provitamineA = provitamineA;
        this.vitamineA = vitamineA;
        this.vitamineB1 = vitamineB1;
        this.vitamineB2 = vitamineB2;
        this.vitamineB3 = vitamineB3;
        this.vitamineB5 = vitamineB5;
        this.vitamineB6 = vitamineB6;
        this.vitamineB8 = vitamineB8;
        this.vitamineB9 = vitamineB9;
        this.vitamineB11 = vitamineB11;
        this.vitamineB12 = vitamineB12;
        this.vitamineC = vitamineC;
        this.vitamineD = vitamineD;
        this.vitamineE = vitamineE;
        this.vitamineK1 = vitamineK1;
        this.vitamineK2 = vitamineK2;
        Ars = ars;
        B = b;
        Ca = ca;
        Cl = cl;
        this.choline = choline;
        Cr = cr;
        Co = co;
        Cu = cu;
        Fe = fe;
        F = f;
        I = i;
        Mg = mg;
        Mn = mn;
        Mo = mo;
        Na = na;
        P = p;
        K = k;
        Rb = rb;
        SiO = siO;
        S = s;
        Se = se;
        V = v;
        Sn = sn;
        Zn = zn;
    }
}
