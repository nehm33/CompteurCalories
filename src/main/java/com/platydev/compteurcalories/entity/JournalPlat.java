package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "composition_journal_plat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JournalPlat {

    @EmbeddedId
    private JournalPlatId journalPlatId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId("nomPlat")
    @JoinColumn(name = "nom_plat")
    private Plat plat;

    private double portions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JournalPlat that = (JournalPlat) o;

        if (Double.compare(portions, that.portions) != 0) return false;
        if (!journalPlatId.equals(that.journalPlatId)) return false;
        return plat.equals(that.plat);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = journalPlatId.hashCode();
        result = 31 * result + plat.hashCode();
        temp = Double.doubleToLongBits(portions);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "JournalPlat{" +
                "journalPlatId=" + journalPlatId +
                ", plat=" + plat +
                ", portions=" + portions +
                '}';
    }
}
