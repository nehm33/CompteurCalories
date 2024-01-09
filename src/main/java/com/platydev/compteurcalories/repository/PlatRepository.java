package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Plat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatRepository extends JpaRepository<Plat, String> {
}
