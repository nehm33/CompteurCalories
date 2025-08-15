package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlimentRepository extends JpaRepository<Aliment, Long> {

    Page<Aliment> findAllByPlatIsNull(Pageable pageable);

    @Query("SELECT a FROM Aliment a LEFT JOIN a.codeBarre cb LEFT JOIN a.plat p WHERE (a.user = :user OR a.user.id = 1) AND (a.nom LIKE :search OR cb.codeBarre LIKE :search) AND p IS NULL")
    Page<Aliment> findUserAlimentsByNomOrCodeBarre(Pageable pageable, String search, User user);

    @Query("SELECT a FROM Aliment a LEFT JOIN a.plat p WHERE (a.user = :user OR a.user.id = 1) AND p IS NULL")
    Page<Aliment> findUserAliments(Pageable pageable, User user);

    Optional<Aliment> findByNomAndUserAndPlatIsNull(String nom, User user);
}
