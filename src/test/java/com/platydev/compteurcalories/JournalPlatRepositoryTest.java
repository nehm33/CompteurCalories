package com.platydev.compteurcalories;

import com.platydev.compteurcalories.entity.JournalPlat;
import com.platydev.compteurcalories.entity.JournalPlatId;
import com.platydev.compteurcalories.entity.Plat;
import com.platydev.compteurcalories.repository.JournalPlatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JournalPlatRepositoryTest {
    
    @Autowired
    private JournalPlatRepository journalPlatRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    public void testFindAllByJournalPlatId_Date() {
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        Plat pateTomate = new Plat("pâte tomate", 2);
        pommeTerreSautees.setId(1);
        pateTomate.setId(2);
        Date date = Date.valueOf("2023-12-22");

        JournalPlat journalPlat1 = new JournalPlat(date, pommeTerreSautees, 1);
        JournalPlat journalPlat2 = new JournalPlat(date, pateTomate, 1);

        List<JournalPlat> journalPlatList = journalPlatRepository.findAllByJournalPlatId_Date(date);
        
        assertNotNull(journalPlatList);
        assertFalse(journalPlatList.isEmpty());
        assertEquals(2, journalPlatList.size());
        assertThat(journalPlatList).contains(journalPlat1, journalPlat2);
    }

    @Test
    public void testFindBy() {
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        pommeTerreSautees.setId(1);
        Date date = Date.valueOf("2023-12-22");

        JournalPlat journalPlat = new JournalPlat(date, pommeTerreSautees, 1);

        JournalPlat journalPlatDB = journalPlatRepository.findById(journalPlat.getJournalPlatId()).orElse(null);

        assertEquals(journalPlat, journalPlatDB);
    }

    @Test
    public void testCreate() {
        Plat pouletGrille = new Plat("poulet grillé", 1);
        pouletGrille.setId(3);
        Date date = Date.valueOf("2023-12-22");

        JournalPlat journalPlat = new JournalPlat(date, pouletGrille, 1);

        journalPlatRepository.save(journalPlat);

        JournalPlat journalPlatDB = entityManager.find(JournalPlat.class, journalPlat.getJournalPlatId());

        assertEquals(journalPlat, journalPlatDB);
    }

    @Test
    public void testUpdate() {
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        pommeTerreSautees.setId(1);
        Date date = Date.valueOf("2023-12-22");

        JournalPlat journalPlat = new JournalPlat(date, pommeTerreSautees, 1);

        JournalPlat journalPlatDB = entityManager.find(JournalPlat.class, journalPlat.getJournalPlatId());

        assertEquals(journalPlat, journalPlatDB);

        journalPlat.setPortions(9);

        journalPlatRepository.save(journalPlat);

        journalPlatDB = entityManager.find(JournalPlat.class, journalPlat.getJournalPlatId());

        assertEquals(journalPlat, journalPlatDB);
    }

    @Test
    public void testDelete() {
        Plat pommeTerreSautees = new Plat("pommes de terre sautées", 1);
        pommeTerreSautees.setId(1);
        Date date = Date.valueOf("2023-12-22");

        JournalPlat journalPlat1 = new JournalPlat(date, pommeTerreSautees, 1);

        journalPlatRepository.deleteById(new JournalPlatId(2, date));

        List<JournalPlat> journalPlatList = journalPlatRepository.findAllByJournalPlatId_Date(date);

        assertNotNull(journalPlatList);
        assertFalse(journalPlatList.isEmpty());
        assertEquals(1, journalPlatList.size());
        assertThat(journalPlatList).contains(journalPlat1);
    }
}
