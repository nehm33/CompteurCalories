package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Plat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatRepository extends JpaRepository<Plat, Long> {

    Page<Plat> findByAlimentUserId(Long utilisateurId, Pageable pageable);

    Page<Plat> findByAlimentUserIdAndAlimentNomContainingIgnoreCase(Long utilisateurId, String nom, Pageable pageable);
}