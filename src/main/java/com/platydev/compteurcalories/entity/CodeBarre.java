package com.platydev.compteurcalories.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "code_barre")
@NoArgsConstructor
@Getter
@Setter
public class CodeBarre {

    @Id
    @Column(name = "code_barre")
    private String codeBarre;

    private String marque;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nom")
    private String aliment;
}
