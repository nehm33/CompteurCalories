package com.platydev.compteurcalories.controller;

import com.platydev.compteurcalories.dto.input.LoginInput;
import com.platydev.compteurcalories.dto.output.AlimentDTO;
import com.platydev.compteurcalories.dto.output.AlimentResponse;
import com.platydev.compteurcalories.dto.output.LoginOutput;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AlimentControllerIntegrationTests {

    private final TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;
    private String jwtTokenTestUser;
    private String jwtTokenAdmin;
    private String jwtTokenUser2;

    @Autowired
    public AlimentControllerIntegrationTests(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/aliments";
        
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
    void getAllForUser_shouldReturnUserAndAdminAliments() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<AlimentResponse> response = restTemplate.exchange(
                baseUrl + "?page=0&size=20&sort=nom,asc",
                HttpMethod.GET,
                request,
                AlimentResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().content());
        
        // Vérifier que testuser voit ses 3 aliments + 6 aliments de l'admin = 8 total
        assertEquals(9, response.getBody().content().size());
        
        // Vérifier quelques noms des aliments (triés par nom)
        assertTrue(response.getBody().content().stream()
                .anyMatch(aliment -> "Brocoli".equals(aliment.nom())));
        assertTrue(response.getBody().content().stream()
                .anyMatch(aliment -> "Carotte".equals(aliment.nom())));
        assertTrue(response.getBody().content().stream()
                .anyMatch(aliment -> "Pomme Rouge".equals(aliment.nom()))); // De l'admin
    }

    @Test
    @Order(2)
    void getAllForAdmin_shouldReturnAllAdminAliments() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenAdmin);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<AlimentResponse> response = restTemplate.exchange(
                baseUrl + "?page=0&size=20&sort=nom,asc",
                HttpMethod.GET,
                request,
                AlimentResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().content());
        
        // L'admin voit ses 6 aliments
        assertEquals(6, response.getBody().content().size());
    }

    @Test
    @Order(3)
    void getAllForUser2_shouldReturnUser2AndAdminAliments() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenUser2);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<AlimentResponse> response = restTemplate.exchange(
                baseUrl + "?page=0&size=20&sort=nom,asc",
                HttpMethod.GET,
                request,
                AlimentResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().content());
        
        // Vérifier que user2 voit ses 2 aliments + 6 aliments de l'admin = 8 total
        assertEquals(8, response.getBody().content().size());
        
        // Vérifier la présence d'aliments de user2 et de l'admin
        assertTrue(response.getBody().content().stream()
                .anyMatch(aliment -> "Avocat".equals(aliment.nom())));
        assertTrue(response.getBody().content().stream()
                .anyMatch(aliment -> "Pain Complet".equals(aliment.nom())));
        assertTrue(response.getBody().content().stream()
                .anyMatch(aliment -> "Pomme Rouge".equals(aliment.nom()))); // De l'admin
    }

    @Test
    @Order(6)
    void add_shouldCreateAlimentForTestUser() {
        // Arrange
        AlimentDTO alimentDTO = AlimentDTO.builder()
                .nom("Pomme Verte Test")
                .calories(52.0f)
                .unite("g")
                .matieresGrasses(0.2f)
                .matieresGrassesSatures(0.03f)
                .matieresGrassesMonoInsaturees(0.01f)
                .matieresGrassesPolyInsaturees(0.01f)
                .matieresGrassesTrans(0.0f)
                .proteines(0.3f)
                .glucides(14.0f)
                .sucre(10.0f)
                .fibres(2.4f)
                .sel(0.0f)
                .cholesterol(0.0f)
                .provitamineA(54.0f)
                .vitamineA(0.017f)
                .vitamineB1(0.026f)
                .vitamineB2(0.091f)
                .vitamineB3(0.061f)
                .vitamineB5(0.041f)
                .vitamineB6(0.005f)
                .vitamineB8(0.0f)
                .vitamineB9(0.0f)
                .vitamineB11(0.0f)
                .vitamineB12(0.0f)
                .vitamineC(4.6f)
                .vitamineD(0.0f)
                .vitamineE(0.18f)
                .vitamineK1(2.2f)
                .vitamineK2(0.0f)
                .Ars(0.0f)
                .B(0.0f)
                .Ca(6.0f)
                .Cl(0.0f)
                .choline(0.0f)
                .Cr(0.0f)
                .Co(0.0f)
                .Cu(0.027f)
                .Fe(0.12f)
                .F(0.0f)
                .I(0.0f)
                .Mg(5.0f)
                .Mn(0.035f)
                .Mo(0.0f)
                .Na(1.0f)
                .P(11.0f)
                .K(107.0f)
                .Rb(0.0f)
                .SiO(0.0f)
                .S(0.0f)
                .Se(0.0f)
                .V(0.0f)
                .Sn(0.0f)
                .Zn(0.04f)
                .build();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AlimentDTO> request = new HttpEntity<>(alimentDTO, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        // Vérifier que l'aliment a été ajouté en consultant la liste
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);
        
        ResponseEntity<AlimentResponse> getResponse = restTemplate.exchange(
                baseUrl + "?page=0&size=20&sort=nom,asc",
                HttpMethod.GET,
                getRequest,
                AlimentResponse.class
        );
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        // Maintenant testuser devrait avoir 9 aliments (3 siens + 6 admin + 1 nouveau).
        assertEquals(10, getResponse.getBody().content().size());
        
        // Vérifier que le nouvel aliment est présent
        assertTrue(getResponse.getBody().content().stream()
                .anyMatch(aliment -> "Pomme Verte Test".equals(aliment.nom())));
    }

    @Test
    @Order(7)
    void add_shouldCreateAlimentWithoutCodeBarreForAdmin() {
        // Arrange
        AlimentDTO alimentDTO = AlimentDTO.builder()
                .nom("Épinards Frais")
                .calories(23.0f)
                .unite("g")
                .matieresGrasses(0.4f)
                .matieresGrassesSatures(0.06f)
                .matieresGrassesMonoInsaturees(0.01f)
                .matieresGrassesPolyInsaturees(0.17f)
                .matieresGrassesTrans(0.0f)
                .proteines(2.9f)
                .glucides(3.6f)
                .sucre(0.4f)
                .fibres(2.2f)
                .sel(0.079f)
                .cholesterol(0.0f)
                .provitamineA(469.0f)
                .vitamineA(0.047f)
                .vitamineB1(0.078f)
                .vitamineB2(0.189f)
                .vitamineB3(0.724f)
                .vitamineB5(0.065f)
                .vitamineB6(0.195f)
                .vitamineB8(0.0f)
                .vitamineB9(194.0f)
                .vitamineB11(0.0f)
                .vitamineB12(0.0f)
                .vitamineC(28.1f)
                .vitamineD(0.0f)
                .vitamineE(2.03f)
                .vitamineK1(482.9f)
                .vitamineK2(0.0f)
                .Ars(0.0f)
                .B(0.0f)
                .Ca(99.0f)
                .Cl(0.0f)
                .choline(19.3f)
                .Cr(0.0f)
                .Co(0.0f)
                .Cu(0.13f)
                .Fe(2.71f)
                .F(0.0f)
                .I(0.0f)
                .Mg(79.0f)
                .Mn(0.897f)
                .Mo(0.0f)
                .Na(79.0f)
                .P(49.0f)
                .K(558.0f)
                .Rb(0.0f)
                .SiO(0.0f)
                .S(0.0f)
                .Se(0.0f)
                .V(0.0f)
                .Sn(0.0f)
                .Zn(0.53f)
                .build();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AlimentDTO> request = new HttpEntity<>(alimentDTO, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(4)
    void searchByNom_shouldReturnFilteredResults() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act - Rechercher les aliments contenant "ro" (Brocoli et Carotte)
        ResponseEntity<AlimentResponse> response = restTemplate.exchange(
                baseUrl + "?page=0&size=20&sort=nom,asc&search=ro",
                HttpMethod.GET,
                request,
                AlimentResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Devrait trouver Brocoli et Carotte (de testuser)
        assertTrue(response.getBody().content().size() >= 2);
        assertTrue(response.getBody().content().stream()
                .anyMatch(aliment -> "Brocoli".equals(aliment.nom())));
        assertTrue(response.getBody().content().stream()
                .anyMatch(aliment -> "Carotte".equals(aliment.nom())));
    }

    @Test
    @Order(5)
    void pagination_shouldWorkCorrectly() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenAdmin);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act - Première page avec 2 éléments
        ResponseEntity<AlimentResponse> response = restTemplate.exchange(
                baseUrl + "?page=0&size=2&sort=nom,asc",
                HttpMethod.GET,
                request,
                AlimentResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().content().size());
        assertEquals(6, response.getBody().totalElements());
        assertEquals(3, response.getBody().totalPages());
        assertFalse(response.getBody().lastPage());
    }

    @Test
    @Order(8)
    void getById_shouldReturnAlimentForTestUser() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Supposons que l'aliment "Brocoli" de testuser a l'ID 8
        long alimentId = 7L;

        // Act
        ResponseEntity<AlimentDTO> response = restTemplate.exchange(
                baseUrl + "/" + alimentId,
                HttpMethod.GET,
                request,
                AlimentDTO.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Brocoli", response.getBody().nom());
        assertNotNull(response.getBody().calories());
        assertEquals("g", response.getBody().unite());
    }

    @Test
    @Order(9)
    void getById_shouldReturnAdminAlimentForUser() {
        // Arrange - Un utilisateur peut voir les aliments de l'admin
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Supposons que l'aliment "Pomme Rouge" de l'admin a l'ID 1
        long adminAlimentId = 1L;

        // Act
        ResponseEntity<AlimentDTO> response = restTemplate.exchange(
                baseUrl + "/" + adminAlimentId,
                HttpMethod.GET,
                request,
                AlimentDTO.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pomme Rouge", response.getBody().nom());
        assertNotNull(response.getBody().calories());
    }

    @Test
    @Order(10)
    void getById_shouldReturnAlimentForAdmin() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenAdmin);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Supposons que l'aliment "Pomme Rouge" de l'admin a l'ID 1
        long alimentId = 1L;

        // Act
        ResponseEntity<AlimentDTO> response = restTemplate.exchange(
                baseUrl + "/" + alimentId,
                HttpMethod.GET,
                request,
                AlimentDTO.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pomme Rouge", response.getBody().nom());
        assertNotNull(response.getBody().calories());
        assertEquals("g", response.getBody().unite());
    }

    @Test
    @Order(11)
    void getById_shouldReturn404WhenAlimentNotFound() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        long nonExistentId = 99999L;

        // Act
        ResponseEntity<AlimentDTO> response = restTemplate.exchange(
                baseUrl + "/" + nonExistentId,
                HttpMethod.GET,
                request,
                AlimentDTO.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(12)
    void getById_shouldReturn403WhenAccessingOtherUserAliment() {
        // Arrange testuser essaie d'accéder à un aliment de user2
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Supposons que l'aliment "Avocat" de user2 a l'ID 6
        long user2AlimentId = 9L;

        // Act
        ResponseEntity<AlimentDTO> response = restTemplate.exchange(
                baseUrl + "/" + user2AlimentId,
                HttpMethod.GET,
                request,
                AlimentDTO.class
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(13)
    void getById_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        // Pas de token JWT
        HttpEntity<Void> request = new HttpEntity<>(headers);

        long alimentId = 1L;

        // Act
        ResponseEntity<AlimentDTO> response = restTemplate.exchange(
                baseUrl + "/" + alimentId,
                HttpMethod.GET,
                request,
                AlimentDTO.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(14)
    void update_shouldUpdateAlimentForTestUser() {
        // Arrange - D'abord créer un aliment à modifier
        AlimentDTO alimentDTO = AlimentDTO.builder()
                .nom("Pomme à Modifier")
                .calories(50.0f)
                .unite("g")
                .matieresGrasses(0.1f)
                .proteines(0.2f)
                .glucides(13.0f)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AlimentDTO> createRequest = new HttpEntity<>(alimentDTO, headers);

        // Créer l'aliment
        ResponseEntity<Void> createResponse = restTemplate.postForEntity(
                baseUrl,
                createRequest,
                Void.class
        );
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        // Récupérer l'ID de l'aliment créé (en supposant qu'on puisse le récupérer par recherche)
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<AlimentResponse> getResponse = restTemplate.exchange(
                baseUrl + "?search=Pomme à Modifier",
                HttpMethod.GET,
                getRequest,
                AlimentResponse.class
        );

        assertNotNull(getResponse.getBody());
        assertFalse(getResponse.getBody().content().isEmpty());

        long alimentId = getResponse.getBody().content().getFirst().id();

        // Préparer les données de mise à jour
        AlimentDTO alimentDTOUpdate = AlimentDTO.builder()
                .nom("Pomme Modifiée")
                .calories(55.0f)
                .unite("g")
                .matieresGrasses(0.15f)
                .proteines(0.3f)
                .glucides(14.0f)
                .sucre(12.0f)
                .fibres(2.0f)
                .build();

        HttpEntity<AlimentDTO> updateRequest = new HttpEntity<>(alimentDTOUpdate, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + alimentId,
                HttpMethod.PUT,
                updateRequest,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Vérifier que l'aliment a été modifié
        ResponseEntity<AlimentResponse> verifyResponse = restTemplate.exchange(
                baseUrl + "?search=Pomme Modifiée",
                HttpMethod.GET,
                getRequest,
                AlimentResponse.class
        );

        assertEquals(HttpStatus.OK, verifyResponse.getStatusCode());
        assertNotNull(verifyResponse.getBody());
        assertTrue(verifyResponse.getBody().content().stream()
                .anyMatch(aliment -> "Pomme Modifiée".equals(aliment.nom()) &&
                        aliment.calories().equals(55.0f)));
    }

    @Test
    @Order(15)
    void update_shouldUpdateAlimentForAdmin() {
        // Arrange
        AlimentDTO alimentDTOUpdate = AlimentDTO.builder()
                .nom("Pomme Rouge Modifiée")
                .calories(60.0f)
                .unite("g")
                .matieresGrasses(0.2f)
                .proteines(0.4f)
                .glucides(15.0f)
                .sucre(13.0f)
                .fibres(2.5f)
                .sel(0.001f)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AlimentDTO> request = new HttpEntity<>(alimentDTOUpdate, headers);

        // Supposons que l'aliment "Pomme Rouge" de l'admin a l'ID 1
        long alimentId = 1L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + alimentId,
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(16)
    void update_shouldReturn404WhenAlimentNotFound() {
        // Arrange
        AlimentDTO alimentDTO = AlimentDTO.builder()
                .nom("Aliment Inexistant")
                .calories(100.0f)
                .unite("g")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AlimentDTO> request = new HttpEntity<>(alimentDTO, headers);

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
    @Order(17)
    void update_shouldReturn403WhenUpdatingOtherUserAliment() {
        // Arrange testuser essaie de modifier un aliment de user2
        AlimentDTO alimentDTO = AlimentDTO.builder()
                .nom("Avocat Modifié")
                .calories(200.0f)
                .unite("g")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AlimentDTO> request = new HttpEntity<>(alimentDTO, headers);

        // Supposons que l'aliment "Avocat" de user2 a l'ID 6
        long user2AlimentId = 9L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + user2AlimentId,
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(18)
    void delete_shouldDeleteAlimentForTestUser() {
        // Arrange - D'abord créer un aliment à supprimer
        AlimentDTO alimentDTO = AlimentDTO.builder()
                .nom("Aliment à Supprimer")
                .calories(100.0f)
                .unite("g")
                .matieresGrasses(1.0f)
                .proteines(2.0f)
                .glucides(20.0f)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AlimentDTO> createRequest = new HttpEntity<>(alimentDTO, headers);

        // Créer l'aliment
        ResponseEntity<Void> createResponse = restTemplate.postForEntity(
                baseUrl,
                createRequest,
                Void.class
        );
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        // Récupérer le nombre d'aliments avant suppression
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setBearerAuth(jwtTokenTestUser);
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<AlimentResponse> beforeResponse = restTemplate.exchange(
                baseUrl + "?page=0&size=50",
                HttpMethod.GET,
                getRequest,
                AlimentResponse.class
        );

        assertNotNull(beforeResponse.getBody());
        int countBefore = beforeResponse.getBody().content().size();

        // Supposons que l'aliment créé a l'ID 101
        long alimentId = 22L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + alimentId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Vérifier que l'aliment a été supprimé
        ResponseEntity<AlimentResponse> afterResponse = restTemplate.exchange(
                baseUrl + "?page=0&size=50",
                HttpMethod.GET,
                getRequest,
                AlimentResponse.class
        );

        assertNotNull(afterResponse.getBody());
        int countAfter = afterResponse.getBody().content().size();
        assertEquals(countBefore - 1, countAfter);

        // Vérifier que l'aliment spécifique n'existe plus
        assertFalse(afterResponse.getBody().content().stream()
                .anyMatch(aliment -> "Aliment à Supprimer".equals(aliment.nom())));
    }

    @Test
    @Order(19)
    void delete_shouldDeleteAlimentForAdmin() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenAdmin);

        // Récupérer le nombre d'aliments avant suppression
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<AlimentResponse> beforeResponse = restTemplate.exchange(
                baseUrl + "?page=0&size=50",
                HttpMethod.GET,
                getRequest,
                AlimentResponse.class
        );

        assertNotNull(beforeResponse.getBody());
        int countBefore = beforeResponse.getBody().content().size();

        // Supposons que l'admin supprime l'aliment "Banane à supprimer" avec l'ID 2
        long alimentId = 11L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + alimentId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Vérifier que l'aliment a été supprimé
        ResponseEntity<AlimentResponse> afterResponse = restTemplate.exchange(
                baseUrl + "?page=0&size=50",
                HttpMethod.GET,
                getRequest,
                AlimentResponse.class
        );

        assertNotNull(afterResponse.getBody());
        int countAfter = afterResponse.getBody().content().size();
        assertEquals(countBefore - 1, countAfter);
    }

    @Test
    @Order(20)
    void delete_shouldReturn404WhenAlimentNotFound() {
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
    @Order(21)
    void delete_shouldReturn403WhenDeletingOtherUserAliment() {
        // Arrange testuser essaie de supprimer un aliment de user2
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenTestUser);

        // Supposons que l'aliment "Pain Complet" de user2 a l'ID 7
        long user2AlimentId = 10L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + user2AlimentId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(22)
    void update_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        AlimentDTO alimentDTO = AlimentDTO.builder()
                .nom("Test Sans Auth")
                .calories(100.0f)
                .unite("g")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Pas de token JWT
        HttpEntity<AlimentDTO> request = new HttpEntity<>(alimentDTO, headers);

        long alimentId = 1L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + alimentId,
                HttpMethod.PUT,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(23)
    void delete_shouldReturn401WhenNotAuthenticated() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        // Pas de token JWT
        HttpEntity<Void> request = new HttpEntity<>(headers);

        long alimentId = 1L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + alimentId,
                HttpMethod.DELETE,
                request,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}