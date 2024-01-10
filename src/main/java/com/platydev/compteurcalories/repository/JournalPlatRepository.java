package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.JournalPlat;
import com.platydev.compteurcalories.entity.JournalPlatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalPlatRepository extends JpaRepository<JournalPlat, JournalPlatId> {
}
