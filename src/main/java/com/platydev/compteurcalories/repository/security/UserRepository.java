package com.platydev.compteurcalories.repository.security;

import com.platydev.compteurcalories.entity.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByNom(String nom);

}
