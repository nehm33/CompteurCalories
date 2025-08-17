package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Journal;
import com.platydev.compteurcalories.entity.JournalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends JpaRepository<Journal, JournalId> {
}
