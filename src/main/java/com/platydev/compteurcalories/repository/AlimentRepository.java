package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlimentRepository extends JpaRepository<Aliment, Long> {

    Page<Aliment> findByNomLikeAndByUserOrByUser(String pattern, User user1, User user2, Pageable pageable);
    Optional<Aliment> findByNomAndByUser(String nom, User user);
    Page<Aliment> findByUserOrUser(User user1, User user2, Pageable pageable);
}
