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

    @Column(name = "nomPlat")
    private String nomPlat;

    @Column(name = "nomAliment")
    private String nomAliment;
}
