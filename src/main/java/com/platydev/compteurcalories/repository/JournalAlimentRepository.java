package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.JournalAliment;
import com.platydev.compteurcalories.entity.JournalAlimentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalAlimentRepository extends JpaRepository<JournalAliment, JournalAlimentId> {
}
