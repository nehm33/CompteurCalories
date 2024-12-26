package com.platydev.compteurcalories.service;

import com.platydev.compteurcalories.dto.input.LoginInputDTO;
import com.platydev.compteurcalories.dto.input.SigninInputDTO;
import com.platydev.compteurcalories.dto.output.LoginOutputDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    LoginOutputDTO authenticate(LoginInputDTO loginInputDTO);
    void add(SigninInputDTO signinInputDTO);
}
