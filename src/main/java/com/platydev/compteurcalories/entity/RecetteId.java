package com.platydev.compteurcalories.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RecetteId implements Serializable {

    @Column(name = "nom_plat")
    private String nomPlat;

    @Column(name = "nom_aliment")
    private String nomAliment;
}
