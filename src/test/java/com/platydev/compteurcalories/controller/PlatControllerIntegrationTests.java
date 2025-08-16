package com.platydev.compteurcalories.controller;

import com.platydev.compteurcalories.dto.input.LoginInput;
import com.platydev.compteurcalories.dto.input.PlatInputDTO;
import com.platydev.compteurcalories.dto.input.RecetteInputDTO;
import com.platydev.compteurcalories.dto.output.LoginOutput;
import com.platydev.compteurcalories.dto.output.PlatDTO;
import com.platydev.compteurcalories.dto.output.PlatResponse;
import com.platydev.compteurcalories.dto.output.PlatWithoutRecetteDTO;
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
class PlatControllerIntegrationTests {

    private final TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;
    private String jwtTokenTestUser;
    private String jwtTokenUser2;

    @Autowired
    public PlatControllerIntegrationTests(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/plats";

        // Authentifier testuser pour obtenir un JWT
        jwtTokenTestUser = authenticateUser("testuser", "password123");

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
    void getAllForUser_shouldReturnUserPlats() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<PlatResponse> response = restTemplate.exchange(
                baseUrl + "?page=0&size=20&sort=id,asc",
                HttpMethod.GET,
                request,
                PlatResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().content());

        // Vérifier que testuser voit ses CINQ plats créés dans data.sql
        assertEquals(5, response.getBody().content().size());

        // Vérifier quelques noms des plats
        assertTrue(response.getBody().content().stream()
                .anyMatch(plat -> "Bol Protéiné au Poulet".equals(plat.nom())));
        assertTrue(response.getBody().content().stream()
                .anyMatch(plat -> "Salade de Saumon aux Légumes".equals(plat.nom())));
    }

