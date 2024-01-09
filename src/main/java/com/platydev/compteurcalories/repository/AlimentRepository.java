package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Aliment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlimentRepository extends JpaRepository<Aliment, String> {
}
