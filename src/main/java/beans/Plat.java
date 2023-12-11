package beans;

import java.util.HashMap;
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

    public void addAliment(Aliment aliment, double quantite) {
        if (aliments == null) {
            aliments = new HashMap<>();
        }
        if (aliments.containsKey(aliment)) {
            aliments.put(aliment, aliments.get(aliment)+quantite);
        } else {
            aliments.put(aliment, quantite);
        }
    }

    public void removeAliment(Aliment aliment) {
        aliments.remove(aliment);
    }

    public boolean hasAliment(Aliment aliment) {
        return aliments != null && aliments.containsKey(aliment);
    }

    public double getQuantiteOf(Aliment aliment) {
        if (hasAliment(aliment)) {
            return aliments.get(aliment);
        }
        return 0;
    }
}
