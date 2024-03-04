package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.CodeBarre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeBarreRepository extends JpaRepository<CodeBarre, String> {

    List<CodeBarre> findByCodeBarreLike(String pattern);
}
