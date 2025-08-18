package com.platydev.compteurcalories.controller;

import com.platydev.compteurcalories.dto.input.JournalInputDTO;
import com.platydev.compteurcalories.dto.input.LoginInput;
import com.platydev.compteurcalories.dto.output.JournalAlimentDTO;
import com.platydev.compteurcalories.dto.output.JournalDTO;
import com.platydev.compteurcalories.dto.output.JournalDetails;
import com.platydev.compteurcalories.dto.output.LoginOutput;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JournalControllerIntegrationTests {

    private final TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;
    private String jwtTokenTestUser;
    private String jwtTokenAdmin;
    private String jwtTokenUser2;

    @Autowired
    public JournalControllerIntegrationTests(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/journaux";

        // Authentifier testuser pour obtenir un JWT
        jwtTokenTestUser = authenticateUser("testuser", "password123");

        // Authentifier admin pour obtenir un JWT
        jwtTokenAdmin = authenticateUser("admin", "admin123");

        // Authentifier user2 pour obtenir un JWT
        jwtTokenUser2 = authenticateUser("user2", "user123");
    }

    private String authenticateUser(String username, String password) {
        LoginInput loginInput = new LoginInput(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginInput> request = new HttpEntity<>(loginInput, headers);
        ResponseEntity<LoginOutput> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/login",
                request,
                LoginOutput.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        return response.getBody().token();
    }

    @Test
    @Order(1)
    void getJournalByDate_shouldReturnEmptyJournalForNewDate() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<JournalDTO> response = restTemplate.exchange(
                baseUrl + "/20240101",
                HttpMethod.GET,
                request,
                JournalDTO.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().breakfast());
        assertNull(response.getBody().lunch());
        assertNull(response.getBody().diner());
    }

    @Test
    @Order(2)
    void addJournalEntry_shouldCreateBreakfastForTestUser() {
        // Arrange
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(1L, 100.0f), // Pomme Rouge (admin) - 52 cal
                new JournalAlimentDTO(7L, 50.0f)   // Brocoli (testuser) - 34 cal * 0.5 = 17 cal
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240101/repas/1",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Vérifier que l'entrée a été créée
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<JournalDTO> getResponse = restTemplate.exchange(
                baseUrl + "/20240101",
                HttpMethod.GET,
                getRequest,
                JournalDTO.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertNotNull(getResponse.getBody().breakfast());
        assertEquals(69.0f, getResponse.getBody().breakfast(), 0.01f); // 52 + 17 = 69
        assertNull(getResponse.getBody().lunch());
        assertNull(getResponse.getBody().diner());
    }

    @Test
    @Order(3)
    void addJournalEntry_shouldCreateLunchForTestUser() {
        // Arrange
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(4L, 150.0f), // Poulet Grillé (admin) - 165 cal * 1.5 = 247.5 cal
                new JournalAlimentDTO(6L, 100.0f)  // Carotte (testuser) - 41 cal
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240101/repas/2",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Vérifier que l'entrée a été créée
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<JournalDTO> getResponse = restTemplate.exchange(
                baseUrl + "/20240101",
                HttpMethod.GET,
                getRequest,
                JournalDTO.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertNotNull(getResponse.getBody().breakfast());
        assertNotNull(getResponse.getBody().lunch());
        assertEquals(69.0f, getResponse.getBody().breakfast(), 0.01f);
        assertEquals(288.5f, getResponse.getBody().lunch(), 0.01f); // 247.5 + 41 = 288.5
        assertNull(getResponse.getBody().diner());
    }

    @Test
    @Order(4)
    void addJournalEntry_shouldCreateDinnerForTestUser() {
        // Arrange
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(5L, 100.0f), // Saumon Atlantique (admin) - 208 cal
                new JournalAlimentDTO(8L, 150.0f), // Yaourt Nature (testuser) - 59 cal * 1.5 = 88.5 cal
                new JournalAlimentDTO(15L, 1.0f)   // Smoothie Bowl Banane-Yaourt (testuser) - 74 cal * 1 portion = 74 cal
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240101/repas/3",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Vérifier que l'entrée a été créée
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<JournalDTO> getResponse = restTemplate.exchange(
                baseUrl + "/20240101",
                HttpMethod.GET,
                getRequest,
                JournalDTO.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertNotNull(getResponse.getBody().breakfast());
        assertNotNull(getResponse.getBody().lunch());
        assertNotNull(getResponse.getBody().diner());
        assertEquals(69.0f, getResponse.getBody().breakfast(), 0.01f);
        assertEquals(288.5f, getResponse.getBody().lunch(), 0.01f);
        assertEquals(370.5f, getResponse.getBody().diner(), 0.01f); // 208 + 88.5 + 74 = 370.5
    }

    @Test
    @Order(5)
    void addJournalEntry_shouldReturn400WhenMealAlreadyExists() {
        // Arrange - Essayer d'ajouter un petit-déjeuner qui existe déjà
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(2L, 100.0f) // Banane (admin)
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240101/repas/1",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(6)
    void addJournalEntry_shouldReturn400ForInvalidMealNumber() {
        // Arrange
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(1L, 100.0f)
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240102/repas/4", // Repas invalide
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(7)
    void addJournalEntry_shouldReturn403WhenAccessingOtherUserAliment() {
        // Arrange - testuser essaie d'utiliser un aliment de user2
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(9L, 100.0f) // Avocat (user2)
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240102/repas/1",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(8)
    void addJournalEntry_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(1L, 100.0f)
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Pas de token JWT
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240102/repas/1",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(9)
    void updateJournalEntry_shouldUpdateBreakfastForTestUser() {
        // Arrange - Modifier le petit-déjeuner existant
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(1L, 100.0f), // Pomme Rouge (admin) - 52 cal
                new JournalAlimentDTO(2L, 100.0f), // Banane (admin) - 89 cal
                new JournalAlimentDTO(3L, 50.0f)   // Riz Blanc (admin) - 130 cal * 0.5 = 65 cal
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/20240101/repas/1",
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Vérifier que l'entrée a été modifiée
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<JournalDTO> getResponse = restTemplate.exchange(
                baseUrl + "/20240101",
                HttpMethod.GET,
                getRequest,
                JournalDTO.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(206.0f, getResponse.getBody().breakfast(), 0.01f); // 89 + 65 + 52 = 206
        assertEquals(288.5f, getResponse.getBody().lunch(), 0.01f); // Inchangé
        assertEquals(370.5f, getResponse.getBody().diner(), 0.01f); // Inchangé
    }

    @Test
    @Order(10)
    void updateJournalEntry_shouldReturn404WhenMealNotFound() {
        // Arrange
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(1L, 100.0f)
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/20240102/repas/1", // Date sans journal
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(11)
    void getJournalDetails_shouldReturnCompleteDetailsForDay() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<JournalDetails> response = restTemplate.exchange(
                baseUrl + "/20240101/details",
                HttpMethod.GET,
                request,
                JournalDetails.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("2024-01-01", response.getBody().date().toString());
        assertNull(response.getBody().repas());
        assertNotNull(response.getBody().alimentQuantites());
        assertEquals(8, response.getBody().alimentQuantites().size()); // 3 aliments petit-déj + 2 aliments déjeuner + 3 aliments dîner

        // Vérifier les totaux nutritionnels détaillés pour toute la journée
        // Petit-déjeuner modifié: 100g Banane + 50g Riz Blanc + 100g Pomme Rouge = 89 + 65 + 52 = 206 cal
        // Déjeuner: 150g Poulet Grillé + 100g Carotte = 247.5 + 41 = 288.5 cal
        // Dîner: 100g Saumon + 150g Yaourt + 1 portion Smoothie Bowl = 208 + 88.5 + 74 = 370.5 cal
        // Total: 154 + 288.5 + 370.5 = 813 cal
        assertNotNull(response.getBody().nutrients());
        assertEquals(865.0f, response.getBody().nutrients().getCalories(), 0.1f);

        // Calculs détaillés des macronutriments pour la journée complète:
        // Matières grasses: (100g Banane: 0.3g) + (100g Pomme Rouge: 0.2g) + (50g Riz: 0.15g) + (150g Poulet: 5.4g) + (100g Carotte: 0.2g) + (100g Saumon: 12.4g) + (150g Yaourt: 0.6g) + (1 portion Smoothie: 0.35g) = 19.6g
        assertEquals(19.6f, response.getBody().nutrients().getMatieresGrasses(), 0.1f);

        // Protéines: (100g Banane: 1.1g) + (100g Pomme Rouge: 0.3g) + (50g Riz: 1.35g) + (150g Poulet: 46.5g) + (100g Carotte: 0.9g) + (100g Saumon: 22.1g) + (150g Yaourt: 15g) + (1 portion Smoothie: 5.55g) = 92.8g
        assertEquals(92.8f, response.getBody().nutrients().getProteines(), 0.1f);

        // Glucides: (100g Banane: 23g) + (100g Pomme Rouge: 14g) + (50g Riz: 14g) + (150g Poulet: 0g) + (100g Carotte: 10g) + (100g Saumon: 0g) + (150g Yaourt: 5.4g) + (1 portion Smoothie: 13.3g) = 79.7g
        assertEquals(79.7f, response.getBody().nutrients().getGlucides(), 0.1f);

        // Fibres: (100g Banane: 2.6g) + (100g Pomme Rouge: 2.4g) + (50g Riz: 0.2g) + (150g Poulet: 0g) + (100g Carotte: 2.8g) + (100g Saumon: 0g) + (150g Yaourt: 0g) + (1 portion Smoothie: 1.3g) = 9.3g
        assertEquals(9.3f, response.getBody().nutrients().getFibres(), 0.1f);

        // Vitamines importantes:
        // Vitamine C: (100g Banane: 8.7mg) + (100g Pomme Rouge: 4.6mg) + (50g Riz: 0mg) + (150g Poulet: 0mg) + (100g Carotte: 5.9mg) + (100g Saumon: 11mg) + (150g Yaourt: 0mg) + (1 portion Smoothie: 4.35mg) = 34.55mg
        assertEquals(34.55f, response.getBody().nutrients().getVitamineC(), 0.1f);

        // Vitamine B12: (100g Banane: 0mg) + (100g Pomme Rouge: 0mg) + (50g Riz: 0mg) + (150g Poulet: 0.45mg) + (100g Carotte: 0mg) + (100g Saumon: 3.2mg) + (150g Yaourt: 0.6mg) + (1 portion Smoothie: 0.2mg) = 4.45mg
        assertEquals(4.45f, response.getBody().nutrients().getVitamineB12(), 0.1f);

        // Minéraux:
        // Calcium (Ca): (100g Banane: 5mg) + (100g Pomme Rouge: 6mg) + (50g Riz: 14mg) + (150g Poulet: 22.5mg) + (100g Carotte: 33mg) + (100g Saumon: 12mg) + (150g Yaourt: 165mg) + (1 portion Smoothie: 57.5mg) = 315mg
        assertEquals(315.0f, response.getBody().nutrients().getCa(), 0.1f);

        // Fer (Fe): (100g Banane: 0.26mg) + (100g Pomme Rouge: 0.12mg) + (50g Riz: 0.4mg) + (150g Poulet: 1.335mg) + (100g Carotte: 0.3mg) + (100g Saumon: 0.31mg) + (150g Yaourt: 0.06mg) + (1 portion Smoothie: 0.15mg) = 2.935mg
        assertEquals(2.935f, response.getBody().nutrients().getFe(), 0.1f);

        // Potassium (K): (100g Banane: 358mg) + (100g Pomme Rouge: 107mg) + (50g Riz: 17.5mg) + (150g Poulet: 384mg) + (100g Carotte: 320mg) + (100g Saumon: 628mg) + (150g Yaourt: 211.5mg) + (1 portion Smoothie: 249.5mg) = 2275.5mg
        assertEquals(2275.5f, response.getBody().nutrients().getK(), 0.1f);
    }

    @Test
    @Order(12)
    void getJournalMealDetails_shouldReturnBreakfastDetails() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<JournalDetails> response = restTemplate.exchange(
                baseUrl + "/20240101/repas/1/details",
                HttpMethod.GET,
                request,
                JournalDetails.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("2024-01-01", response.getBody().date().toString());
        assertEquals(1, response.getBody().repas());
        assertNotNull(response.getBody().alimentQuantites());
        assertEquals(3, response.getBody().alimentQuantites().size()); // Banane + Riz + Pomme Rouge

        // Vérifier les totaux nutritionnels détaillés du petit-déjeuner
        // Petit-déjeuner modifié (test #9): 100g Banane + 50g Riz Blanc
        assertNotNull(response.getBody().nutrients());

        // Calories: (100g Banane: 89 cal) + (50g Riz: 65 cal) + (100g Pomme Rouge: 52 cal) = 206 cal
        assertEquals(206.0f, response.getBody().nutrients().getCalories(), 0.01f);

        // Macronutriments détaillés:
        // Matières grasses: (100g Banane: 0.3g) + (50g Riz: 0.15g) + (100g Pomme Rouge: 0.2g) = 0.65g
        assertEquals(0.65f, response.getBody().nutrients().getMatieresGrasses(), 0.01f);

        // Matières grasses saturées: (100g Banane: 0.11g) + (50g Riz: 0.04g) + (100g Pomme Rouge: 0.03g) = 0.18g
        assertEquals(0.18f, response.getBody().nutrients().getMatieresGrassesSatures(), 0.01f);

        // Protéines: (100g Banane: 1.1g) + (50g Riz: 1.35g) + (100g Pomme Rouge: 0.3g) = 2.75g
        assertEquals(2.75f, response.getBody().nutrients().getProteines(), 0.01f);

        // Glucides: (100g Banane: 23g) + (50g Riz: 14g) + (100g Pomme Rouge: 14g) = 51g
        assertEquals(51.0f, response.getBody().nutrients().getGlucides(), 0.01f);

        // Sucre: (100g Banane: 12g) + (50g Riz: 0.05g) + (100g Pomme Rouge: 10g) = 22.05g
        assertEquals(22.05f, response.getBody().nutrients().getSucre(), 0.01f);

        // Fibres: (100g Banane: 2.6g) + (50g Riz: 0.2g) + (100g Pomme Rouge: 2.4g) = 5.2g
        assertEquals(5.2f, response.getBody().nutrients().getFibres(), 0.01f);

        // Vitamines du groupe B:
        // Vitamine B1: (100g Banane: 0.031mg) + (50g Riz: 0.035mg) + (100g Pomme Rouge: 0.026mg) = 0.092mg
        assertEquals(0.092f, response.getBody().nutrients().getVitamineB1(), 0.001f);

        // Vitamine B3: (100g Banane: 0.665mg) + (50g Riz: 0.8mg) + (100g Pomme Rouge: 0.061mg) = 1.526mg
        assertEquals(1.526f, response.getBody().nutrients().getVitamineB3(), 0.001f);

        // Vitamine B6: (100g Banane: 0.367mg) + (50g Riz: 0.082mg) + (100g Pomme Rouge: 0.005mg) = 0.454mg
        assertEquals(0.454f, response.getBody().nutrients().getVitamineB6(), 0.001f);

        // Autres vitamines:
        // Vitamine C: (100g Banane: 8.7mg) + (50g Riz: 0mg) + (100g Pomme Rouge: 4.6mg) = 13.3mg
        assertEquals(13.3f, response.getBody().nutrients().getVitamineC(), 0.1f);

        // Vitamine E: (100g Banane: 0.1mg) + (50g Riz: 0.055mg) + (100g Pomme Rouge: 0.18mg) = 0.335mg
        assertEquals(0.335f, response.getBody().nutrients().getVitamineE(), 0.001f);

        // Minéraux essentiels:
        // Magnésium (Mg): (100g Banane: 27mg) + (50g Riz: 12.5mg) + (100g Pomme Rouge: 5mg) = 44.5mg
        assertEquals(44.5f, response.getBody().nutrients().getMg(), 0.1f);

        // Phosphore (P): (100g Banane: 22mg) + (50g Riz: 57.5mg) + (100g Pomme Rouge: 11mg) = 90.5mg
        assertEquals(90.5f, response.getBody().nutrients().getP(), 0.1f);

        // Potassium (K): (100g Banane: 358mg) + (50g Riz: 17.5mg) + (100g Pomme Rouge: 107mg) = 482.5mg
        assertEquals(482.5f, response.getBody().nutrients().getK(), 0.1f);

        // Zinc (Zn): (100g Banane: 0.15mg) + (50g Riz: 0.545mg) + (100g Pomme Rouge: 0.04mg) = 0.735mg
        assertEquals(0.735f, response.getBody().nutrients().getZn(), 0.001f);

        // Manganèse (Mn): (100g Banane: 0.27mg) + (50g Riz: 0.545mg) + (100g Pomme Rouge: 0.035mg) = 0.85mg
        assertEquals(0.85f, response.getBody().nutrients().getMn(), 0.001f);

        // Cuivre (Cu): (100g Banane: 0.078mg) + (50g Riz: 0.11mg) + (100g Pomme Rouge: 0.027mg) = 0.215mg
        assertEquals(0.215f, response.getBody().nutrients().getCu(), 0.001f);
    }

    @Test
    @Order(13)
    void getJournalMealDetails_shouldReturnEmptyForNonExistentMeal() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<JournalDetails> response = restTemplate.exchange(
                baseUrl + "/20240102/repas/1/details", // Date sans journal
                HttpMethod.GET,
                request,
                JournalDetails.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("2024-01-02", response.getBody().date().toString());
        assertEquals(1, response.getBody().repas());
        assertNotNull(response.getBody().alimentQuantites());
        assertTrue(response.getBody().alimentQuantites().isEmpty());

        // Vérifier les totaux nutritionnels vides
        assertNotNull(response.getBody().nutrients());
        assertEquals(0.0f, response.getBody().nutrients().getCalories(), 0.01f);
    }

    @Test
    @Order(14)
    void getJournalMealDetails_shouldReturn400ForInvalidMealNumber() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<JournalDetails> response = restTemplate.exchange(
                baseUrl + "/20240101/repas/0/details", // Repas invalide
                HttpMethod.GET,
                request,
                JournalDetails.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(15)
    void addJournalEntry_shouldWorkForUser2WithOwnAliments() {
        // Arrange - user2 ajoute un repas avec ses propres aliments
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(9L, 100.0f),  // Avocat (user2) - 160 cal
                new JournalAlimentDTO(10L, 50.0f),  // Pain Complet (user2) - 247 cal * 0.5 = 123.5 cal
                new JournalAlimentDTO(1L, 100.0f)   // Pomme Rouge (admin) - 52 cal
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenUser2);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240103/repas/1",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Vérifier que l'entrée a été créée
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenUser2);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<JournalDTO> getResponse = restTemplate.exchange(
                baseUrl + "/20240103",
                HttpMethod.GET,
                getRequest,
                JournalDTO.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(335.5f, getResponse.getBody().breakfast(), 0.01f); // 160 + 123.5 + 52 = 335.5
        assertNull(getResponse.getBody().lunch());
        assertNull(getResponse.getBody().diner());
    }

    @Test
    @Order(16)
    void addJournalEntry_shouldWorkForAdminWithOwnAliments() {
        // Arrange - admin ajoute un repas
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(4L, 200.0f), // Poulet Grillé (admin) - 165 cal * 2 = 330 cal
                new JournalAlimentDTO(3L, 100.0f)  // Riz Blanc (admin) - 130 cal
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240104/repas/2",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Vérifier que l'entrée a été créée
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenAdmin);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<JournalDTO> getResponse = restTemplate.exchange(
                baseUrl + "/20240104",
                HttpMethod.GET,
                getRequest,
                JournalDTO.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertNull(getResponse.getBody().breakfast());
        assertEquals(460.0f, getResponse.getBody().lunch(), 0.01f); // 330 + 130 = 460
        assertNull(getResponse.getBody().diner());
    }

    @Test
    @Order(17)
    void addJournalEntry_shouldAggregateQuantitiesOfSameAliment() {
        // Arrange - Ajouter le même aliment plusieurs fois dans un repas
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(1L, 100.0f), // Pomme Rouge - 52 cal
                new JournalAlimentDTO(1L, 50.0f),  // Pomme Rouge encore - 52 cal * 0.5 = 26 cal
                new JournalAlimentDTO(2L, 100.0f)  // Banane - 89 cal
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240105/repas/1",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Vérifier le total - la pomme devrait être comptée 150g au total
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<JournalDTO> getResponse = restTemplate.exchange(
                baseUrl + "/20240105",
                HttpMethod.GET,
                getRequest,
                JournalDTO.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(167.0f, getResponse.getBody().breakfast(), 0.01f); // (52*1.5) + 89 = 78 + 89 = 167
    }

    @Test
    @Order(18)
    void getJournalByDate_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        // Pas de token JWT
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<JournalDTO> response = restTemplate.exchange(
                baseUrl + "/20240101",
                HttpMethod.GET,
                request,
                JournalDTO.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(19)
    void isolation_shouldEnsureUserDataIsolation() {
        // Arrange - Vérifier que testuser ne voit pas les journaux d'autres utilisateurs
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act - testuser regarde la date où user2 a un journal (20240103)
        ResponseEntity<JournalDTO> response = restTemplate.exchange(
                baseUrl + "/20240103",
                HttpMethod.GET,
                request,
                JournalDTO.class
        );

        // Assert - testuser ne devrait voir aucun journal pour cette date
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().breakfast());
        assertNull(response.getBody().lunch());
        assertNull(response.getBody().diner());
    }

    @Test
    @Order(20)
    void addJournalEntry_shouldReturn400ForEmptyAlimentList() {
        // Arrange
        JournalInputDTO journalInput = new JournalInputDTO(List.of()); // Liste vide

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240106/repas/1",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(21)
    void addJournalEntry_shouldReturn400ForNegativeQuantity() {
        // Arrange
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(1L, -50.0f) // Quantité négative
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240106/repas/1",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(22)
    void addJournalEntry_shouldReturn404ForNonExistentAliment() {
        // Arrange
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(99999L, 100.0f) // Aliment inexistant
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/20240106/repas/1",
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(23)
    void updateJournalEntry_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(1L, 100.0f)
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Pas de token JWT
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/20240101/repas/1",
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(24)
    void updateJournalEntry_shouldReturn403WhenAccessingOtherUserAliment() {
        // Arrange - testuser essaie de modifier avec un aliment de user2
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(9L, 100.0f) // Avocat (user2)
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/20240101/repas/1",
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(25)
    void updateJournalEntry_shouldReturn400ForInvalidMealNumber() {
        // Arrange
        List<JournalAlimentDTO> aliments = List.of(
                new JournalAlimentDTO(1L, 100.0f)
        );

        JournalInputDTO journalInput = new JournalInputDTO(aliments);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JournalInputDTO> request = new HttpEntity<>(journalInput, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/20240101/repas/0", // Repas invalide
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(26)
    void getJournalDetails_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        // Pas de token JWT
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<JournalDetails> response = restTemplate.exchange(
                baseUrl + "/20240101/details",
                HttpMethod.GET,
                request,
                JournalDetails.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(27)
    void getJournalMealDetails_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        // Pas de token JWT
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<JournalDetails> response = restTemplate.exchange(
                baseUrl + "/20240101/repas/1/details",
                HttpMethod.GET,
                request,
                JournalDetails.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(28)
    void complexScenario_shouldHandleMultipleMealsAndUpdates() {
        // Arrange - Scénario complexe avec plusieurs repas et modifications
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenUser2);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Créer un petit-déjeuner
        List<JournalAlimentDTO> breakfast = List.of(
                new JournalAlimentDTO(9L, 100.0f),  // Avocat - 160 cal
                new JournalAlimentDTO(2L, 50.0f)    // Banane (admin) - 89 * 0.5 = 44.5 cal
        );
        JournalInputDTO breakfastInput = new JournalInputDTO(breakfast);
        HttpEntity<JournalInputDTO> breakfastRequest = new HttpEntity<>(breakfastInput, headers);

        ResponseEntity<Void> breakfastResponse = restTemplate.postForEntity(
                baseUrl + "/20240107/repas/1",
                breakfastRequest,
                Void.class
        );
        assertEquals(HttpStatus.CREATED, breakfastResponse.getStatusCode());

        // Créer un déjeuner
        List<JournalAlimentDTO> lunch = List.of(
                new JournalAlimentDTO(10L, 100.0f), // Pain Complet - 247 cal
                new JournalAlimentDTO(5L, 150.0f)   // Saumon (admin) - 208 * 1.5 = 312 cal
        );
        JournalInputDTO lunchInput = new JournalInputDTO(lunch);
        HttpEntity<JournalInputDTO> lunchRequest = new HttpEntity<>(lunchInput, headers);

        ResponseEntity<Void> lunchResponse = restTemplate.postForEntity(
                baseUrl + "/20240107/repas/2",
                lunchRequest,
                Void.class
        );
        assertEquals(HttpStatus.CREATED, lunchResponse.getStatusCode());

        // Modifier le petit-déjeuner
        List<JournalAlimentDTO> updatedBreakfast = List.of(
                new JournalAlimentDTO(9L, 150.0f),  // Avocat - 160 * 1.5 = 240 cal
                new JournalAlimentDTO(1L, 100.0f)   // Pomme Rouge (admin) - 52 cal
        );
        JournalInputDTO updatedBreakfastInput = new JournalInputDTO(updatedBreakfast);
        HttpEntity<JournalInputDTO> updateRequest = new HttpEntity<>(updatedBreakfastInput, headers);

        ResponseEntity<Void> updateResponse = restTemplate.exchange(
                baseUrl + "/20240107/repas/1",
                HttpMethod.PUT,
                updateRequest,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, updateResponse.getStatusCode());

        // Vérifier le résultat final
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenUser2);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<JournalDTO> finalResponse = restTemplate.exchange(
                baseUrl + "/20240107",
                HttpMethod.GET,
                getRequest,
                JournalDTO.class
        );

        // Assert
        assertEquals(HttpStatus.OK, finalResponse.getStatusCode());
        assertNotNull(finalResponse.getBody());
        assertEquals(292.0f, finalResponse.getBody().breakfast(), 0.01f); // 240 + 52 = 292
        assertEquals(559.0f, finalResponse.getBody().lunch(), 0.01f);     // 247 + 312 = 559
        assertNull(finalResponse.getBody().diner());

        // Vérifier les détails complets de la journée
        ResponseEntity<JournalDetails> detailsResponse = restTemplate.exchange(
                baseUrl + "/20240107/details",
                HttpMethod.GET,
                getRequest,
                JournalDetails.class
        );

        assertEquals(HttpStatus.OK, detailsResponse.getStatusCode());
        assertNotNull(detailsResponse.getBody());
        assertEquals(4, detailsResponse.getBody().alimentQuantites().size()); // 2 aliments par repas
        assertEquals(851.0f, detailsResponse.getBody().nutrients().getCalories(), 0.01f); // 292 + 559 = 851
    }

    @Test
    @Order(29)
    void dateFormat_shouldHandleValidDateFormats() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act - Test avec différentes dates valides
        ResponseEntity<JournalDTO> response1 = restTemplate.exchange(
                baseUrl + "/20241231", // Fin d'année
                HttpMethod.GET,
                request,
                JournalDTO.class
        );

        ResponseEntity<JournalDTO> response2 = restTemplate.exchange(
                baseUrl + "/20240229", // Année bissextile
                HttpMethod.GET,
                request,
                JournalDTO.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }
}