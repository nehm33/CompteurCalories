package com.platydev.compteurcalories.service;

import com.platydev.compteurcalories.dto.input.LoginInput;
import com.platydev.compteurcalories.dto.input.SigninInput;
import com.platydev.compteurcalories.dto.output.LoginOutput;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    LoginOutput authenticate(LoginInput loginInput);
    void add(SigninInput signinInput);
}
