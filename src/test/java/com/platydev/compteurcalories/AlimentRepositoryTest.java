package com.platydev.compteurcalories;

import com.platydev.compteurcalories.entity.Aliment;
import com.platydev.compteurcalories.entity.CodeBarre;
import com.platydev.compteurcalories.repository.AlimentRepository;
import com.platydev.compteurcalories.repository.CodeBarreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AlimentRepositoryTest {

    @Autowired
    private AlimentRepository alimentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CodeBarreRepository codeBarreRepository;

    @Test
    public void testFindAll() {
        Aliment oignon = new Aliment("oignon", 40, "g", 0.1, 0, 0, 0, 0, 1.1, 9, 4.2, 1.7, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 7.4, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0.2, 0, 0, 10, 0, 0, 4, 0, 146, 0, 0, 0, 0, 0, 0, 0);
        Aliment tomate = new Aliment("tomate", 19.3, "g", 0.26, 0, 0, 0, 0, 0.86, 2.5, 0, 1.2, 0, 0, 449, 0, 0.039, 0.019, 0.65, 0.21, 0.082, 0, 22.7, 0, 0, 15.5, 0, 0.66, 7.9, 0, 0, 0, 8.14, 51, 0, 0, 0, 0.029, 0.12, 0, 0.2, 10.1, 0.066, 0, 3.22, 26.6, 256, 0, 0, 0, 10, 0, 0, 0.087);
        Aliment pommeDeTerre = new Aliment("pomme de terre", 93.2, "g", 1.37, 0, 0, 0, 0, 2.01, 17.2, 1.05, 1.96, 0, 0, 3.68, 0, 0.07, 0.013, 1.08, 0.46, 0.24, 0, 18.4, 0, 0.03, 5.05, 0, 0.12, 1.34, 0,0, 0, 9.62, 150, 0, 0, 0, 0.095, 0.43, 0, 6.78, 20.7, 0.12, 0, 48.8, 43.7, 450, 0, 0, 0, 7.07, 0, 0, 0.24);
        Aliment huileOlive = new Aliment("huile d'olive", 900, "g", 100, 14, 75.2, 6.9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 47.8, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0.6, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0);
        Aliment blancPoulet = new Aliment("blanc de poulet", 121, "g", 1.8, 0.6, 0.7, 0.4, 0, 26.2, 0, 0, 0, 0, 70.4, 0, 4.7, 0.2, 0.1, 11.1, 1.4, 0.5, 0, 8, 0, 0.3, 2.6, 0.3, 0.3, 0.3, 0, 0, 0, 13.6, 0, 0, 0, 0, 0, 0.4, 0, 5, 26.5, 0, 0, 415, 480, 290, 0, 0, 0, 11, 0, 0, 0.8);
        Aliment oignon2 = new Aliment("oignon2", 40, "g", 0.1, 0, 0, 0, 0, 1.1, 9, 4.2, 1.7, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 7.4, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0.2, 0, 0, 10, 0, 0, 4, 0, 146, 0, 0, 0, 0, 0, 0, 0);

        List<Aliment> alimentList = alimentRepository.findAll();

        assertNotNull(alimentList);
        assertFalse(alimentList.isEmpty());
        assertEquals(6, alimentList.size());
        assertThat(alimentList).contains(oignon, tomate, pommeDeTerre, huileOlive, blancPoulet, oignon2);
    }

    @Test
    public void testFindBy() {
        Aliment pommeDeTerre = new Aliment("pomme de terre", 93.2, "g", 1.37, 0, 0, 0, 0, 2.01, 17.2, 1.05, 1.96, 0, 0, 3.68, 0, 0.07, 0.013, 1.08, 0.46, 0.24, 0, 18.4, 0, 0.03, 5.05, 0, 0.12, 1.34, 0,0, 0, 9.62, 150, 0, 0, 0, 0.095, 0.43, 0, 6.78, 20.7, 0.12, 0, 48.8, 43.7, 450, 0, 0, 0, 7.07, 0, 0, 0.24);

        Aliment aliment = alimentRepository.findById(3L).orElse(null);

        assertEquals(pommeDeTerre, aliment);

        CodeBarre fleuryMichel = new CodeBarre("1233456789098", "fleury michel", entityManager.find(Aliment.class, 3));

        assertEquals(fleuryMichel, aliment.getCodeBarre());
    }

    @Test
    public void testCreate() {
        Aliment aliment = new Aliment("gfhhg", 333, "g", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        CodeBarre codeBarre = new CodeBarre("1234456789021", "trucmuche", aliment);
        aliment.setCodeBarre(codeBarre);

        alimentRepository.save(aliment);

        Aliment alimentDB = entityManager.find(Aliment.class, aliment.getId());

        assertEquals(aliment, alimentDB);

        CodeBarre codeBarreDB = entityManager.find(CodeBarre.class, "1234456789021");

        assertEquals(codeBarre, codeBarreDB);
    }

    @Test
    public void testUpdate() {
        Aliment pommeDeTerre = new Aliment("pomme de terre", 93.2, "g", 1.37, 0, 0, 0, 0, 2.01, 17.2, 1.05, 1.96, 0, 0, 3.68, 0, 0.07, 0.013, 1.08, 0.46, 0.24, 0, 18.4, 0, 0.03, 5.05, 0, 0.12, 1.34, 0,0, 0, 9.62, 150, 0, 0, 0, 0.095, 0.43, 0, 6.78, 20.7, 0.12, 0, 48.8, 43.7, 450, 0, 0, 0, 7.07, 0, 0, 0.24);
        pommeDeTerre.setId(3);

        Aliment alimentDB = entityManager.find(Aliment.class, 3);

        assertEquals(pommeDeTerre, alimentDB);

        CodeBarre codeBarre = new CodeBarre("1234456789021", "truc", pommeDeTerre);
        pommeDeTerre.setCalories(450);
        pommeDeTerre.setCl(342);
        pommeDeTerre.setCodeBarre(codeBarre);

        alimentRepository.save(pommeDeTerre);

        alimentDB = entityManager.find(Aliment.class, 3);

        assertEquals(pommeDeTerre, alimentDB);

        assertEquals(codeBarre, alimentDB.getCodeBarre());
    }

    @Test
    public void testDelete() {
        Aliment oignon = new Aliment("oignon", 40, "g", 0.1, 0, 0, 0, 0, 1.1, 9, 4.2, 1.7, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 7.4, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0.2, 0, 0, 10, 0, 0, 4, 0, 146, 0, 0, 0, 0, 0, 0, 0);
        Aliment tomate = new Aliment("tomate", 19.3, "g", 0.26, 0, 0, 0, 0, 0.86, 2.5, 0, 1.2, 0, 0, 449, 0, 0.039, 0.019, 0.65, 0.21, 0.082, 0, 22.7, 0, 0, 15.5, 0, 0.66, 7.9, 0, 0, 0, 8.14, 51, 0, 0, 0, 0.029, 0.12, 0, 0.2, 10.1, 0.066, 0, 3.22, 26.6, 256, 0, 0, 0, 10, 0, 0, 0.087);
        Aliment pommeDeTerre = new Aliment("pomme de terre", 93.2, "g", 1.37, 0, 0, 0, 0, 2.01, 17.2, 1.05, 1.96, 0, 0, 3.68, 0, 0.07, 0.013, 1.08, 0.46, 0.24, 0, 18.4, 0, 0.03, 5.05, 0, 0.12, 1.34, 0,0, 0, 9.62, 150, 0, 0, 0, 0.095, 0.43, 0, 6.78, 20.7, 0.12, 0, 48.8, 43.7, 450, 0, 0, 0, 7.07, 0, 0, 0.24);
        Aliment huileOlive = new Aliment("huile d'olive", 900, "g", 100, 14, 75.2, 6.9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 47.8, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0.6, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0);
        Aliment blancPoulet = new Aliment("blanc de poulet", 121, "g", 1.8, 0.6, 0.7, 0.4, 0, 26.2, 0, 0, 0, 0, 70.4, 0, 4.7, 0.2, 0.1, 11.1, 1.4, 0.5, 0, 8, 0, 0.3, 2.6, 0.3, 0.3, 0.3, 0, 0, 0, 13.6, 0, 0, 0, 0, 0, 0.4, 0, 5, 26.5, 0, 0, 415, 480, 290, 0, 0, 0, 11, 0, 0, 0.8);

        Aliment aliment = entityManager.find(Aliment.class, 6);
        entityManager.persist(new CodeBarre("3455678532169", "truu", aliment));
        alimentRepository.deleteById(6L);

        List<Aliment> alimentList = alimentRepository.findAll();

        assertNotNull(alimentList);
        assertFalse(alimentList.isEmpty());
        assertEquals(5, alimentList.size());
        assertThat(alimentList).contains(oignon, tomate, pommeDeTerre, huileOlive, blancPoulet);

        List<CodeBarre> codeBarreList = codeBarreRepository.findAll();

        assertNotNull(codeBarreList);
        assertFalse(codeBarreList.isEmpty());
        assertEquals(2, codeBarreList.size());
    }
}
