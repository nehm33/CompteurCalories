package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "aliments")
@NoArgsConstructor
@Getter
@Setter
public class Aliment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(max = 100)
    private String nom;

    @Positive
    private double calories;

    @NotBlank
    @Size(max = 2)
    private String unite;

    @Column(name = "Mat_Gras")
    @PositiveOrZero
    private double matieresGrasses;

    @Column(name = "Mat_Gras_S")
    @PositiveOrZero
    private double matieresGrassesSatures;

    @Column(name = "Mat_Gras_MI")
    @PositiveOrZero
    private double matieresGrassesMonoInsaturees;

    @Column(name = "Mat_Gras_PI")
    @PositiveOrZero
    private double matieresGrassesPolyInsaturees;

    @Column(name = "Mat_Gras_T")
    @PositiveOrZero
    private double matieresGrassesTrans;

    @PositiveOrZero
    private double proteines;

    @PositiveOrZero
    private double glucides;

    @PositiveOrZero
    private double sucre;

    @PositiveOrZero
    private double fibres;

    @PositiveOrZero
    private double sel;

    @PositiveOrZero
    private double cholesterol;

    @Column(name = "pro_vit_A")
    @PositiveOrZero
    private double provitamineA;

    @Column(name = "vit_A")
    @PositiveOrZero
    private double vitamineA;

    @Column(name = "vit_B1")
    @PositiveOrZero
    private double vitamineB1;

    @Column(name = "vit_B2")
    @PositiveOrZero
    private double vitamineB2;

    @Column(name = "vit_B3")
    @PositiveOrZero
    private double vitamineB3;

    @Column(name = "vit_B5")
    @PositiveOrZero
    private double vitamineB5;

    @Column(name = "vit_B6")
    @PositiveOrZero
    private double vitamineB6;

    @Column(name = "vit_B8")
    @PositiveOrZero
    private double vitamineB8;

    @Column(name = "vit_B9")
    @PositiveOrZero
    private double vitamineB9;

    @Column(name = "vit_B11")
    @PositiveOrZero
    private double vitamineB11;

    @Column(name = "vit_B12")
    @PositiveOrZero
    private double vitamineB12;

    @Column(name = "vit_C")
    @PositiveOrZero
    private double vitamineC;

    @Column(name = "vit_D")
    @PositiveOrZero
    private double vitamineD;

    @Column(name = "vit_E")
    @PositiveOrZero
    private double vitamineE;

    @Column(name = "vit_K1")
    @PositiveOrZero
    private double vitamineK1;

    @Column(name = "vit_K2")
    @PositiveOrZero
    private double vitamineK2;

    @PositiveOrZero
    private double Ars;

    @PositiveOrZero
    private double B;

    @PositiveOrZero
    private double Ca;

    @PositiveOrZero
    private double Cl;

    @PositiveOrZero
    private double choline;

    @PositiveOrZero
    private double Cr;

    @PositiveOrZero
    private double Co;

    @PositiveOrZero
    private double Cu;

    @PositiveOrZero
    private double Fe;

    @PositiveOrZero
    private double F;

    @PositiveOrZero
    private double I;

    @PositiveOrZero
    private double Mg;

    @PositiveOrZero
    private double Mn;

    @PositiveOrZero
    private double Mo;

    @PositiveOrZero
    private double Na;

    @PositiveOrZero
    private double P;

    @PositiveOrZero
    private double K;

    @PositiveOrZero
    private double Rb;

    @PositiveOrZero
    private double SiO;

    @PositiveOrZero
    private double S;

    @PositiveOrZero
    private double Se;

    @PositiveOrZero
    private double V;

    @PositiveOrZero
    private double Sn;

    @PositiveOrZero
    private double Zn;

    @OneToOne(mappedBy = "aliment", cascade = CascadeType.ALL)
    private CodeBarre codeBarre;

    @OneToMany(mappedBy = "aliment", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Recette> recettes;

    @OneToMany(mappedBy = "aliment", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<JournalAliment> journalAlimentList;

    public Aliment(String nom, double calories, String unite, double matieresGrasses, double matieresGrassesSatures, double matieresGrassesMonoInsaturees, double matieresGrassesPolyInsaturees, double matieresGrassesTrans, double proteines, double glucides, double sucre, double fibres, double sel, double cholesterol, double provitamineA, double vitamineA, double vitamineB1, double vitamineB2, double vitamineB3, double vitamineB5, double vitamineB6, double vitamineB8, double vitamineB9, double vitamineB11, double vitamineB12, double vitamineC, double vitamineD, double vitamineE, double vitamineK1, double vitamineK2, double ars, double b, double ca, double cl, double choline, double cr, double co, double cu, double fe, double f, double i, double mg, double mn, double mo, double na, double p, double k, double rb, double siO, double s, double se, double v, double sn, double zn) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aliment aliment)) return false;

        if (Double.compare(getCalories(), aliment.getCalories()) != 0) return false;
        if (Double.compare(getMatieresGrasses(), aliment.getMatieresGrasses()) != 0) return false;
        if (Double.compare(getMatieresGrassesSatures(), aliment.getMatieresGrassesSatures()) != 0) return false;
        if (Double.compare(getMatieresGrassesMonoInsaturees(), aliment.getMatieresGrassesMonoInsaturees()) != 0)
            return false;
        if (Double.compare(getMatieresGrassesPolyInsaturees(), aliment.getMatieresGrassesPolyInsaturees()) != 0)
            return false;
        if (Double.compare(getMatieresGrassesTrans(), aliment.getMatieresGrassesTrans()) != 0) return false;
        if (Double.compare(getProteines(), aliment.getProteines()) != 0) return false;
        if (Double.compare(getGlucides(), aliment.getGlucides()) != 0) return false;
        if (Double.compare(getSucre(), aliment.getSucre()) != 0) return false;
        if (Double.compare(getFibres(), aliment.getFibres()) != 0) return false;
        if (Double.compare(getSel(), aliment.getSel()) != 0) return false;
        if (Double.compare(getCholesterol(), aliment.getCholesterol()) != 0) return false;
        if (Double.compare(getProvitamineA(), aliment.getProvitamineA()) != 0) return false;
        if (Double.compare(getVitamineA(), aliment.getVitamineA()) != 0) return false;
        if (Double.compare(getVitamineB1(), aliment.getVitamineB1()) != 0) return false;
        if (Double.compare(getVitamineB2(), aliment.getVitamineB2()) != 0) return false;
        if (Double.compare(getVitamineB3(), aliment.getVitamineB3()) != 0) return false;
        if (Double.compare(getVitamineB5(), aliment.getVitamineB5()) != 0) return false;
        if (Double.compare(getVitamineB6(), aliment.getVitamineB6()) != 0) return false;
        if (Double.compare(getVitamineB8(), aliment.getVitamineB8()) != 0) return false;
        if (Double.compare(getVitamineB9(), aliment.getVitamineB9()) != 0) return false;
        if (Double.compare(getVitamineB11(), aliment.getVitamineB11()) != 0) return false;
        if (Double.compare(getVitamineB12(), aliment.getVitamineB12()) != 0) return false;
        if (Double.compare(getVitamineC(), aliment.getVitamineC()) != 0) return false;
        if (Double.compare(getVitamineD(), aliment.getVitamineD()) != 0) return false;
        if (Double.compare(getVitamineE(), aliment.getVitamineE()) != 0) return false;
        if (Double.compare(getVitamineK1(), aliment.getVitamineK1()) != 0) return false;
        if (Double.compare(getVitamineK2(), aliment.getVitamineK2()) != 0) return false;
        if (Double.compare(getArs(), aliment.getArs()) != 0) return false;
        if (Double.compare(getB(), aliment.getB()) != 0) return false;
        if (Double.compare(getCa(), aliment.getCa()) != 0) return false;
        if (Double.compare(getCl(), aliment.getCl()) != 0) return false;
        if (Double.compare(getCholine(), aliment.getCholine()) != 0) return false;
        if (Double.compare(getCr(), aliment.getCr()) != 0) return false;
        if (Double.compare(getCo(), aliment.getCo()) != 0) return false;
        if (Double.compare(getCu(), aliment.getCu()) != 0) return false;
        if (Double.compare(getFe(), aliment.getFe()) != 0) return false;
        if (Double.compare(getF(), aliment.getF()) != 0) return false;
        if (Double.compare(getI(), aliment.getI()) != 0) return false;
        if (Double.compare(getMg(), aliment.getMg()) != 0) return false;
        if (Double.compare(getMn(), aliment.getMn()) != 0) return false;
        if (Double.compare(getMo(), aliment.getMo()) != 0) return false;
        if (Double.compare(getNa(), aliment.getNa()) != 0) return false;
        if (Double.compare(getP(), aliment.getP()) != 0) return false;
        if (Double.compare(getK(), aliment.getK()) != 0) return false;
        if (Double.compare(getRb(), aliment.getRb()) != 0) return false;
        if (Double.compare(getSiO(), aliment.getSiO()) != 0) return false;
        if (Double.compare(getS(), aliment.getS()) != 0) return false;
        if (Double.compare(getSe(), aliment.getSe()) != 0) return false;
        if (Double.compare(getV(), aliment.getV()) != 0) return false;
        if (Double.compare(getSn(), aliment.getSn()) != 0) return false;
        if (Double.compare(getZn(), aliment.getZn()) != 0) return false;
        if (!getNom().equals(aliment.getNom())) return false;
        return getUnite().equals(aliment.getUnite());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getNom().hashCode();
        temp = Double.doubleToLongBits(getCalories());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getUnite().hashCode();
        temp = Double.doubleToLongBits(getMatieresGrasses());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getMatieresGrassesSatures());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getMatieresGrassesMonoInsaturees());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getMatieresGrassesPolyInsaturees());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getMatieresGrassesTrans());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getProteines());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getGlucides());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSucre());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getFibres());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSel());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCholesterol());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getProvitamineA());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineA());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineB1());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineB2());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineB3());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineB5());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineB6());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineB8());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineB9());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineB11());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineB12());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineC());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineD());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineE());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineK1());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamineK2());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getArs());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getB());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCa());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCl());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCholine());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCr());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCo());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCu());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getFe());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getF());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getI());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getMg());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getMn());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getMo());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getNa());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getP());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getK());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getRb());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSiO());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getS());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSe());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getV());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSn());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getZn());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Aliment{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
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
                ", vitamineK1=" + vitamineK1 +
                ", vitamineK2=" + vitamineK2 +
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
