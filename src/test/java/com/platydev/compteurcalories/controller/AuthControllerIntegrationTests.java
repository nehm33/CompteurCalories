package com.platydev.compteurcalories.controller;

import com.platydev.compteurcalories.dto.input.LoginInput;
import com.platydev.compteurcalories.dto.input.SigninInput;
import com.platydev.compteurcalories.dto.output.LoginOutput;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.repository.security.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthControllerIntegrationTests {

    private final TestRestTemplate restTemplate;

    private final UserRepository userRepository;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    public AuthControllerIntegrationTests(TestRestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/auth";
    }

    @Test
    void signin_shouldCreateUser_whenValidInput() {
        // Arrange
        SigninInput signinInput = new SigninInput("test", "test");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SigninInput> request = new HttpEntity<>(signinInput, headers);

        // Debug: Check if roles exist in database
        System.out.println("Testing signin with user: " + signinInput.username());

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/signin",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        // Verify user was created in database
        User savedUser = userRepository.findByUsername("test").orElse(null);
        assertNotNull(savedUser);
        assertEquals("test", savedUser.getUsername());
        
        // Verify user has roles
        assertNotNull(savedUser.getRoles());
        assertFalse(savedUser.getRoles().isEmpty());
    }

    @Test
    void login_shouldReturnToken_whenValidCredentials() {
        // Arrange - User is already created in SQL script
        LoginInput loginInput = new LoginInput("testuser", "password123");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginInput> request = new HttpEntity<>(loginInput, headers);

        // Act
        ResponseEntity<LoginOutput> response = restTemplate.postForEntity(
                baseUrl + "/login",
                request,
                LoginOutput.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().token());
        assertTrue(response.getBody().expiresIn() > 0);
    }

    @Test
    void login_shouldReturnError_whenInvalidCredentials() {
        // Arrange
        LoginInput loginInput = new LoginInput("nonexistent", "wrongpassword");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginInput> request = new HttpEntity<>(loginInput, headers);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/login",
                request,
                String.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void signin_shouldReturnError_whenUsernameAlreadyExists() {
        // Arrange - User "testuser" already exists in SQL script
        SigninInput signinInput = new SigninInput("testuser", "password123");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SigninInput> request = new HttpEntity<>(signinInput, headers);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/signin",
                request,
                String.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
} 