    @Test
    @Order(2)
    void getAllForUser2_shouldReturnUser2Plats() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenUser2);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<PlatResponse> response = restTemplate.exchange(
                baseUrl + "?page=0&size=20&sort=id,asc",
                HttpMethod.GET,
                request,
                PlatResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().content());

        // Vérifier que user2 voit ses 2 plats créés dans data.sql
        assertEquals(2, response.getBody().content().size());

        // Vérifier les noms des plats de user2
        assertTrue(response.getBody().content().stream()
                .anyMatch(plat -> "Toast Complet Saumon-Avocat".equals(plat.nom())));
        assertTrue(response.getBody().content().stream()
                .anyMatch(plat -> "Bowl Tropical Poulet-Banane".equals(plat.nom())));
    }

    @Test
    @Order(3)
    void searchByNom_shouldReturnFilteredResults() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act - Rechercher les plats contenant "Saumon"
        ResponseEntity<PlatResponse> response = restTemplate.exchange(
                baseUrl + "?page=0&size=20&search=saumon",
                HttpMethod.GET,
                request,
                PlatResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Devrait trouver "Salade de Saumon aux Légumes" et "Mix Saumon-Pomme-Brocoli"
        assertEquals(2, response.getBody().content().size());
        assertTrue(response.getBody().content().stream()
                .anyMatch(plat -> plat.nom().contains("Saumon")));
    }

    @Test
    @Order(4)
    void pagination_shouldWorkCorrectly() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act - Première page avec 2 éléments
        ResponseEntity<PlatResponse> response = restTemplate.exchange(
                baseUrl + "?page=0&size=2&sort=id,asc",
                HttpMethod.GET,
                request,
                PlatResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().content().size());
        assertEquals(5, response.getBody().totalElements());
        assertEquals(3, response.getBody().totalPages());
        assertFalse(response.getBody().lastPage());
    }

    @Test
    @Order(5)
    void getById_shouldReturnPlatForTestUser() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // ID du premier plat de testuser (Bol Protéiné au Poulet)
        long platId = 1L;

        // Act
        ResponseEntity<PlatDTO> response = restTemplate.exchange(
                baseUrl + "/" + platId,
                HttpMethod.GET,
                request,
                PlatDTO.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bol Protéiné au Poulet", response.getBody().nom());
        assertEquals(3.0f, response.getBody().nbPortions());
        assertNotNull(response.getBody().recettes());
        assertEquals(3, response.getBody().recettes().size()); // Poulet + Carotte + Riz
    }

    @Test
    @Order(6)
    void getById_shouldReturn404WhenPlatNotFound() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        long nonExistentId = 99999L;

        // Act
        ResponseEntity<PlatDTO> response = restTemplate.exchange(
                baseUrl + "/" + nonExistentId,
                HttpMethod.GET,
                request,
                PlatDTO.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(7)
    void getById_shouldReturn403WhenAccessingOtherUserPlat() {
        // Arrange testuser essaie d'accéder à un plat de user2
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // ID du premier plat de user2 (Toast Complet Saumon-Avocat)
        long user2PlatId = 6L;

        // Act
        ResponseEntity<PlatDTO> response = restTemplate.exchange(
                baseUrl + "/" + user2PlatId,
                HttpMethod.GET,
                request,
                PlatDTO.class
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(8)
    void add_shouldCreatePlatWithAdminAndUserIngredients() {
        // Arrange - Utiliser des aliments de l'admin accessibles à testuser
        RecetteInputDTO recette1 = new RecetteInputDTO(4L, 150.0f); // Poulet Grillé (admin)
        RecetteInputDTO recette2 = new RecetteInputDTO(3L, 100.0f); // Riz Blanc (admin)
        RecetteInputDTO recette3 = new RecetteInputDTO(6L, 50.0f); // Carotte
        PlatInputDTO platDTO = new PlatInputDTO(3.0f, "Mon Riz gras", List.of(recette1, recette2, recette3));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PlatInputDTO> request = new HttpEntity<>(platDTO, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        // Act - Rechercher le plat créé
        ResponseEntity<PlatResponse> getResponse = restTemplate.exchange(
                baseUrl + "?page=0&size=20&search=Mon riz gras",
                HttpMethod.GET,
                getRequest,
                PlatResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertFalse(getResponse.getBody().content().isEmpty());

        PlatWithoutRecetteDTO createdPlat = getResponse.getBody().content().getFirst();
        assertEquals("Mon Riz gras", createdPlat.nom());
        assertEquals(132.666667f, createdPlat.calories());
    }

    @Test
    @Order(9)
    void add_shouldReturn404WhenIngredientNotFound() {
        // Arrange
        RecetteInputDTO recette = new RecetteInputDTO(99999L, 100.0f); // Aliment inexistant
        PlatInputDTO platDTO = new PlatInputDTO(1.0f, "Plat Impossible", List.of(recette));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PlatInputDTO> request = new HttpEntity<>(platDTO, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(10)
    void add_shouldReturn403WhenUsingInaccessibleIngredient() {
        // Arrange - testuser essaie d'utiliser un aliment de user2
        RecetteInputDTO recette = new RecetteInputDTO(9L, 100.0f); // Avocat (appartient à user2)
        PlatInputDTO platDTO = new PlatInputDTO(1.0f, "Plat Interdit", List.of(recette));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PlatInputDTO> request = new HttpEntity<>(platDTO, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(11)
    void add_shouldAggregateRecettesWhenSameIngredient() {
        // Arrange - Ajouter le même ingrédient plusieurs fois
        RecetteInputDTO recette1 = new RecetteInputDTO(6L, 100.0f); // Carotte 100g
        RecetteInputDTO recette2 = new RecetteInputDTO(6L, 50.0f);  // Carotte 50g
        PlatInputDTO platDTO = new PlatInputDTO(1.0f, "Carotte Doublée", List.of(recette1, recette2));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PlatInputDTO> request = new HttpEntity<>(platDTO, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Vérifier en récupérant le plat créé
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<PlatResponse> getResponse = restTemplate.exchange(
                baseUrl + "?search=Carotte Doublée",
                HttpMethod.GET,
                getRequest,
                PlatResponse.class
        );

        assertNotNull(getResponse.getBody());
        assertFalse(getResponse.getBody().content().isEmpty());

        PlatWithoutRecetteDTO createdPlat = getResponse.getBody().content().getFirst();
        assertEquals("Carotte Doublée", createdPlat.nom());
        assertEquals(61.5f, createdPlat.calories());
    }

    @Test
    @Order(12)
    void update_shouldUpdatePlatDataOnly() {
        // Arrange - Modifier un plat existant avec les mêmes recettes
        RecetteInputDTO recette1 = new RecetteInputDTO(4L, 100.0f); // Poulet (même quantité qu'avant)
        RecetteInputDTO recette2 = new RecetteInputDTO(6L, 100.0f); // Carotte
        RecetteInputDTO recette3 = new RecetteInputDTO(3L, 50.0f);  // Riz
        PlatInputDTO platDTO = new PlatInputDTO(4.0f, "Bol Protéiné Modifié", List.of(recette1, recette2, recette3));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PlatInputDTO> request = new HttpEntity<>(platDTO, headers);

        // ID du premier plat de testuser
        long platId = 1L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + platId,
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Vérifier que les modifications ont été appliquées
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<PlatDTO> getResponse = restTemplate.exchange(
                baseUrl + "/" + platId,
                HttpMethod.GET,
                getRequest,
                PlatDTO.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Bol Protéiné Modifié", getResponse.getBody().nom());
        assertEquals(4.0f, getResponse.getBody().nbPortions());
        assertEquals(67.75f, getResponse.getBody().calories());
    }

    @Test
    @Order(13)
    void update_shouldUpdateWithNewRecettes() {
        // Arrange - Modifier un plat avec des recettes complètement différentes
        RecetteInputDTO recette1 = new RecetteInputDTO(5L, 100.0f); // Saumon (nouveau)
        RecetteInputDTO recette2 = new RecetteInputDTO(7L, 150.0f); // Brocoli (nouveau)
        PlatInputDTO platDTO = new PlatInputDTO(2.0f, "Saumon-Brocoli", List.of(recette1, recette2));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PlatInputDTO> request = new HttpEntity<>(platDTO, headers);

        // ID du deuxième plat de testuser
        long platId = 2L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + platId,
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Vérifier les modifications
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<PlatDTO> getResponse = restTemplate.exchange(
                baseUrl + "/" + platId,
                HttpMethod.GET,
                getRequest,
                PlatDTO.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Saumon-Brocoli", getResponse.getBody().nom());
        assertEquals(2, getResponse.getBody().recettes().size());
        assertEquals(129.5f, getResponse.getBody().calories());
    }

    @Test
    @Order(14)
    void update_shouldReturn404WhenPlatNotFound() {
        // Arrange
        PlatInputDTO platDTO = new PlatInputDTO(1.0f, "Plat Inexistant", List.of());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PlatInputDTO> request = new HttpEntity<>(platDTO, headers);

        long nonExistentId = 99999L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + nonExistentId,
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(15)
    void update_shouldReturn403WhenUpdatingOtherUserPlat() {
        // Arrange testuser essaie de modifier un plat de user2
        PlatInputDTO platDTO = new PlatInputDTO(1.0f, "Plat Modifié", List.of());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PlatInputDTO> request = new HttpEntity<>(platDTO, headers);

        // ID du premier plat de user2
        long user2PlatId = 6L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + user2PlatId,
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(16)
    void delete_shouldDeletePlatForTestUser() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);

        // Récupérer le nombre de plats avant suppression
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<PlatResponse> beforeResponse = restTemplate.exchange(
                baseUrl + "?page=0&size=50",
                HttpMethod.GET,
                getRequest,
                PlatResponse.class
        );

        assertNotNull(beforeResponse.getBody());
        int countBefore = beforeResponse.getBody().content().size();

        // ID du troisième plat de testuser
        long platId = 3L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + platId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Vérifier que le plat a été supprimé
        ResponseEntity<PlatResponse> afterResponse = restTemplate.exchange(
                baseUrl + "?page=0&size=50",
                HttpMethod.GET,
                getRequest,
                PlatResponse.class
        );

        assertNotNull(afterResponse.getBody());
        int countAfter = afterResponse.getBody().content().size();
        assertEquals(countBefore - 1, countAfter);
    }

    @Test
    @Order(17)
    void delete_shouldReturn404WhenPlatNotFound() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);

        long nonExistentId = 99999L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + nonExistentId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(18)
    void delete_shouldReturn403WhenDeletingOtherUserPlat() {
        // Arrange testuser essaie de supprimer un plat de user2
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);

        // ID du deuxième plat de user2
        long user2PlatId = 7L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + user2PlatId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(19)
    void getAllForUser_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        // Pas de token JWT
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<PlatResponse> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                request,
                PlatResponse.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(20)
    void add_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        PlatInputDTO platDTO = new PlatInputDTO(1.0f, "Test Sans Auth", List.of());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Pas de token JWT
        HttpEntity<PlatInputDTO> request = new HttpEntity<>(platDTO, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(21)
    void update_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        PlatInputDTO platDTO = new PlatInputDTO(1.0f, "Test Sans Auth", List.of());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Pas de token JWT
        HttpEntity<PlatInputDTO> request = new HttpEntity<>(platDTO, headers);

        long platId = 1L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + platId,
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(22)
    void delete_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        // Pas de token JWT
        HttpEntity<Void> request = new HttpEntity<>(headers);

        long platId = 1L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + platId,
                HttpMethod.DELETE,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}