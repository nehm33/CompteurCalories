package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.JournalAliment;
import com.platydev.compteurcalories.entity.JournalAlimentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JournalAlimentRepository extends JpaRepository<JournalAliment, JournalAlimentId> {

    List<JournalAliment> findAllByJournalAlimentId_Date(LocalDate date);

    List<JournalAliment> findByJournalAlimentId_UserIdAndJournalAlimentId_Date(Long userId, LocalDate date);

    List<JournalAliment> findByJournalAlimentId_UserIdAndJournalAlimentId_DateAndJournalAlimentId_Repas(
            Long userId, LocalDate date, int repas);
}