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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "classpath:sql/init_test_data.sql", config = @SqlConfig(encoding = "utf-8"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AlimentControllerIntegrationTest {

    private final TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;
    private String jwtTokenTestUser;
    private String jwtTokenAdmin;
    private String jwtTokenUser2;

    @Autowired
    public AlimentControllerIntegrationTest(TestRestTemplate restTemplate) {
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
                baseUrl + "?pageNumber=0&pageSize=20&sortBy=nom&sortOrder=asc",
                HttpMethod.GET,
                request,
                AlimentResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().content());
        
        // Vérifier que testuser voit ses 3 aliments + 5 aliments de l'admin = 8 total
        assertEquals(8, response.getBody().content().size());
        
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
                baseUrl + "?pageNumber=0&pageSize=20&sortBy=nom&sortOrder=asc",
                HttpMethod.GET,
                request,
                AlimentResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().content());
        
        // L'admin voit ses 5 aliments + ses propres aliments en tant qu'admin = 5 aliments
        assertEquals(5, response.getBody().content().size());
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
                baseUrl + "?pageNumber=0&pageSize=20&sortBy=nom&sortOrder=asc",
                HttpMethod.GET,
                request,
                AlimentResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().content());
        
        // Vérifier que user2 voit ses 2 aliments + 5 aliments de l'admin = 7 total
        assertEquals(7, response.getBody().content().size());
        
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
                baseUrl + "?pageNumber=0&pageSize=20&sortBy=nom&sortOrder=asc",
                HttpMethod.GET,
                getRequest,
                AlimentResponse.class
        );
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        // Maintenant testuser devrait avoir 9 aliments (3 siens + 5 admin + 1 nouveau)
        assertEquals(9, getResponse.getBody().content().size());
        
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
                baseUrl + "?pageNumber=0&pageSize=10&sortBy=nom&sortOrder=asc&search=ro",
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
        assertEquals(5, response.getBody().totalElements());
        assertEquals(3, response.getBody().totalPages());
        assertFalse(response.getBody().lastPage());
    }
}