package com.platydev.compteurcalories.service.impl;

import com.platydev.compteurcalories.dto.input.JournalInputDTO;
import com.platydev.compteurcalories.dto.output.JournalAlimentDTO;
import com.platydev.compteurcalories.dto.output.JournalDTO;
import com.platydev.compteurcalories.dto.output.JournalDetails;
import com.platydev.compteurcalories.dto.output.NutrientTotals;
import com.platydev.compteurcalories.entity.*;
import com.platydev.compteurcalories.entity.security.AppRole;
import com.platydev.compteurcalories.entity.security.Role;
import com.platydev.compteurcalories.entity.security.User;
import com.platydev.compteurcalories.exception.ApiException;
import com.platydev.compteurcalories.exception.ForbiddenException;
import com.platydev.compteurcalories.exception.NotFoundException;
import com.platydev.compteurcalories.infrastructure.JournalAlimentMapper;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.JournalAlimentRepository;
import com.platydev.compteurcalories.repository.JournalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JournalServiceImplTests {

    @Mock
    private JournalRepository journalRepository;

    @Mock
    private JournalAlimentRepository journalAlimentRepository;

    @Mock
    private AlimentRepository alimentRepository;

    @Mock
    private NutritionalCalculator nutritionalCalculator;

    @Mock
    private JournalAlimentMapper journalAlimentMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JournalServiceImpl journalService;

    private User testUser;
    private Aliment testAliment1;
    private Aliment testAliment2;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        // Setup utilisateur de test
        Role userRole = new Role(2L, AppRole.ROLE_USER);
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .roles(Set.of(userRole))
                .build();

        // Setup utilisateur admin (ID=1 pour les aliments publics)
        Role adminRole = new Role(1L, AppRole.ROLE_ADMIN);
        User adminUser = User.builder()
                .id(1L)
                .username("admin")
                .password("password")
                .roles(Set.of(adminRole))
                .build();

        // Setup aliments de test
        testAliment1 = Aliment.builder()
                .id(1L)
                .nom("Pomme")
                .calories(52f)
                .unite("100g")
                .user(testUser)
                .build();

        testAliment2 = Aliment.builder()
                .id(2L)
                .nom("Banane")
                .calories(89f)
                .unite("100g")
                .user(adminUser) // Aliment public
                .build();

        testDate = LocalDate.of(2024, 1, 15);

        // Mock du SecurityContext
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
    }

    @Test
    void getJournalByDate_ShouldReturnJournalDTO_WhenDataExists() {
        // Given
        Journal breakfast = new Journal();
        breakfast.setJournalId(new JournalId(testUser.getId(), testDate, 1));
        breakfast.setCalories(300f);

        Journal lunch = new Journal();
        lunch.setJournalId(new JournalId(testUser.getId(), testDate, 2));
        lunch.setCalories(500f);

        List<Journal> journaux = Arrays.asList(breakfast, lunch);
        when(journalRepository.findByJournalId_UserIdAndJournalId_Date(testUser.getId(), testDate))
                .thenReturn(journaux);

        // When
        JournalDTO result = journalService.getJournalByDate(testDate);

        // Then
        assertNotNull(result);
        assertEquals(300f, result.breakfast());
        assertEquals(500f, result.lunch());
        assertNull(result.diner());
    }

    @Test
    void getJournalByDate_ShouldReturnNullValues_WhenNoData() {
        // Given
        when(journalRepository.findByJournalId_UserIdAndJournalId_Date(testUser.getId(), testDate))
                .thenReturn(Collections.emptyList());

        // When
        JournalDTO result = journalService.getJournalByDate(testDate);

        // Then
        assertNotNull(result);
        assertNull(result.breakfast());
        assertNull(result.lunch());
        assertNull(result.diner());
    }

    @Test
    void addJournalEntry_ShouldCreateJournalAndJournalAliment_WhenValidInput() {
        // Given
        int repas = 1;
        List<JournalAlimentDTO> alimentQuantites = Arrays.asList(
                new JournalAlimentDTO(1L, 100f),
                new JournalAlimentDTO(2L, 150f)
        );
        JournalInputDTO input = new JournalInputDTO(alimentQuantites);

        when(journalRepository.existsById(new JournalId(testUser.getId(), testDate, repas)))
                .thenReturn(false);
        when(alimentRepository.findById(1L)).thenReturn(Optional.of(testAliment1));
        when(alimentRepository.findById(2L)).thenReturn(Optional.of(testAliment2));
        when(nutritionalCalculator.calculateTotalCalories(any())).thenReturn(400f);

        // When
        journalService.addJournalEntry(testDate, repas, input);

        // Then
        verify(journalRepository).save(any(Journal.class));
        verify(journalAlimentRepository).saveAll(anyList());
    }

    @Test
    void addJournalEntry_ShouldThrowApiException_WhenJournalAlreadyExists() {
        // Given
        int repas = 1;
        JournalInputDTO input = new JournalInputDTO(Collections.emptyList());

        when(journalRepository.existsById(new JournalId(testUser.getId(), testDate, repas)))
                .thenReturn(true);

        // When & Then
        ApiException exception = assertThrows(ApiException.class,
                () -> journalService.addJournalEntry(testDate, repas, input));
        assertEquals("Un journal existe déjà pour cette date et ce repas. Utilisez PUT pour le modifier.",
                exception.getMessage());
    }

    @Test
    void addJournalEntry_ShouldThrowApiException_WhenInvalidRepasNumber() {
        // Given
        int repas = 5; // Invalid
        JournalInputDTO input = new JournalInputDTO(Collections.emptyList());

        // When & Then
        ApiException exception = assertThrows(ApiException.class,
                () -> journalService.addJournalEntry(testDate, repas, input));
        assertEquals("Le numéro de repas doit être 1 (petit-déjeuner), 2 (déjeuner) ou 3 (dîner)",
                exception.getMessage());
    }

    @Test
    void addJournalEntry_ShouldThrowNotFoundException_WhenAlimentNotFound() {
        // Given
        int repas = 1;
        List<JournalAlimentDTO> alimentQuantites = List.of(
                new JournalAlimentDTO(999L, 100f) // Aliment inexistant
        );
        JournalInputDTO input = new JournalInputDTO(alimentQuantites);

        when(journalRepository.existsById(any())).thenReturn(false);
        when(alimentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> journalService.addJournalEntry(testDate, repas, input));
        assertEquals("Aliment avec l'ID 999 non trouvé", exception.getMessage());
    }

    @Test
    void addJournalEntry_ShouldThrowForbiddenException_WhenUserDoesNotOwnAliment() {
        // Given
        User otherUser = User.builder().id(3L).username("other").build();
        Aliment forbiddenAliment = Aliment.builder()
                .id(3L)
                .nom("Forbidden")
                .user(otherUser)
                .build();

        int repas = 1;
        List<JournalAlimentDTO> alimentQuantites = List.of(
                new JournalAlimentDTO(3L, 100f)
        );
        JournalInputDTO input = new JournalInputDTO(alimentQuantites);

        when(journalRepository.existsById(any())).thenReturn(false);
        when(alimentRepository.findById(3L)).thenReturn(Optional.of(forbiddenAliment));

        // When & Then
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> journalService.addJournalEntry(testDate, repas, input));
        assertEquals("Vous n'avez pas accès à l'aliment avec l'ID 3", exception.getMessage());
    }

    @Test
    void addJournalEntry_ShouldThrowApiException_WhenEmptyAlimentList() {
        // Given
        int repas = 1;
        JournalInputDTO input = new JournalInputDTO(Collections.emptyList());

        when(journalRepository.existsById(any())).thenReturn(false);

        // When & Then
        ApiException exception = assertThrows(ApiException.class,
                () -> journalService.addJournalEntry(testDate, repas, input));
        assertEquals("La liste des aliments ne peut pas être vide", exception.getMessage());
    }

    @Test
    void addJournalEntry_ShouldThrowApiException_WhenNegativeQuantity() {
        // Given
        int repas = 1;
        List<JournalAlimentDTO> alimentQuantites = List.of(
                new JournalAlimentDTO(1L, -50f) // Quantité négative
        );
        JournalInputDTO input = new JournalInputDTO(alimentQuantites);

        when(journalRepository.existsById(any())).thenReturn(false);

        // When & Then
        ApiException exception = assertThrows(ApiException.class,
                () -> journalService.addJournalEntry(testDate, repas, input));
        assertEquals("La quantité doit être positive pour l'aliment ID: 1", exception.getMessage());
    }

    @Test
    void updateJournalEntry_ShouldUpdateSuccessfully_WhenValidInput() {
        // Given
        int repas = 1;
        List<JournalAlimentDTO> alimentQuantites = List.of(
                new JournalAlimentDTO(1L, 200f)
        );
        JournalInputDTO input = new JournalInputDTO(alimentQuantites);

        JournalId journalId = new JournalId(testUser.getId(), testDate, repas);
        Journal existingJournal = new Journal();
        existingJournal.setJournalId(journalId);
        existingJournal.setCalories(300f);

        when(journalRepository.existsById(journalId)).thenReturn(true);
        when(journalRepository.findById(journalId)).thenReturn(Optional.of(existingJournal));
        when(journalAlimentRepository.findByJournalAlimentId_UserIdAndJournalAlimentId_DateAndJournalAlimentId_Repas(
                testUser.getId(), testDate, repas)).thenReturn(Collections.emptyList());
        when(alimentRepository.findById(1L)).thenReturn(Optional.of(testAliment1));
        when(nutritionalCalculator.calculateTotalCalories(any())).thenReturn(400f);

        // When
        journalService.updateJournalEntry(testDate, repas, input);

        // Then
        verify(journalAlimentRepository).deleteAll(anyList());
        verify(journalRepository).save(existingJournal);
        verify(journalAlimentRepository).saveAll(anyList());
        assertEquals(400f, existingJournal.getCalories());
    }

    @Test
    void updateJournalEntry_ShouldThrowNotFoundException_WhenJournalNotExists() {
        // Given
        int repas = 1;
        JournalInputDTO input = new JournalInputDTO(Collections.emptyList());

        JournalId journalId = new JournalId(testUser.getId(), testDate, repas);
        when(journalRepository.existsById(journalId)).thenReturn(false);

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> journalService.updateJournalEntry(testDate, repas, input));
        assertEquals("Aucun journal trouvé pour cette date et ce repas", exception.getMessage());
    }

    @Test
    void getJournalDetails_ShouldReturnDetails_WhenDataExists() {
        // Given
        JournalAliment ja1 = new JournalAliment();
        ja1.setJournalAlimentId(new JournalAlimentId(1L, testUser.getId(), testDate, 1));
        ja1.setAliment(testAliment1);
        ja1.setQuantite(100f);

        JournalAliment ja2 = new JournalAliment();
        ja2.setJournalAlimentId(new JournalAlimentId(2L, testUser.getId(), testDate, 2));
        ja2.setAliment(testAliment2);
        ja2.setQuantite(150f);

        List<JournalAliment> journalAliments = Arrays.asList(ja1, ja2);
        List<JournalAlimentDTO> alimentDTOs = Arrays.asList(
                new JournalAlimentDTO(1L, 100f),
                new JournalAlimentDTO(2L, 150f)
        );
        NutrientTotals nutrients = new NutrientTotals();
        nutrients.setCalories(300f);

        when(journalAlimentRepository.findByJournalAlimentId_UserIdAndJournalAlimentId_Date(
                testUser.getId(), testDate)).thenReturn(journalAliments);
        when(journalAlimentMapper.toDTOList(journalAliments)).thenReturn(alimentDTOs);
        when(nutritionalCalculator.calculateTotals(any())).thenReturn(nutrients);

        // When
        JournalDetails result = journalService.getJournalDetails(testDate);

        // Then
        assertNotNull(result);
        assertEquals(testDate, result.date());
        assertNull(result.repas());
        assertEquals(2, result.alimentQuantites().size());
        assertEquals(300f, result.nutrients().getCalories());
    }

    @Test
    void getJournalDetails_ShouldReturnEmptyDetails_WhenNoData() {
        // Given
        when(journalAlimentRepository.findByJournalAlimentId_UserIdAndJournalAlimentId_Date(
                testUser.getId(), testDate)).thenReturn(Collections.emptyList());

        // When
        JournalDetails result = journalService.getJournalDetails(testDate);

        // Then
        assertNotNull(result);
        assertEquals(testDate, result.date());
        assertNull(result.repas());
        assertTrue(result.alimentQuantites().isEmpty());
        assertNotNull(result.nutrients());
    }

    @Test
    void getJournalMealDetails_ShouldReturnMealDetails_WhenDataExists() {
        // Given
        int repas = 1;
        JournalAliment ja1 = new JournalAliment();
        ja1.setJournalAlimentId(new JournalAlimentId(1L, testUser.getId(), testDate, repas));
        ja1.setAliment(testAliment1);
        ja1.setQuantite(100f);

        List<JournalAliment> journalAliments = List.of(ja1);
        List<JournalAlimentDTO> alimentDTOs = List.of(new JournalAlimentDTO(1L, 100f));
        NutrientTotals nutrients = new NutrientTotals();
        nutrients.setCalories(150f);

        when(journalAlimentRepository.findByJournalAlimentId_UserIdAndJournalAlimentId_DateAndJournalAlimentId_Repas(
                testUser.getId(), testDate, repas)).thenReturn(journalAliments);
        when(journalAlimentMapper.toDTOList(journalAliments)).thenReturn(alimentDTOs);
        when(nutritionalCalculator.calculateTotals(any())).thenReturn(nutrients);

        // When
        JournalDetails result = journalService.getJournalMealDetails(testDate, repas);

        // Then
        assertNotNull(result);
        assertEquals(testDate, result.date());
        assertEquals(repas, result.repas());
        assertEquals(1, result.alimentQuantites().size());
        assertEquals(150f, result.nutrients().getCalories());
    }

    @Test
    void getJournalMealDetails_ShouldReturnEmptyDetails_WhenNoData() {
        // Given
        int repas = 1;
        when(journalAlimentRepository.findByJournalAlimentId_UserIdAndJournalAlimentId_DateAndJournalAlimentId_Repas(
                testUser.getId(), testDate, repas)).thenReturn(Collections.emptyList());

        // When
        JournalDetails result = journalService.getJournalMealDetails(testDate, repas);

        // Then
        assertNotNull(result);
        assertEquals(testDate, result.date());
        assertEquals(repas, result.repas());
        assertTrue(result.alimentQuantites().isEmpty());
        assertNotNull(result.nutrients());
    }

    @Test
    void getJournalMealDetails_ShouldThrowApiException_WhenInvalidRepasNumber() {
        // Given
        int repas = 0; // Invalid

        // When & Then
        ApiException exception = assertThrows(ApiException.class,
                () -> journalService.getJournalMealDetails(testDate, repas));
        assertEquals("Le numéro de repas doit être 1 (petit-déjeuner), 2 (déjeuner) ou 3 (dîner)",
                exception.getMessage());
    }

    @Test
    void addJournalEntry_ShouldAggregateQuantities_WhenSameAlimentMultipleTimes() {
        // Given
        int repas = 1;
        List<JournalAlimentDTO> alimentQuantites = Arrays.asList(
                new JournalAlimentDTO(1L, 50f),
                new JournalAlimentDTO(1L, 75f), // Même aliment, quantités à additionner
                new JournalAlimentDTO(2L, 100f)
        );
        JournalInputDTO input = new JournalInputDTO(alimentQuantites);

        when(journalRepository.existsById(any())).thenReturn(false);
        when(alimentRepository.findById(1L)).thenReturn(Optional.of(testAliment1));
        when(alimentRepository.findById(2L)).thenReturn(Optional.of(testAliment2));
        when(nutritionalCalculator.calculateTotalCalories(any())).thenReturn(400f);

        // When
        journalService.addJournalEntry(testDate, repas, input);

        // Then
        verify(journalAlimentRepository).saveAll(argThat(list -> {
            List<JournalAliment> journalAliments = (List<JournalAliment>) list;
            // Vérifier qu'il y a bien 2 entrées (pas 3) car l'aliment 1 est agrégé
            assertEquals(2, journalAliments.size());

            // Trouver l'entrée pour l'aliment 1 et vérifier que la quantité est agrégée (50 + 75 = 125)
            Optional<JournalAliment> aliment1Entry = journalAliments.stream()
                    .filter(ja -> ja.getAliment().getId().equals(1L))
                    .findFirst();
            assertTrue(aliment1Entry.isPresent());
            assertEquals(125f, aliment1Entry.get().getQuantite());

            return true;
        }));
    }
}