package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.LoginInput;
import com.platydev.compteurcalories.dto.input.SigninInput;
import com.platydev.compteurcalories.dto.output.LoginOutput;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.infrastructure.UserMapper;
import com.platydev.compteurcalories.repository.security.UserRepository;
import com.platydev.compteurcalories.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_shouldReturnUser_whenUserExists() {
        // Arrange
        String username = "testuser";
        User user = mock(User.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userService.loadUserByUsername(username);

        // Assert
        assertEquals(user, result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
        // Arrange
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername(username)
        );
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void authenticate_shouldReturnLoginOutput_whenCredentialsAreValid() {
        // Arrange
        String username = "testuser";
        String password = "password";
        String hashedPassword = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
        String token = "jwt-token";
        int expiration = 7200;

        LoginInput loginInput = new LoginInput(username, password);
        User user = User.builder().username(username).password(hashedPassword).build();
        LoginOutput expectedOutput = new LoginOutput(token, expiration);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtUtils.generateTokenFromUsername(user)).thenReturn(token);
        when(jwtUtils.getJwtExpiration()).thenReturn(expiration);

        // Act
        LoginOutput result = userService.authenticate(loginInput);

        // Assert
        assertEquals(expectedOutput, result);
        verify(userRepository).findByUsername(username);
        verify(jwtUtils).generateTokenFromUsername(user);
        verify(jwtUtils).getJwtExpiration();
    }

    @Test
    void authenticate_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
        // Arrange
        String username = "nonexistent";
        String password = "password";
        LoginInput loginInput = new LoginInput(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userService.authenticate(loginInput)
        );
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByUsername(username);
        verifyNoInteractions(jwtUtils);
    }

    @Test
    void authenticate_shouldThrowUsernameNotFoundException_whenPasswordIsIncorrect() {
        // Arrange
        String username = "testuser";
        String password = "wrongpassword";
        String hashedPassword = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
        LoginInput loginInput = new LoginInput(username, password);
        User user = User.builder().username(username).password(hashedPassword).build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userService.authenticate(loginInput)
        );
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository).findByUsername(username);
        verifyNoInteractions(jwtUtils);
    }

    @Test
    void add_shouldSaveUserWithHashedPassword_whenSigninInputIsValid() {
        // Arrange
        String username = "newuser";
        String password = "newpassword";
        String hashedPassword = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
        SigninInput signinInput = new SigninInput(username, password);
        User user = User.builder().username(username).password(password).build();

        when(userMapper.toUser(signinInput)).thenReturn(user);

        // Act
        userService.add(signinInput);

        // Assert
        verify(userMapper).toUser(signinInput);
        verify(userRepository).save(user);
    }
}