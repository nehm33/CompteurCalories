package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Aliment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlimentRepository extends JpaRepository<Aliment, Long> {

    @Query(value = "SELECT a FROM Aliment a LEFT JOIN FETCH a.plat p LEFT JOIN FETCH a.codeBarre cb WHERE p IS NULL",
            countQuery = "SELECT COUNT(DISTINCT a.id) FROM Aliment a LEFT JOIN a.plat p WHERE p IS NULL")
    Page<Aliment> findAllByPlatIsNull(Pageable pageable);

    @Query(value = "SELECT a FROM Aliment a LEFT JOIN FETCH a.codeBarre cb LEFT JOIN FETCH a.plat p " +
            "WHERE (a.userId = :userId OR a.userId = 1) AND (UPPER(a.nom) LIKE :pattern OR cb.codeBarre LIKE :pattern) " +
            "AND p IS NULL",
            countQuery = "SELECT COUNT(DISTINCT a.id) FROM Aliment a LEFT JOIN a.codeBarre cb LEFT JOIN a.plat p " +
                    "WHERE (a.userId = :userId OR a.userId = 1) " +
                    "AND (UPPER(a.nom) LIKE :pattern OR cb.codeBarre LIKE :pattern) AND p IS NULL")
    Page<Aliment> findUserAlimentsByNomOrCodeBarre(Pageable pageable, String pattern, long userId);

    @Query(value = "SELECT a FROM Aliment a LEFT JOIN FETCH a.plat p LEFT JOIN FETCH a.codeBarre cb " +
            "WHERE (a.userId = :userId OR a.userId = 1) AND p IS NULL",
            countQuery = "SELECT COUNT(DISTINCT a.id) FROM Aliment a LEFT JOIN a.plat p " +
                    "WHERE (a.userId = :userId OR a.userId = 1) AND p IS NULL")
    Page<Aliment> findUserAliments(Pageable pageable, long userId);

    @Query("SELECT a FROM Aliment a LEFT JOIN FETCH a.plat p LEFT JOIN FETCH a.codeBarre cb WHERE a.nom = :nom " +
            "AND a.userId = :userId AND p IS NULL")
    Optional<Aliment> findByNomAndUserAndPlatIsNull(String nom, long userId);
}
