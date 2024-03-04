package com.platydev.compteurcalories.repository;

import com.platydev.compteurcalories.entity.Aliment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlimentRepository extends JpaRepository<Aliment, Long> {

    List<Aliment> findByNomLike(String pattern);
}
