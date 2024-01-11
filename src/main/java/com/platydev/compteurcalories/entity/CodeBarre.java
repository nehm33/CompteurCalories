package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "code_barre")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CodeBarre {

    @Id
    @Column(name = "code_barre")
    private String codeBarre;

    private String marque;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nom_aliment")
    private Aliment aliment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodeBarre codeBarre1 = (CodeBarre) o;

        if (!codeBarre.equals(codeBarre1.codeBarre)) return false;
        return marque.equals(codeBarre1.marque);
    }

    @Override
    public int hashCode() {
        int result = codeBarre.hashCode();
        result = 31 * result + marque.hashCode();
        return result;
    }
}
