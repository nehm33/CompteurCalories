package com.platydev.compteurcalories.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JournalAlimentId implements Serializable {

    @Column(name = "aliment_id")
    private long alimentId;

    @Basic
    @Column(name = "date_journal")
    private LocalDate date;
}
