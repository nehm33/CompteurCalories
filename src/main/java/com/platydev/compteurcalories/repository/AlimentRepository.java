package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlimentRepository extends JpaRepository<Aliment, Long> {

    @Query("SELECT a FROM Aliment a LEFT JOIN a.codeBarre cb WHERE (a.user = :user OR a.user.id = 1) AND (a.nom LIKE :pattern OR cb.codeBarre LIKE :pattern)")
    Page<Aliment> findUserAlimentsByNomOrCodeBarre(Pageable pageable, @Param("pattern") String pattern, @Param("user") User user);

    @Query("SELECT a FROM Aliment a WHERE a.user = :user OR a.user.id = 1")
    Page<Aliment> findUserAliments(Pageable pageable, @Param("user") User user);

    Optional<Aliment> findByNomAndUser(String nom, User user);
}
