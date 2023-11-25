package beans;

import java.util.Map;

public class Plat {

    private String nom;
    private double nbPortions;
    private Map<Aliment, Double> aliments;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getNbPortions() {
        return nbPortions;
    }

    public void setNbPortions(double nbPortions) {
        this.nbPortions = nbPortions;
    }

    public Map<Aliment, Double> getAliments() {
        return aliments;
    }

    public void setAliments(Map<Aliment, Double> aliments) {
        this.aliments = aliments;
    }
}