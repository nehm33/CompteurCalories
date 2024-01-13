package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

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
    @MapsId("platId")
    @JoinColumn(name = "plat_id")
    private Plat plat;

    private double portions;

    public JournalPlat(Date date, Plat plat, double portions) {
        this.journalPlatId = new JournalPlatId(plat.getId(), date);
        this.plat = plat;
        this.portions = portions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalPlat that)) return false;

        if (Double.compare(getPortions(), that.getPortions()) != 0) return false;
        if (!getJournalPlatId().equals(that.getJournalPlatId())) return false;
        return getPlat().equals(that.getPlat());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getJournalPlatId().hashCode();
        result = 31 * result + getPlat().hashCode();
        temp = Double.doubleToLongBits(getPortions());
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
