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
public class JournalPlatId implements Serializable {

    @Column(name = "nom")
    private String nomPlat;

    @Column(name = "dateJournal")
    private Date date;
}
