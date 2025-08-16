package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Plat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatRepository extends JpaRepository<Plat, Long> {

    @Query(value = "SELECT p FROM Plat p LEFT JOIN FETCH p.aliment a LEFT JOIN FETCH a.codeBarre " +
            "LEFT JOIN FETCH a.user u LEFT JOIN FETCH u.roles WHERE u.id = ?1",
            countQuery = "SELECT COUNT(DISTINCT p.id) FROM Plat p LEFT JOIN p.aliment a " +
                    "LEFT JOIN a.user u WHERE u.id = ?1")
    Page<Plat> findByAlimentUserId(Pageable pageable, Long utilisateurId);

    @Query(value = "SELECT p FROM Plat p LEFT JOIN FETCH p.aliment a LEFT JOIN FETCH a.codeBarre " +
            "LEFT JOIN FETCH a.user u LEFT JOIN FETCH u.roles WHERE u.id = ?1 AND UPPER(a.nom) LIKE ?2",
            countQuery = "SELECT COUNT(DISTINCT p.id) FROM Plat p LEFT JOIN p.aliment a " +
                    "LEFT JOIN a.user u WHERE u.id = ?1 AND UPPER(a.nom) LIKE ?2")
    Page<Plat> findByAlimentUserIdAndAlimentNomContainingIgnoreCase(Pageable pageable, Long utilisateurId, String pattern);
}