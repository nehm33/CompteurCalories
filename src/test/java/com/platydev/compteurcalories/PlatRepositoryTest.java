package com.platydev.compteurcalories;

import com.platydev.compteurcalories.entity.Plat;
import com.platydev.compteurcalories.repository.PlatRepository;
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
public class PlatRepositoryTest {

    @Autowired
    private PlatRepository platRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindAll() {
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        Plat pateTomate = new Plat("pâte tomate", 2);
        Plat pouletGrille = new Plat("poulet grillé", 1);
        Plat pommeTerreSautees2 = new Plat("pommes de terre sautées 2", 1);

        List<Plat> platList = platRepository.findAll();

        assertNotNull(platList);
        assertFalse(platList.isEmpty());
        assertEquals(4, platList.size());
        assertThat(platList).contains(pommeTerreSautees, pateTomate, pouletGrille, pommeTerreSautees2);
    }

    @Test
    public void testFindBy() {
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);

        Plat plat = platRepository.findById(1L).orElse(null);

        assertEquals(pommeTerreSautees, plat);
    }

    @Test
    public void testCreate() {
        Plat plat = new Plat("roejgti", 4);

        platRepository.save(plat);

        Plat platDB = entityManager.find(Plat.class, plat.getId());

        assertEquals(plat, platDB);
    }

    @Test
    public void testUpdate() {
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        pommeTerreSautees.setId(1);

        Plat platDB = entityManager.find(Plat.class, 1);

        assertEquals(pommeTerreSautees, platDB);

        pommeTerreSautees.setNom("pote de terre");
        pommeTerreSautees.setNbPortions(5);

        platRepository.save(pommeTerreSautees);

        platDB = entityManager.find(Plat.class, 1);

        assertEquals(pommeTerreSautees, platDB);
    }

    @Test
    public void testDelete() {
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        Plat pateTomate = new Plat("pâte tomate", 2);
        Plat pouletGrille = new Plat("poulet grillé", 1);

        platRepository.deleteById(4L);

        List<Plat> platList = platRepository.findAll();

        assertNotNull(platList);
        assertFalse(platList.isEmpty());
        assertEquals(3, platList.size());
        assertThat(platList).contains(pommeTerreSautees, pateTomate, pouletGrille);
    }

    @Test
    public void testFindByNomLike() {
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        Plat pateTomate = new Plat("pâte tomate", 2);
        Plat pommeTerreSautees2 = new Plat("pommes de terre sautées 2", 1);

        List<Plat> platList = platRepository.findByNomLike("%om%");

        assertNotNull(platList);
        assertFalse(platList.isEmpty());
        assertEquals(3, platList.size());
        assertThat(platList).contains(pommeTerreSautees, pateTomate, pommeTerreSautees2);
    }
}
