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
@Data
public class JournalAlimentId implements Serializable {

    @Column(name = "aliment_id")
    private long alimentId;

    @Column(name = "user_id")
    private long userId;

    @Basic
    @Column(name = "date_journal")
    private LocalDate date;
}
