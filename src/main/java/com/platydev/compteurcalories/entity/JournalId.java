package com.platydev.compteurcalories.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JournalId implements Serializable {

    @Column(name = "user_id")
    private long userId;

    @Basic
    @Column(name = "date_journal")
    private LocalDate date;

    @Basic
    @Column(name = "repas")
    private int repas;
}
