package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Recette;
import com.platydev.compteurcalories.entity.RecetteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetteRepository extends JpaRepository<Recette, RecetteId> {
}
