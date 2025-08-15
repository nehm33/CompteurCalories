package com.platydev.compteurcalories.entity;

import com.platydev.compteurcalories.entity.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "aliments")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aliment {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String nom;

    @Positive
    private Float calories;

    @NotBlank
    @Size(max = 7)
    private String unite;

    @Column(name = "Mat_Gras")
    @PositiveOrZero
    private Float matieresGrasses;

    @Column(name = "Mat_Gras_S")
    @PositiveOrZero
    private Float matieresGrassesSatures;

    @Column(name = "Mat_Gras_MI")
    @PositiveOrZero
    private Float matieresGrassesMonoInsaturees;

    @Column(name = "Mat_Gras_PI")
    @PositiveOrZero
    private Float matieresGrassesPolyInsaturees;

    @Column(name = "Mat_Gras_T")
    @PositiveOrZero
    private Float matieresGrassesTrans;

    @PositiveOrZero
    private Float proteines;

    @PositiveOrZero
    private Float glucides;

    @PositiveOrZero
    private Float sucre;

    @PositiveOrZero
    private Float fibres;

    @PositiveOrZero
    private Float sel;

    @PositiveOrZero
    private Float cholesterol;

    @Column(name = "pro_vit_A")
    @PositiveOrZero
    private Float provitamineA;

    @Column(name = "vit_A")
    @PositiveOrZero
    private Float vitamineA;

    @Column(name = "vit_B1")
    @PositiveOrZero
    private Float vitamineB1;

    @Column(name = "vit_B2")
    @PositiveOrZero
    private Float vitamineB2;

    @Column(name = "vit_B3")
    @PositiveOrZero
    private Float vitamineB3;

    @Column(name = "vit_B5")
    @PositiveOrZero
    private Float vitamineB5;

    @Column(name = "vit_B6")
    @PositiveOrZero
    private Float vitamineB6;

    @Column(name = "vit_B8")
    @PositiveOrZero
    private Float vitamineB8;

    @Column(name = "vit_B9")
    @PositiveOrZero
    private Float vitamineB9;

    @Column(name = "vit_B11")
    @PositiveOrZero
    private Float vitamineB11;

    @Column(name = "vit_B12")
    @PositiveOrZero
    private Float vitamineB12;

    @Column(name = "vit_C")
    @PositiveOrZero
    private Float vitamineC;

    @Column(name = "vit_D")
    @PositiveOrZero
    private Float vitamineD;

    @Column(name = "vit_E")
    @PositiveOrZero
    private Float vitamineE;

    @Column(name = "vit_K1")
    @PositiveOrZero
    private Float vitamineK1;

    @Column(name = "vit_K2")
    @PositiveOrZero
    private Float vitamineK2;

    @PositiveOrZero
    private Float Ars;

    @PositiveOrZero
    private Float B;

    @PositiveOrZero
    private Float Ca;

    @PositiveOrZero
    private Float Cl;

    @PositiveOrZero
    private Float choline;

    @PositiveOrZero
    private Float Cr;

    @PositiveOrZero
    private Float Co;

    @PositiveOrZero
    private Float Cu;

    @PositiveOrZero
    private Float Fe;

    @PositiveOrZero
    private Float F;

    @PositiveOrZero
    private Float I;

    @PositiveOrZero
    private Float Mg;

    @PositiveOrZero
    private Float Mn;

    @PositiveOrZero
    private Float Mo;

    @PositiveOrZero
    private Float Na;

    @PositiveOrZero
    private Float P;

    @PositiveOrZero
    private Float K;

    @PositiveOrZero
    private Float Rb;

    @PositiveOrZero
    private Float SiO;

    @PositiveOrZero
    private Float S;

    @PositiveOrZero
    private Float Se;

    @PositiveOrZero
    private Float V;

    @PositiveOrZero
    private Float Sn;

    @PositiveOrZero
    private Float Zn;

    @OneToOne(mappedBy = "aliment", cascade = CascadeType.ALL)
    @ToString.Exclude
    private CodeBarre codeBarre;

    @ManyToOne(cascade = CascadeType.MERGE)
    @ToString.Exclude
    private User user;

    @OneToOne(mappedBy = "aliment", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Plat plat;

    @OneToMany(mappedBy = "aliment", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Recette> recettes;

    @OneToMany(mappedBy = "aliment", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<JournalAliment> journalAlimentList;
}
