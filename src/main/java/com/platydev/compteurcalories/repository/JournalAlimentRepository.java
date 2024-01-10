package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.JournalAliment;
import com.platydev.compteurcalories.entity.JournalAlimentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalAlimentRepository extends JpaRepository<JournalAliment, JournalAlimentId> {
}
