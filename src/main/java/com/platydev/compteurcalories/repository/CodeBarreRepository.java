package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.CodeBarre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeBarreRepository extends JpaRepository<CodeBarre, String> {

    @Query("SELECT c FROM CodeBarre c LEFT JOIN FETCH c.aliment a WHERE a.id = ?1")
    Optional<CodeBarre> findByAlimentId(long alimentId);
}
