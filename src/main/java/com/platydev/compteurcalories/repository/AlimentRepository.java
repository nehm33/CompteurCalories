package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Aliment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimentRepository extends JpaRepository<Aliment, Long> {
}
