package com.platydev.compteurcalories;

import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.Plat;
import com.platydev.compteurcalories.entity.Recette;
import com.platydev.compteurcalories.entity.RecetteId;
import com.platydev.compteurcalories.repository.RecetteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecetteRepositoryTest {

    @Autowired
    private RecetteRepository recetteRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindAllByRecetteId_PlatId() {
        Aliment pommeDeTerre = new Aliment("pomme de terre", 93.2, "g", 1.37, 0, 0, 0, 0, 2.01, 17.2, 1.05, 1.96, 0, 0, 3.68, 0, 0.07, 0.013, 1.08, 0.46, 0.24, 0, 18.4, 0, 0.03, 5.05, 0, 0.12, 1.34, 0,0, 0, 9.62, 150, 0, 0, 0, 0.095, 0.43, 0, 6.78, 20.7, 0.12, 0, 48.8, 43.7, 450, 0, 0, 0, 7.07, 0, 0, 0.24);
        Aliment huileOlive = new Aliment("huile d'olive", 900, "g", 100, 14, 75.2, 6.9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 47.8, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0.6, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0);
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        pommeDeTerre.setId(3);
        huileOlive.setId(4);
        pommeTerreSautees.setId(1);

        Recette recette1 = new Recette(pommeTerreSautees, pommeDeTerre, 500);
        Recette recette2 = new Recette(pommeTerreSautees, huileOlive, 100);

        List<Recette> recetteList = recetteRepository.findAllByRecetteId_PlatId(1);

        assertNotNull(recetteList);
        assertFalse(recetteList.isEmpty());
        assertEquals(2, recetteList.size());
        assertThat(recetteList).contains(recette1, recette2);
    }

    @Test
    public void testFindBy() {
        Aliment pommeDeTerre = new Aliment("pomme de terre", 93.2, "g", 1.37, 0, 0, 0, 0, 2.01, 17.2, 1.05, 1.96, 0, 0, 3.68, 0, 0.07, 0.013, 1.08, 0.46, 0.24, 0, 18.4, 0, 0.03, 5.05, 0, 0.12, 1.34, 0,0, 0, 9.62, 150, 0, 0, 0, 0.095, 0.43, 0, 6.78, 20.7, 0.12, 0, 48.8, 43.7, 450, 0, 0, 0, 7.07, 0, 0, 0.24);
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        pommeDeTerre.setId(3);
        pommeTerreSautees.setId(1);

        Recette recette = new Recette(pommeTerreSautees, pommeDeTerre, 500);

        Recette recetteDB = recetteRepository.findById(recette.getRecetteId()).orElse(null);

        assertEquals(recette, recetteDB);
    }

    @Test
    public void testCreate() {
        Aliment blancPoulet = new Aliment("blanc de poulet", 121, "g", 1.8, 0.6, 0.7, 0.4, 0, 26.2, 0, 0, 0, 0, 70.4, 0, 4.7, 0.2, 0.1, 11.1, 1.4, 0.5, 0, 8, 0, 0.3, 2.6, 0.3, 0.3, 0.3, 0, 0, 0, 13.6, 0, 0, 0, 0, 0, 0.4, 0, 5, 26.5, 0, 0, 415, 480, 290, 0, 0, 0, 11, 0, 0, 0.8);
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        blancPoulet.setId(5);
        pommeTerreSautees.setId(1);

        Recette recette = new Recette(pommeTerreSautees, blancPoulet, 100);

        recetteRepository.save(recette);

        Recette recetteDB = entityManager.find(Recette.class, recette.getRecetteId());

        assertEquals(recette, recetteDB);
    }

    @Test
    public void testUpdate() {
        Aliment pommeDeTerre = new Aliment("pomme de terre", 93.2, "g", 1.37, 0, 0, 0, 0, 2.01, 17.2, 1.05, 1.96, 0, 0, 3.68, 0, 0.07, 0.013, 1.08, 0.46, 0.24, 0, 18.4, 0, 0.03, 5.05, 0, 0.12, 1.34, 0,0, 0, 9.62, 150, 0, 0, 0, 0.095, 0.43, 0, 6.78, 20.7, 0.12, 0, 48.8, 43.7, 450, 0, 0, 0, 7.07, 0, 0, 0.24);
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        pommeDeTerre.setId(3);
        pommeTerreSautees.setId(1);

        Recette recette = new Recette(pommeTerreSautees, pommeDeTerre, 500);

        Recette recetteDB = entityManager.find(Recette.class, recette.getRecetteId());

        recette.setQuantite(300);

        recetteRepository.save(recette);

        recetteDB = entityManager.find(Recette.class, recette.getRecetteId());

        assertEquals(recette, recetteDB);
    }

    @Test
    public void testDelete() {
        Aliment pommeDeTerre = new Aliment("pomme de terre", 93.2, "g", 1.37, 0, 0, 0, 0, 2.01, 17.2, 1.05, 1.96, 0, 0, 3.68, 0, 0.07, 0.013, 1.08, 0.46, 0.24, 0, 18.4, 0, 0.03, 5.05, 0, 0.12, 1.34, 0,0, 0, 9.62, 150, 0, 0, 0, 0.095, 0.43, 0, 6.78, 20.7, 0.12, 0, 48.8, 43.7, 450, 0, 0, 0, 7.07, 0, 0, 0.24);
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        pommeDeTerre.setId(3);
        pommeTerreSautees.setId(1);

        Recette recette = new Recette(pommeTerreSautees, pommeDeTerre, 500);

        recetteRepository.deleteById(new RecetteId(1, 4));

        List<Recette> recetteList = recetteRepository.findAllByRecetteId_PlatId(1);

        assertNotNull(recetteList);
        assertFalse(recetteList.isEmpty());
        assertEquals(1, recetteList.size());
        assertThat(recetteList).contains(recette);
    }
}
