package beans;

import java.util.Date;
import java.util.Map;

public class Journal {

    private Date date;
    private Map<Aliment, Double> aliments;
    private Map<Plat, Double> plats;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<Aliment, Double> getAliments() {
        return aliments;
    }

    public void setAliments(Map<Aliment, Double> aliments) {
        this.aliments = aliments;
    }

    public Map<Plat, Double> getPlats() {
        return plats;
    }

    public void setPlats(Map<Plat, Double> plats) {
        this.plats = plats;
    }
}
