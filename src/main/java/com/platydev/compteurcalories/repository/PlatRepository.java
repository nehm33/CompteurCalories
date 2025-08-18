package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.dto.output.PlatWithoutRecetteDTO;
import com.platydev.compteurcalories.entity.Plat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatRepository extends JpaRepository<Plat, Long> {

    @Query(value = "SELECT " +
            "new com.platydev.compteurcalories.dto.output.PlatWithoutRecetteDTO(p.id, a.id, a.nom, a.calories) " +
            "FROM Plat p LEFT JOIN p.aliment a WHERE a.userId = ?1",
            countQuery = "SELECT COUNT(DISTINCT p.id) FROM Plat p LEFT JOIN p.aliment a WHERE a.userId = ?1")
    Page<PlatWithoutRecetteDTO> findByAlimentUserId(Pageable pageable, Long utilisateurId);

    @Query(value = "SELECT " +
            "new com.platydev.compteurcalories.dto.output.PlatWithoutRecetteDTO(p.id, a.id, a.nom, a.calories) " +
            "FROM Plat p LEFT JOIN p.aliment a WHERE a.userId = ?1 AND UPPER(a.nom) LIKE ?2",
            countQuery = "SELECT COUNT(DISTINCT p.id) FROM Plat p LEFT JOIN p.aliment a WHERE a.userId = ?1 " +
                    "AND UPPER(a.nom) LIKE ?2")
    Page<PlatWithoutRecetteDTO> findByAlimentUserIdAndAlimentNomContainingIgnoreCase(Pageable pageable, Long utilisateurId, String pattern);
}