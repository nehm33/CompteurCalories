package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
