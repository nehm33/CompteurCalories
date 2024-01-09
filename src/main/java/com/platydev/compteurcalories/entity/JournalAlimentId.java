package com.platydev.compteurcalories.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class JournalAlimentId implements Serializable {

    @Column(name = "nom")
    private String nomAliment;

    @Column(name = "dateJournal")
    private Date date;
}
