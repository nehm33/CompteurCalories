package com.platydev.compteurcalories.controller;

import com.platydev.compteurcalories.dto.input.LoginInputDTO;
import com.platydev.compteurcalories.dto.input.SigninInputDTO;
import com.platydev.compteurcalories.dto.output.LoginOutputDTO;
import com.platydev.compteurcalories.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginOutputDTO login(@RequestBody LoginInputDTO loginInputDTO) {
        return userService.authenticate(loginInputDTO);
    }

    @PostMapping("/signin")
    public void signIn(@RequestBody SigninInputDTO signinInputDTO) {
        userService.add(signinInputDTO);
    }
}
