package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.repository.security.UserRepository;
import com.platydev.compteurcalories.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
