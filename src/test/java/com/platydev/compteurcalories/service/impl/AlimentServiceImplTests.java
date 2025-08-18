package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.AlimentInputDTO;
import com.platydev.compteurcalories.dto.output.AlimentDTO;
import com.platydev.compteurcalories.dto.output.AlimentResponse;
import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.exception.ApiException;
import com.platydev.compteurcalories.exception.ForbiddenException;
import com.platydev.compteurcalories.infrastructure.AlimentMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.CodeBarreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AlimentServiceImplTests {

    @Mock
    private AlimentRepository alimentRepository;
    @Mock
    private CodeBarreRepository codeBarreRepository;
    @Mock
    private AlimentMapper alimentMapper;
    @InjectMocks
    private AlimentServiceImpl alimentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnAlimentResponse_whenAlimentsExist() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("nom")));
        List<Aliment> aliments = List.of(new Aliment());
        Page<Aliment> page = new PageImpl<>(aliments);
        List<AlimentDTO> alimentDTOS = List.of(mock(AlimentDTO.class));
        AlimentResponse response = mock(AlimentResponse.class);

        when(alimentRepository.findAllByPlatIsNull(any(Pageable.class))).thenReturn(page);
        when(alimentMapper.toDTO(any(Aliment.class))).thenReturn(alimentDTOS.getFirst());
        when(alimentMapper.toAlimentResponse(any(), any())).thenReturn(response);
        // Act
        AlimentResponse result = alimentService.getAll(pageable);

        // Assert
        assertEquals(response, result);
        verify(alimentRepository).findAllByPlatIsNull(any(Pageable.class));
        verify(alimentMapper).toDTO(any(Aliment.class));
    }

    @Test
    void getAll_shouldThrowApiException_whenNoAlimentsExist() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("nom")));
        Page<Aliment> emptyPage = new PageImpl<>(Collections.emptyList());
        when(alimentRepository.findAllByPlatIsNull(any(Pageable.class))).thenReturn(emptyPage);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () ->
                alimentService.getAll(pageable)
        );
        assertEquals("Aucun aliment créé jusqu'à maintenant", exception.getMessage());
        verify(alimentRepository).findAllByPlatIsNull(any(Pageable.class));
        verifyNoInteractions(alimentMapper);
    }

    @Test
    void find_shouldReturnAlimentResponse() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("nom")));
        String word = "test";
        Page<Aliment> page = new PageImpl<>(List.of(new Aliment()));
        List<AlimentDTO> alimentDTOS = List.of(mock(AlimentDTO.class));
        AlimentResponse response = mock(AlimentResponse.class);
        User user = User.builder().id(5L).roles(Set.of()).build();

        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        when(alimentRepository.findUserAlimentsByNomOrCodeBarre(any(Pageable.class), eq("%" + word.toUpperCase() + "%"), any(Long.class))).thenReturn(page);
        when(alimentMapper.toDTO(any(Aliment.class))).thenReturn(alimentDTOS.getFirst());
        when(alimentMapper.toAlimentResponse(any(), any())).thenReturn(response);

        // Act
        AlimentResponse result = alimentService.find(pageable, word);

        // Assert
        assertEquals(response, result);
    }

    @Test
    void add_shouldSaveAliment_whenNotExists() {
        // Arrange
        AlimentInputDTO alimentDTO = mock(AlimentInputDTO.class);
        when(alimentDTO.nom()).thenReturn("test");
        when(alimentRepository.findByNomAndUserAndPlatIsNull(anyString(), eq(5L))).thenReturn(Optional.empty());
        User user = User.builder().id(5L).roles(Set.of()).build();
        Aliment aliment = new Aliment();
        when(alimentMapper.toEntity(alimentDTO)).thenReturn(aliment);
        
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        // Act
        alimentService.add(alimentDTO);

        // Assert
        verify(alimentRepository).save(aliment);
    }

    @Test
    void add_shouldThrowApiException_whenAlimentExists() {
        // Arrange
        AlimentInputDTO alimentDTO = mock(AlimentInputDTO.class);
        when(alimentDTO.nom()).thenReturn("test");
        when(alimentRepository.findByNomAndUserAndPlatIsNull(anyString(), eq(5L))).thenReturn(Optional.of(new Aliment()));
        User user = User.builder().id(5L).roles(Set.of()).build();
        
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> alimentService.add(alimentDTO));
        assertEquals("Cet aliment existe déjà", exception.getMessage());
        verify(alimentRepository, never()).save(any());
    }

    @Test
    void update_shouldUpdateAliment_whenExistsAndOwned() {
        // Arrange
        long alimentId = 1L;
        AlimentInputDTO alimentDTO = mock(AlimentInputDTO.class);
        User user = User.builder().id(5L).roles(Set.of()).build();
        Aliment aliment = new Aliment();
        aliment.setUserId(user.getId());
        
        when(alimentRepository.findById(alimentId)).thenReturn(Optional.of(aliment));
        when(codeBarreRepository.findByAlimentId(alimentId)).thenReturn(Optional.empty());
        when(alimentMapper.toEntity(alimentDTO)).thenReturn(aliment);
        when(alimentRepository.save(any(Aliment.class))).thenReturn(aliment);
        when(alimentMapper.toDTO(any(Aliment.class))).thenReturn(mock(AlimentDTO.class));
        
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        // Act
        alimentService.update(alimentId, alimentDTO);

        // Assert
        verify(alimentRepository).save(any(Aliment.class));
    }

    @Test
    void update_shouldThrowForbiddenException_whenNotOwned() {
        // Arrange
        long alimentId = 1L;
        AlimentInputDTO alimentDTO = mock(AlimentInputDTO.class);
        User user = User.builder().id(5L).roles(Set.of()).build();
        User otherUser = User.builder().id(3L).roles(Set.of()).build();
        Aliment aliment = new Aliment();
        aliment.setUserId(otherUser.getId());
        
        when(alimentRepository.findById(alimentId)).thenReturn(Optional.of(aliment));
        
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> alimentService.update(alimentId, alimentDTO));
        assertEquals("Cet aliment ne vous appartient pas", exception.getMessage());
        verify(alimentRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteAliment_whenOwned() {
        // Arrange
        long alimentId = 1L;
        User user = User.builder().id(5L).roles(Set.of()).build();
        Aliment aliment = new Aliment();
        aliment.setUserId(user.getId());
        
        when(alimentRepository.findById(alimentId)).thenReturn(Optional.of(aliment));
        
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        // Act
        alimentService.delete(alimentId);

        // Assert
        verify(alimentRepository).deleteById(alimentId);
    }

    @Test
    void delete_shouldThrowForbiddenException_whenNotOwned() {
        // Arrange
        long alimentId = 1L;
        User user = User.builder().id(5L).roles(Set.of()).build();
        User otherUser = User.builder().id(3L).roles(Set.of()).build();
        Aliment aliment = new Aliment();
        aliment.setUserId(otherUser.getId());
        
        when(alimentRepository.findById(alimentId)).thenReturn(Optional.of(aliment));
        
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> alimentService.delete(alimentId));
        assertEquals("Cet aliment ne vous appartient pas", exception.getMessage());
        verify(alimentRepository, never()).deleteById(anyLong());
    }

    @Test
    void existsByName_shouldReturnTrue_whenAlimentExists() {
        // Arrange
        String alimentName = "test";
        User user = User.builder().id(5L).roles(Set.of()).build();
        when(alimentRepository.findByNomAndUserAndPlatIsNull(eq(alimentName), eq(user.getId()))).thenReturn(Optional.of(new Aliment()));
        
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        // Act
        boolean exists = alimentService.existsByName(alimentName);

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByName_shouldReturnFalse_whenAlimentDoesNotExist() {
        // Arrange
        String alimentName = "test";
        User user = User.builder().id(5L).roles(Set.of()).build();
        when(alimentRepository.findByNomAndUserAndPlatIsNull(eq(alimentName), eq(user.getId()))).thenReturn(Optional.empty());
        
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        // Act
        boolean exists = alimentService.existsByName(alimentName);

        // Assert
        assertFalse(exists);
    }
} 