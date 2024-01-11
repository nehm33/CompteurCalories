package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "composition_journal_aliment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JournalAliment {

    @EmbeddedId
    private JournalAlimentId journalAlimentId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("nomAliment")
    @JoinColumn(name = "nom_aliment")
    private Aliment aliment;

    private double quantite;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JournalAliment that = (JournalAliment) o;

        if (Double.compare(quantite, that.quantite) != 0) return false;
        if (!journalAlimentId.equals(that.journalAlimentId)) return false;
        return aliment.equals(that.aliment);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = journalAlimentId.hashCode();
        result = 31 * result + aliment.hashCode();
        temp = Double.doubleToLongBits(quantite);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "JournalAliment{" +
                "journalAlimentId=" + journalAlimentId +
                ", aliment=" + aliment +
                ", quantite=" + quantite +
                '}';
    }
}
