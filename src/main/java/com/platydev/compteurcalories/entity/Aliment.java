package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "aliments")
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "pro_vit_A")
    private double provitamineA;

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

    @Column(name = "vit_B9")
    private double vitamineB9;

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

    private double Na;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aliment aliment = (Aliment) o;

        if (Double.compare(calories, aliment.calories) != 0) return false;
        if (Double.compare(matieresGrasses, aliment.matieresGrasses) != 0) return false;
        if (Double.compare(matieresGrassesSatures, aliment.matieresGrassesSatures) != 0) return false;
        if (Double.compare(matieresGrassesMonoInsaturees, aliment.matieresGrassesMonoInsaturees) != 0)
            return false;
        if (Double.compare(matieresGrassesPolyInsaturees, aliment.matieresGrassesPolyInsaturees) != 0)
            return false;
        if (Double.compare(matieresGrassesTrans, aliment.matieresGrassesTrans) != 0) return false;
        if (Double.compare(proteines, aliment.proteines) != 0) return false;
        if (Double.compare(glucides, aliment.glucides) != 0) return false;
        if (Double.compare(sucre, aliment.sucre) != 0) return false;
        if (Double.compare(fibres, aliment.fibres) != 0) return false;
        if (Double.compare(sel, aliment.sel) != 0) return false;
        if (Double.compare(cholesterol, aliment.cholesterol) != 0) return false;
        if (Double.compare(provitamineA, aliment.provitamineA) != 0) return false;
        if (Double.compare(vitamineA, aliment.vitamineA) != 0) return false;
        if (Double.compare(vitamineB1, aliment.vitamineB1) != 0) return false;
        if (Double.compare(vitamineB2, aliment.vitamineB2) != 0) return false;
        if (Double.compare(vitamineB3, aliment.vitamineB3) != 0) return false;
        if (Double.compare(vitamineB5, aliment.vitamineB5) != 0) return false;
        if (Double.compare(vitamineB6, aliment.vitamineB6) != 0) return false;
        if (Double.compare(vitamineB8, aliment.vitamineB8) != 0) return false;
        if (Double.compare(vitamineB9, aliment.vitamineB9) != 0) return false;
        if (Double.compare(vitamineB11, aliment.vitamineB11) != 0) return false;
        if (Double.compare(vitamineB12, aliment.vitamineB12) != 0) return false;
        if (Double.compare(vitamineC, aliment.vitamineC) != 0) return false;
        if (Double.compare(vitamineD, aliment.vitamineD) != 0) return false;
        if (Double.compare(vitamineE, aliment.vitamineE) != 0) return false;
        if (Double.compare(vitamineK, aliment.vitamineK) != 0) return false;
        if (Double.compare(Ars, aliment.Ars) != 0) return false;
        if (Double.compare(B, aliment.B) != 0) return false;
        if (Double.compare(Ca, aliment.Ca) != 0) return false;
        if (Double.compare(Cl, aliment.Cl) != 0) return false;
        if (Double.compare(choline, aliment.choline) != 0) return false;
        if (Double.compare(Cr, aliment.Cr) != 0) return false;
        if (Double.compare(Co, aliment.Co) != 0) return false;
        if (Double.compare(Cu, aliment.Cu) != 0) return false;
        if (Double.compare(Fe, aliment.Fe) != 0) return false;
        if (Double.compare(F, aliment.F) != 0) return false;
        if (Double.compare(I, aliment.I) != 0) return false;
        if (Double.compare(Mg, aliment.Mg) != 0) return false;
        if (Double.compare(Mn, aliment.Mn) != 0) return false;
        if (Double.compare(Mo, aliment.Mo) != 0) return false;
        if (Double.compare(Na, aliment.Na) != 0) return false;
        if (Double.compare(P, aliment.P) != 0) return false;
        if (Double.compare(K, aliment.K) != 0) return false;
        if (Double.compare(Rb, aliment.Rb) != 0) return false;
        if (Double.compare(SiO, aliment.SiO) != 0) return false;
        if (Double.compare(S, aliment.S) != 0) return false;
        if (Double.compare(Se, aliment.Se) != 0) return false;
        if (Double.compare(V, aliment.V) != 0) return false;
        if (Double.compare(Sn, aliment.Sn) != 0) return false;
        if (Double.compare(Zn, aliment.Zn) != 0) return false;
        if (!nom.equals(aliment.nom)) return false;
        return unite.equals(aliment.unite);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = nom.hashCode();
        temp = Double.doubleToLongBits(calories);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + unite.hashCode();
        temp = Double.doubleToLongBits(matieresGrasses);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(matieresGrassesSatures);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(matieresGrassesMonoInsaturees);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(matieresGrassesPolyInsaturees);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(matieresGrassesTrans);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(proteines);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(glucides);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(sucre);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(fibres);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(sel);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(cholesterol);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(provitamineA);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineA);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineB1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineB2);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineB3);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineB5);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineB6);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineB8);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineB9);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineB11);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineB12);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineC);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineD);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineE);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vitamineK);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Ars);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(B);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Ca);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Cl);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(choline);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Cr);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Co);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Cu);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Fe);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(F);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(I);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Mg);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Mn);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Mo);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Na);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(P);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(K);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Rb);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(SiO);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(S);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Se);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(V);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Sn);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Zn);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Aliment{" +
                "nom='" + nom + '\'' +
                ", calories=" + calories +
                ", unite='" + unite + '\'' +
                ", matieresGrasses=" + matieresGrasses +
                ", matieresGrassesSatures=" + matieresGrassesSatures +
                ", matieresGrassesMonoInsaturees=" + matieresGrassesMonoInsaturees +
                ", matieresGrassesPolyInsaturees=" + matieresGrassesPolyInsaturees +
                ", matieresGrassesTrans=" + matieresGrassesTrans +
                ", proteines=" + proteines +
                ", glucides=" + glucides +
                ", sucre=" + sucre +
                ", fibres=" + fibres +
                ", sel=" + sel +
                ", cholesterol=" + cholesterol +
                ", provitamineA=" + provitamineA +
                ", vitamineA=" + vitamineA +
                ", vitamineB1=" + vitamineB1 +
                ", vitamineB2=" + vitamineB2 +
                ", vitamineB3=" + vitamineB3 +
                ", vitamineB5=" + vitamineB5 +
                ", vitamineB6=" + vitamineB6 +
                ", vitamineB8=" + vitamineB8 +
                ", vitamineB9=" + vitamineB9 +
                ", vitamineB11=" + vitamineB11 +
                ", vitamineB12=" + vitamineB12 +
                ", vitamineC=" + vitamineC +
                ", vitamineD=" + vitamineD +
                ", vitamineE=" + vitamineE +
                ", vitamineK=" + vitamineK +
                ", Ars=" + Ars +
                ", B=" + B +
                ", Ca=" + Ca +
                ", Cl=" + Cl +
                ", choline=" + choline +
                ", Cr=" + Cr +
                ", Co=" + Co +
                ", Cu=" + Cu +
                ", Fe=" + Fe +
                ", F=" + F +
                ", I=" + I +
                ", Mg=" + Mg +
                ", Mn=" + Mn +
                ", Mo=" + Mo +
                ", Na=" + Na +
                ", P=" + P +
                ", K=" + K +
                ", Rb=" + Rb +
                ", SiO=" + SiO +
                ", S=" + S +
                ", Se=" + Se +
                ", V=" + V +
                ", Sn=" + Sn +
                ", Zn=" + Zn +
                '}';
    }
}
