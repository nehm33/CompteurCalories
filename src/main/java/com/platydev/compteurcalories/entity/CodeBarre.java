package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "code_barre")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CodeBarre {

    @Id
    @Column(name = "code_barre")
    @NotBlank
    @Size(min = 13, max = 13)
    private String codeBarre;

    @NotBlank
    @Size(max = 30)
    private String marque;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aliment_id")
    private Aliment aliment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeBarre codeBarre1)) return false;

        if (!getCodeBarre().equals(codeBarre1.getCodeBarre())) return false;
        if (!getMarque().equals(codeBarre1.getMarque())) return false;
        return getAliment().getId() == codeBarre1.getAliment().getId();
    }

    @Override
    public int hashCode() {
        int result = getCodeBarre().hashCode();
        result = 31 * result + getMarque().hashCode();
        result = 31 * result + (int) getAliment().getId();
        return result;
    }

    @Override
    public String toString() {
        return "CodeBarre{" +
                "codeBarre='" + codeBarre + '\'' +
                ", marque='" + marque + '\'' +
                ", aliment=" + aliment.getId() +
                '}';
    }
}
