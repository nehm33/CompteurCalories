package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.CodeBarre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeBarreRepository extends JpaRepository<CodeBarre, String> {
}
