package com.platydev.compteurcalories.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class JournalPlatId implements Serializable {

    @Column(name = "nom_plat")
    private String nomPlat;

    @Basic
    @Column(name = "date_journal")
    private Date date;
}
