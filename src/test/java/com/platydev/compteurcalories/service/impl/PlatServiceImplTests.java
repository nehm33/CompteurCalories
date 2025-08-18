package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.PlatInputDTO;
import com.platydev.compteurcalories.dto.input.RecetteInputDTO;
import com.platydev.compteurcalories.dto.output.NutrientTotals;
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
    @Mock
    private NutritionalCalculator nutritionalCalculator;

    @InjectMocks
    private PlatServiceImpl platService;

    private User currentUser;
    private User otherUser;
    private Aliment alimentIngredient;
    private Aliment alimentPlat;
    private Plat plat;
    private NutrientTotals mockTotals;

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
        alimentIngredient.setUserId(currentUser.getId());
        alimentIngredient.setUnite("g");
        alimentIngredient.setCalories(20.0f);
        alimentIngredient.setProteines(1.0f);

        // Setup aliment plat
        alimentPlat = new Aliment();
        alimentPlat.setId(20L);
        alimentPlat.setNom("Salade de tomates");
        alimentPlat.setUserId(currentUser.getId());
        alimentPlat.setUnite("portion");

        // Setup plat
        plat = new Plat();
        plat.setId(30L);
        plat.setNbPortions(2.0f);
        plat.setAliment(alimentPlat);
        alimentPlat.setPlat(plat);

        // Setup mock totals
        mockTotals = mock(NutrientTotals.class);
        when(mockTotals.getCalories()).thenReturn(40.0f);
        when(mockTotals.getProteines()).thenReturn(2.0f);

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
        List<PlatWithoutRecetteDTO> platDTOs = List.of(mock(PlatWithoutRecetteDTO.class));
        Page<PlatWithoutRecetteDTO> page = new PageImpl<>(platDTOs);
        PlatResponse response = mock(PlatResponse.class);

        when(platRepository.findByAlimentUserId(pageable, currentUser.getId())).thenReturn(page);
        when(platMapper.toPlatResponse(page)).thenReturn(response);

        // Act
        PlatResponse result = platService.find(pageable, null);

        // Assert
        assertEquals(response, result);
        verify(platRepository).findByAlimentUserId(pageable, currentUser.getId());
        verify(platMapper).toPlatResponse(page);
    }

    @Test
    void find_shouldReturnPlatResponse_withSearch() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String search = "salade";
        List<PlatWithoutRecetteDTO> platDTOs = List.of(mock(PlatWithoutRecetteDTO.class));
        Page<PlatWithoutRecetteDTO> page = new PageImpl<>(platDTOs);
        PlatResponse response = mock(PlatResponse.class);

        when(platRepository.findByAlimentUserIdAndAlimentNomContainingIgnoreCase(
                pageable, currentUser.getId(), "%" + search.toUpperCase() + "%")).thenReturn(page);
        when(platMapper.toPlatResponse(page)).thenReturn(response);

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
        List<PlatWithoutRecetteDTO> platDTOs = List.of(mock(PlatWithoutRecetteDTO.class));
        Page<PlatWithoutRecetteDTO> page = new PageImpl<>(platDTOs);
        PlatResponse response = mock(PlatResponse.class);

        when(platRepository.findByAlimentUserId(pageable, currentUser.getId())).thenReturn(page);
        when(platMapper.toPlatResponse(any())).thenReturn(response);

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
        plat.getAliment().setUserId(otherUser.getId());
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
        when(nutritionalCalculator.calculateTotals(any())).thenReturn(mockTotals);
        when(nutritionalCalculator.createPlatAliment(eq(platDTO), eq(mockTotals), eq(currentUser)))
                .thenReturn(alimentPlat);
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        platService.add(platDTO);

        // Assert
        verify(alimentRepository).findById(10L);
        verify(nutritionalCalculator).calculateTotals(any());
        verify(nutritionalCalculator).createPlatAliment(eq(platDTO), eq(mockTotals), eq(currentUser));
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
        verifyNoInteractions(nutritionalCalculator);
    }

    @Test
    void add_shouldThrowForbiddenException_whenIngredientNotAccessible() {
        // Arrange
        alimentIngredient.setUserId(otherUser.getId());
        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 200.0f);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Salade", List.of(recetteDTO));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> platService.add(platDTO));
        assertEquals("Vous n'avez pas accès à l'aliment avec l'ID 10", exception.getMessage());
        verify(alimentRepository, never()).save(any());
        verifyNoInteractions(nutritionalCalculator);
    }

    @Test
    void add_shouldAllowSystemAliment() {
        // Arrange
        User systemUser = new User();
        systemUser.setId(1L);
        alimentIngredient.setUserId(systemUser.getId());

        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 200.0f);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Salade", List.of(recetteDTO));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(nutritionalCalculator.calculateTotals(any())).thenReturn(mockTotals);
        when(nutritionalCalculator.createPlatAliment(any(), any(), any())).thenReturn(alimentPlat);
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        platService.add(platDTO);

        // Assert
        verify(alimentRepository).save(any(Aliment.class));
        verify(nutritionalCalculator).calculateTotals(any());
        verify(nutritionalCalculator).createPlatAliment(any(), any(), any());
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
        verifyNoInteractions(nutritionalCalculator);
    }

    @Test
    void add_shouldAggregateRecettes_whenSameIngredientMultipleTimes() {
        // Arrange
        RecetteInputDTO recette1 = new RecetteInputDTO(10L, 100.0f);
        RecetteInputDTO recette2 = new RecetteInputDTO(10L, 150.0f);
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Salade", List.of(recette1, recette2));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(nutritionalCalculator.calculateTotals(any())).thenReturn(mockTotals);
        when(nutritionalCalculator.createPlatAliment(any(), any(), any())).thenReturn(alimentPlat);
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
        verify(nutritionalCalculator).calculateTotals(argThat(map -> map.containsKey(alimentIngredient) && map.get(alimentIngredient).equals(250.0f)));
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
        when(nutritionalCalculator.calculateTotals(any())).thenReturn(mockTotals);
        doNothing().when(nutritionalCalculator).updateAlimentWithTotals(any(), any(), anyFloat());
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);

        // Act
        platService.update(platId, platDTO);

        // Assert
        verify(platRepository).findById(platId);
        verify(nutritionalCalculator).calculateTotals(any());
        verify(nutritionalCalculator).updateAlimentWithTotals(eq(alimentPlat), eq(mockTotals), eq(3.0f));
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
        when(nutritionalCalculator.calculateTotals(any())).thenReturn(mockTotals);
        doNothing().when(nutritionalCalculator).updateAlimentWithTotals(any(), any(), anyFloat());
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        platService.update(platId, platDTO);

        // Assert
        verify(recetteRepository).deleteAll(List.of(existingRecette));
        verify(nutritionalCalculator).calculateTotals(any());
        verify(nutritionalCalculator).updateAlimentWithTotals(any(), any(), anyFloat());
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
        verifyNoInteractions(nutritionalCalculator);
    }

    @Test
    void update_shouldThrowForbiddenException_whenPlatNotOwned() {
        // Arrange
        long platId = 30L;
        plat.getAliment().setUserId(otherUser.getId());
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Test", List.of());

        when(platRepository.findById(platId)).thenReturn(Optional.of(plat));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> platService.update(platId, platDTO));
        assertEquals("Vous n'êtes pas autorisé à accéder à ce plat", exception.getMessage());
        verifyNoInteractions(nutritionalCalculator);
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
        plat.getAliment().setUserId(otherUser.getId());
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
        when(nutritionalCalculator.calculateTotals(any())).thenReturn(mockTotals);
        when(nutritionalCalculator.createPlatAliment(any(), any(), any())).thenReturn(alimentPlat);
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act & Assert - Ne doit pas lever d'exception
        assertDoesNotThrow(() -> platService.add(platDTO));
        verify(alimentRepository).save(any(Aliment.class));
        verify(nutritionalCalculator).calculateTotals(any());
    }

    @Test
    void add_shouldHandlePortionUnit_whenIngredientIsPortionBased() {
        // Arrange
        alimentIngredient.setUnite("portion");
        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 2.0f); // 2 portions
        PlatInputDTO platDTO = new PlatInputDTO(1.0f, "Test Plat", List.of(recetteDTO));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(nutritionalCalculator.calculateTotals(any())).thenReturn(mockTotals);
        when(nutritionalCalculator.createPlatAliment(any(), any(), any())).thenReturn(alimentPlat);
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        platService.add(platDTO);

        // Assert
        verify(nutritionalCalculator).calculateTotals(argThat(map -> {
            // Vérifier que la quantité est utilisée telle quelle (pas divisée par 100)
            return map.containsKey(alimentIngredient) && map.get(alimentIngredient).equals(2.0f);
        }));
    }

    @Test
    void add_shouldHandleGramUnit_whenIngredientIsGramBased() {
        // Arrange
        alimentIngredient.setUnite("g");
        RecetteInputDTO recetteDTO = new RecetteInputDTO(10L, 200.0f); // 200g
        PlatInputDTO platDTO = new PlatInputDTO(1.0f, "Test Plat", List.of(recetteDTO));

        when(alimentRepository.findById(10L)).thenReturn(Optional.of(alimentIngredient));
        when(nutritionalCalculator.calculateTotals(any())).thenReturn(mockTotals);
        when(nutritionalCalculator.createPlatAliment(any(), any(), any())).thenReturn(alimentPlat);
        when(alimentRepository.save(any(Aliment.class))).thenReturn(alimentPlat);
        when(recetteRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        platService.add(platDTO);

        // Assert
        verify(nutritionalCalculator).calculateTotals(argThat(map -> {
            // Vérifier que la quantité est divisée par 100 pour les grammes
            return map.containsKey(alimentIngredient) && map.get(alimentIngredient).equals(200.0f);
        }));
    }
}