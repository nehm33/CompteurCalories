package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.LoginInput;
import com.platydev.compteurcalories.dto.input.SigninInput;
import com.platydev.compteurcalories.dto.output.LoginOutput;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.infrastructure.UserMapper;
import com.platydev.compteurcalories.repository.security.UserRepository;
import com.platydev.compteurcalories.security.JwtUtils;
import com.platydev.compteurcalories.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtils jwtUtils, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public LoginOutput authenticate(LoginInput loginInput) {
        User user = userRepository.findByUsername(loginInput.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginInput.password(), user.getPassword())) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        String token = jwtUtils.generateTokenFromUsername(user);
        return new LoginOutput(token, jwtUtils.getJwtExpiration());
    }

    @Override
    @Transactional
    public void add(SigninInput signinInput) {
        User user = userMapper.toUser(signinInput);
        user.setPassword(passwordEncoder.encode(signinInput.password()));
        userRepository.save(user);
    }
}
