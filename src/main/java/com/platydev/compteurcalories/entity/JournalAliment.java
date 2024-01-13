package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

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
    @MapsId("alimentId")
    @JoinColumn(name = "aliment_id")
    private Aliment aliment;

    private double quantite;

    public JournalAliment(Date date, Aliment aliment, double quantite) {
        this.journalAlimentId = new JournalAlimentId(aliment.getId(), date);
        this.aliment = aliment;
        this.quantite = quantite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalAliment that)) return false;

        if (Double.compare(getQuantite(), that.getQuantite()) != 0) return false;
        if (!getJournalAlimentId().equals(that.getJournalAlimentId())) return false;
        return getAliment().equals(that.getAliment());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getJournalAlimentId().hashCode();
        result = 31 * result + getAliment().hashCode();
        temp = Double.doubleToLongBits(getQuantite());
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
