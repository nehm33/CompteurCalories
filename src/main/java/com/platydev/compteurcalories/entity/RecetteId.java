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

    @Column(name = "plat_id")
    private long platId;

    @Column(name = "aliment_id")
    private long alimentId;
}
