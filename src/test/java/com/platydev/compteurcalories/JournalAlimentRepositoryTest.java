package com.platydev.compteurcalories;

import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.JournalAliment;
import com.platydev.compteurcalories.entity.JournalAlimentId;
import com.platydev.compteurcalories.repository.JournalAlimentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JournalAlimentRepositoryTest {

    @Autowired
    private JournalAlimentRepository journalAlimentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindAllByJournalAlimentId_Date() {
        Aliment oignon = new Aliment("oignon", 40, "g", 0.1, 0, 0, 0, 0, 1.1, 9, 4.2, 1.7, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 7.4, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0.2, 0, 0, 10, 0, 0, 4, 0, 146, 0, 0, 0, 0, 0, 0, 0);
        Aliment tomate = new Aliment("tomate", 19.3, "g", 0.26, 0, 0, 0, 0, 0.86, 2.5, 0, 1.2, 0, 0, 449, 0, 0.039, 0.019, 0.65, 0.21, 0.082, 0, 22.7, 0, 0, 15.5, 0, 0.66, 7.9, 0, 0, 0, 8.14, 51, 0, 0, 0, 0.029, 0.12, 0, 0.2, 10.1, 0.066, 0, 3.22, 26.6, 256, 0, 0, 0, 10, 0, 0, 0.087);
        Date date = Date.valueOf("2023-12-25");
        oignon.setId(1);
        tomate.setId(2);

        JournalAliment journalAliment1 = new JournalAliment(date, oignon, 2);
        JournalAliment journalAliment2 = new JournalAliment(date, tomate, 4);

        List<JournalAliment> journalAlimentList = journalAlimentRepository.findAllByJournalAlimentId_Date(date);

        assertNotNull(journalAlimentList);
        assertFalse(journalAlimentList.isEmpty());
        assertEquals(2, journalAlimentList.size());
        assertThat(journalAlimentList).contains(journalAliment1, journalAliment2);
    }

    @Test
    public void testFindBy() {
        Aliment oignon = new Aliment("oignon", 40, "g", 0.1, 0, 0, 0, 0, 1.1, 9, 4.2, 1.7, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 7.4, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0.2, 0, 0, 10, 0, 0, 4, 0, 146, 0, 0, 0, 0, 0, 0, 0);
        Date date = Date.valueOf("2023-12-25");
        oignon.setId(1);

        JournalAliment journalAliment = new JournalAliment(date, oignon, 2);

        JournalAliment journalAlimentDB = journalAlimentRepository.findById(journalAliment.getJournalAlimentId()).orElse(null);

        assertEquals(journalAliment, journalAlimentDB);
    }

    @Test
    public void testCreate() {
        Aliment huileOlive = new Aliment("huile d'olive", 900, "g", 100, 14, 75.2, 6.9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 47.8, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0.6, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0);
        Date date = Date.valueOf("2023-12-25");
        huileOlive.setId(4);

        JournalAliment journalAliment = new JournalAliment(date, huileOlive, 40);

        journalAlimentRepository.save(journalAliment);

        JournalAliment journalAlimentDB = entityManager.find(JournalAliment.class, journalAliment.getJournalAlimentId());

        assertEquals(journalAliment, journalAlimentDB);
    }

    @Test
    public void testUpdate() {
        Aliment oignon = new Aliment("oignon", 40, "g", 0.1, 0, 0, 0, 0, 1.1, 9, 4.2, 1.7, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 7.4, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0.2, 0, 0, 10, 0, 0, 4, 0, 146, 0, 0, 0, 0, 0, 0, 0);
        Date date = new Date(LocalDate.of(2023, 12, 25).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        oignon.setId(1);

        JournalAliment journalAliment = new JournalAliment(date, oignon, 2);

        JournalAliment journalAlimentDB = entityManager.find(JournalAliment.class, journalAliment.getJournalAlimentId());

        assertEquals(journalAliment, journalAlimentDB);

        journalAliment.setQuantite(7);

        journalAlimentRepository.save(journalAliment);

        journalAlimentDB = entityManager.find(JournalAliment.class, journalAliment.getJournalAlimentId());

        assertEquals(journalAliment, journalAlimentDB);
    }

    @Test
    public void testDelete() {
        Aliment oignon = new Aliment("oignon", 40, "g", 0.1, 0, 0, 0, 0, 1.1, 9, 4.2, 1.7, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 7.4, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0.2, 0, 0, 10, 0, 0, 4, 0, 146, 0, 0, 0, 0, 0, 0, 0);
        Date date = Date.valueOf("2023-12-25");
        oignon.setId(1);

        JournalAliment journalAliment = new JournalAliment(date, oignon, 2);

        journalAlimentRepository.deleteById(new JournalAlimentId(2, date));

        List<JournalAliment> journalAlimentList = journalAlimentRepository.findAllByJournalAlimentId_Date(date);

        assertNotNull(journalAlimentList);
        assertFalse(journalAlimentList.isEmpty());
        assertEquals(1, journalAlimentList.size());
        assertThat(journalAlimentList).contains(journalAliment);
    }
}
