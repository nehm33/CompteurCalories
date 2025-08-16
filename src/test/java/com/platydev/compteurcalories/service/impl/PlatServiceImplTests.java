package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.PlatInputDTO;
import com.platydev.compteurcalories.dto.input.RecetteInputDTO;
import com.platydev.compteurcalories.dto.output.PlatDTO;
import com.platydev.compteurcalories.dto.output.PlatResponse;
import com.platydev.compteurcalories.dto.output.PlatWithoutRecetteDTO;
import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.Plat;
import com.platydev.compteurcalories.entity.Recette;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.exception.ApiException;
import com.platydev.compteurcalories.exception.ForbiddenException;
import com.platydev.compteurcalories.exception.NotFoundException;
import com.platydev.compteurcalories.infrastructure.PlatMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.PlatRepository;
import com.platydev.compteurcalories.repository.RecetteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PlatServiceImplTests {

    @Mock
    private PlatRepository platRepository;
    @Mock
    private AlimentRepository alimentRepository;
    @Mock
    private RecetteRepository recetteRepository;
    @Mock
    private PlatMapper platMapper;

    @InjectMocks
    private PlatServiceImpl platService;

    private User currentUser;
    private User otherUser;
    private Aliment alimentIngredient;
    private Aliment alimentPlat;
    private Plat plat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup users
        currentUser = new User();
        currentUser.setId(1L);

        otherUser = new User();
        otherUser.setId(2L);

        // Setup aliment ingredient
        alimentIngredient = new Aliment();
        alimentIngredient.setId(10L);
        alimentIngredient.setNom("Tomate");
        alimentIngredient.setUser(currentUser);
        alimentIngredient.setCalories(20.0f);
        alimentIngredient.setProteines(1.0f);

        // Setup aliment plat
        alimentPlat = new Aliment();
        alimentPlat.setId(20L);
        alimentPlat.setNom("Salade de tomates");
        alimentPlat.setUser(currentUser);
        alimentPlat.setUnite("portion");

        // Setup plat
        plat = new Plat();
        plat.setId(30L);
        plat.setNbPortions(2.0f);
        plat.setAliment(alimentPlat);
        alimentPlat.setPlat(plat);

        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void find_shouldReturnPlatResponse_withoutSearch() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Plat> plats = List.of(plat);
        Page<Plat> page = new PageImpl<>(plats);
        List<PlatWithoutRecetteDTO> platDTOs = List.of(mock(PlatWithoutRecetteDTO.class));
        PlatResponse response = mock(PlatResponse.class);

        when(platRepository.findByAlimentUserId(pageable, currentUser.getId())).thenReturn(page);
        when(platMapper.toPlatWithoutRecetteDTOList(plats)).thenReturn(platDTOs);
        when(platMapper.toPlatResponse(platDTOs, page)).thenReturn(response);

        // Act
        PlatResponse result = platService.find(pageable, null);

        // Assert
        assertEquals(response, result);
        verify(platRepository).findByAlimentUserId(pageable, currentUser.getId());
        verify(platMapper).toPlatWithoutRecetteDTOList(plats);
        verify(platMapper).toPlatResponse(platDTOs, page);
    }

    @Test
    void find_shouldReturnPlatResponse_withSearch() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String search = "salade";
        List<Plat> plats = List.of(plat);
        Page<Plat> page = new PageImpl<>(plats);
        List<PlatWithoutRecetteDTO> platDTOs = List.of(mock(PlatWithoutRecetteDTO.class));
        PlatResponse response = mock(PlatResponse.class);

        when(platRepository.findByAlimentUserIdAndAlimentNomContainingIgnoreCase(
                pageable, currentUser.getId(), "%" + search.toUpperCase() + "%")).thenReturn(page);
        when(platMapper.toPlatWithoutRecetteDTOList(plats)).thenReturn(platDTOs);
        when(platMapper.toPlatResponse(platDTOs, page)).thenReturn(response);

        // Act
        PlatResponse result = platService.find(pageable, search);

        // Assert
        assertEquals(response, result);
        verify(platRepository).findByAlimentUserIdAndAlimentNomContainingIgnoreCase(
                pageable, currentUser.getId(), "%" + search.toUpperCase() + "%");
    }

    @Test
    void find_shouldHandleEmptySearch() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String search = "   ";
        List<Plat> plats = List.of(plat);
        Page<Plat> page = new PageImpl<>(plats);

        when(platRepository.findByAlimentUserId(pageable, currentUser.getId())).thenReturn(page);
        when(platMapper.toPlatWithoutRecetteDTOList(anyList())).thenReturn(List.of());
        when(platMapper.toPlatResponse(any(), any())).thenReturn(mock(PlatResponse.class));

        // Act
        platService.find(pageable, search);

        // Assert
        verify(platRepository).findByAlimentUserId(pageable, currentUser.getId());
        verify(platRepository, never()).findByAlimentUserIdAndAlimentNomContainingIgnoreCase(any(), anyLong(), anyString());
    }

    @Test
    void findById_shouldReturnPlatDTO_whenPlatExistsAndOwned() {
        // Arrange
        long platId = 30L;
        PlatDTO expectedDTO = mock(PlatDTO.class);

        when(platRepository.findById(platId)).thenReturn(Optional.of(plat));
        when(platMapper.toDTO(plat)).thenReturn(expectedDTO);

        // Act
        PlatDTO result = platService.findById(platId);

        // Assert
        assertEquals(expectedDTO, result);
        verify(platRepository).findById(platId);
        verify(platMapper).toDTO(plat);
    }

    @Test
    void findById_shouldThrowNotFoundException_whenPlatDoesNotExist() {
        // Arrange
        long platId = 999L;
        when(platRepository.findById(platId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> platService.findById(platId));
        assertEquals("Plat non trouvé", exception.getMessage());
        verify(platRepository).findById(platId);
        verifyNoInteractions(platMapper);
    }

    @Test
    void findById_shouldThrowForbiddenException_whenPlatNotOwned() {
        // Arrange
        long platId = 30L;
        plat.getAliment().setUser(otherUser);
        when(platRepository.findById(platId)).thenReturn(Optional.of(plat));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> platService.findById(platId));
        assertEquals("Vous n'êtes pas autorisé à accéder à ce plat", exception.getMessage());
        verify(platRepository).findById(platId);
        verifyNoInteractions(platMapper);
    }

    @Test
    void add_shouldCreatePlatSuccessfully() {
        // Arrange
        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 200.0f);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Salade de tomates", List.of(recetteDTO));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        platService.add(platDTO);

        // Assert
        verify(alimentRepository).findById(10L);
        verify(alimentRepository).save(any(Aliment.class));
        verify(recetteRepository).saveAll(anyList());
    }

    @Test
    void add_shouldThrowNotFoundException_whenIngredientNotFound() {
        // Arrange
        RecetteInputDTO recetteDTO = new RecetteInputDTO(999L, 200.0f);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Salade", List.of(recetteDTO));

        when(alimentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> platService.add(platDTO));
        assertEquals("Aliment avec l'ID 999 non trouvé", exception.getMessage());
        verify(alimentRepository, never()).save(any());
    }

    @Test
    void add_shouldThrowForbiddenException_whenIngredientNotAccessible() {
        // Arrange
        alimentIngredient.setUser(otherUser);
        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 200.0f);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Salade", List.of(recetteDTO));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> platService.add(platDTO));
        assertEquals("Vous n'avez pas accès à l'aliment avec l'ID 10", exception.getMessage());
        verify(alimentRepository, never()).save(any());
    }

    @Test
    void add_shouldAllowSystemAliment() {
        // Arrange
        User systemUser = new User();
        systemUser.setId(1L);
        alimentIngredient.setUser(systemUser);

        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 200.0f);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Salade", List.of(recetteDTO));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        platService.add(platDTO);

        // Assert
        verify(alimentRepository).save(any(Aliment.class));
    }

    @Test
    void add_shouldThrowApiException_whenUsingPlatAsIngredient() {
        // Arrange
        alimentIngredient.setPlat(new Plat()); // Fait de cet aliment un plat
        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 200.0f);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Salade", List.of(recetteDTO));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class,
                () -> platService.add(platDTO));
        assertEquals("Impossible d'utiliser un plat comme ingrédient d'un autre plat (ID: 10)",
                exception.getMessage());
    }

    @Test
    void add_shouldAggregateRecettes_whenSameIngredientMultipleTimes() {
        // Arrange
        RecetteInputDTO recette1 = new RecetteInputDTO(10L, 100.0f);
        RecetteInputDTO recette2 = new RecetteInputDTO(10L, 150.0f);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Salade", List.of(recette1, recette2));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        platService.add(platDTO);

        // Assert
        verify(recetteRepository).saveAll(argThat(recettes -> {
            List<Recette> recettesList = (List<Recette>) recettes;
            return recettesList.size() == 1 &&
                    recettesList.getFirst().getQuantite() == 250.0f;
        }));
    }

    @Test
    void update_shouldUpdatePlatDataOnly_whenRecettesIdentical() {
        // Arrange
        long platId = 30L;
        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 200.0f);
        PlatInputDTO platDTO = new PlatInputDTO(3.0f, "Nouveau nom", List.of(recetteDTO));

        Recette existingRecette = new Recette();
        existingRecette.setAliment(alimentIngredient);
        existingRecette.setQuantite(200.0f);

        when(platRepository.findById(platId)).thenReturn(Optional.of(plat));
        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(recetteRepository.findAllByRecetteId_PlatId(platId)).thenReturn(List.of(existingRecette));
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);

        // Act
        platService.update(platId, platDTO);

        // Assert
        verify(platRepository).findById(platId);
        verify(alimentRepository).save(any(Aliment.class));
        verify(recetteRepository, never()).deleteAll(any());
        verify(recetteRepository, never()).saveAll(any());
    }

    @Test
    void update_shouldUpdateWithNewRecettes_whenRecettesDifferent() {
        // Arrange
        long platId = 30L;
        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 300.0f); // Quantité différente
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Nouveau nom", List.of(recetteDTO));

        Recette existingRecette = new Recette();
        existingRecette.setAliment(alimentIngredient);
        existingRecette.setQuantite(200.0f);

        when(platRepository.findById(platId)).thenReturn(Optional.of(plat));
        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(recetteRepository.findAllByRecetteId_PlatId(platId)).thenReturn(List.of(existingRecette));
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        platService.update(platId, platDTO);

        // Assert
        verify(recetteRepository).deleteAll(List.of(existingRecette));
        verify(recetteRepository).saveAll(anyList());
        verify(alimentRepository).save(any(Aliment.class));
    }

    @Test
    void update_shouldThrowNotFoundException_whenPlatDoesNotExist() {
        // Arrange
        long platId = 999L;
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Test", List.of());

        when(platRepository.findById(platId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> platService.update(platId, platDTO));
        assertEquals("Plat non trouvé", exception.getMessage());
    }

    @Test
    void update_shouldThrowForbiddenException_whenPlatNotOwned() {
        // Arrange
        long platId = 30L;
        plat.getAliment().setUser(otherUser);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Test", List.of());

        when(platRepository.findById(platId)).thenReturn(Optional.of(plat));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> platService.update(platId, platDTO));
        assertEquals("Vous n'êtes pas autorisé à accéder à ce plat", exception.getMessage());
    }

    @Test
    void delete_shouldDeletePlat_whenExistsAndOwned() {
        // Arrange
        long platId = 30L;
        when(platRepository.findById(platId)).thenReturn(Optional.of(plat));

        // Act
        platService.delete(platId);

        // Assert
        verify(platRepository).findById(platId);
        verify(alimentRepository).deleteById(alimentPlat.getId());
    }

    @Test
    void delete_shouldThrowNotFoundException_whenPlatDoesNotExist() {
        // Arrange
        long platId = 999L;
        when(platRepository.findById(platId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> platService.delete(platId));
        assertEquals("Plat non trouvé", exception.getMessage());
        verify(alimentRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_shouldThrowForbiddenException_whenPlatNotOwned() {
        // Arrange
        long platId = 30L;
        plat.getAliment().setUser(otherUser);
        when(platRepository.findById(platId)).thenReturn(Optional.of(plat));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> platService.delete(platId));
        assertEquals("Vous n'êtes pas autorisé à accéder à ce plat", exception.getMessage());
        verify(alimentRepository, never()).deleteById(anyLong());
    }

    @Test
    void validateAndAggregateRecettes_shouldHandleNullValues() {
        // Arrange
        alimentIngredient.setCalories(null);
        alimentIngredient.setProteines(null);

        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 200.0f);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Test", List.of(recetteDTO));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act & Assert - Ne doit pas lever d'exception
        assertDoesNotThrow(() -> platService.add(platDTO));
        verify(alimentRepository).save(any(Aliment.class));
    }

    @Test
    void safeDivide_shouldHandleZeroDivision() {
        // Ce test vérifie le comportement de la division par zéro dans les calculs nutritionnels
        // Arrange
        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 200.0f);
        PlatInputDTO platDTO = new PlatInputDTO(0.0f, "Test", List.of(recetteDTO)); // 0 portions

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act & Assert - Ne doit pas lever d'exception
        assertDoesNotThrow(() -> platService.add(platDTO));
    }
}