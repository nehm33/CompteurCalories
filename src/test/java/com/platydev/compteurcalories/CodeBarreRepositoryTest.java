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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CodeBarreRepositoryTest {

    @Autowired
    private CodeBarreRepository codeBarreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlimentRepository alimentRepository;

    @Test
    public void testFindAll() {
        CodeBarre fleuryMichel = new CodeBarre("1233456789098", "fleury michel", entityManager.find(Aliment.class, 3));
        CodeBarre doree = new CodeBarre("3211456789045", "doree", entityManager.find(Aliment.class, 4));

        List<CodeBarre> codeBarreList = codeBarreRepository.findAll();

        assertNotNull(codeBarreList);
        assertFalse(codeBarreList.isEmpty());
        assertEquals(codeBarreList.size(), 2);
        assertThat(codeBarreList).contains(fleuryMichel, doree);
    }

    @Test
    public void testFindBy() {
        CodeBarre fleuryMichel = new CodeBarre("1233456789098", "fleury michel", entityManager.find(Aliment.class, 3));

        CodeBarre codeBarre = codeBarreRepository.findById("1233456789098").orElse(null);

        assertEquals(fleuryMichel, codeBarre);
    }

    @Test
    public void testCreate() {
        CodeBarre truc = new CodeBarre("65423457890120", "truc", entityManager.find(Aliment.class, 1));

        codeBarreRepository.save(truc);

        CodeBarre codeBarreDB = entityManager.find(CodeBarre.class, "65423457890120");

        assertEquals(truc, codeBarreDB);

        Aliment oignon = new Aliment("oignon", 40, "g", 0.1, 0, 0, 0, 0, 1.1, 9, 4.2, 1.7, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 7.4, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0.2, 0, 0, 10, 0, 0, 4, 0, 146, 0, 0, 0, 0, 0, 0, 0);

        Aliment aliment = entityManager.find(Aliment.class, 1);

        assertNotNull(aliment);
        assertEquals(oignon, aliment);
    }

    @Test
    public void testUpdate() {
        CodeBarre fleuryMichel = new CodeBarre("1233456789098", "fleury michel", entityManager.find(Aliment.class, 3));

        CodeBarre codeBarreDB = codeBarreRepository.findById("1233456789098").orElse(null);

        assertEquals(fleuryMichel, codeBarreDB);

        fleuryMichel.setMarque("tfugj");

        codeBarreRepository.save(fleuryMichel);

        codeBarreDB = codeBarreRepository.findById("1233456789098").orElse(null);

        assertEquals(fleuryMichel, codeBarreDB);

        Aliment pommeDeTerre = new Aliment("pomme de terre", 93.2, "g", 1.37, 0, 0, 0, 0, 2.01, 17.2, 1.05, 1.96, 0, 0, 3.68, 0, 0.07, 0.013, 1.08, 0.46, 0.24, 0, 18.4, 0, 0.03, 5.05, 0, 0.12, 1.34, 0,0, 0, 9.62, 150, 0, 0, 0, 0.095, 0.43, 0, 6.78, 20.7, 0.12, 0, 48.8, 43.7, 450, 0, 0, 0, 7.07, 0, 0, 0.24);

        Aliment aliment = entityManager.find(Aliment.class, 3);

        assertNotNull(aliment);
        assertEquals(pommeDeTerre, aliment);
    }

    @Test
    public void testDelete() {
        CodeBarre fleuryMichel = new CodeBarre("1233456789098", "fleury michel", entityManager.find(Aliment.class, 3));

        codeBarreRepository.deleteById("3211456789045");

        List<CodeBarre> codeBarreList = codeBarreRepository.findAll();

        assertNotNull(codeBarreList);
        assertFalse(codeBarreList.isEmpty());
        assertEquals(codeBarreList.size(), 1);
        assertThat(codeBarreList).contains(fleuryMichel);

        Aliment huileOlive = new Aliment("huile d'olive", 900, "g", 100, 14, 75.2, 6.9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 47.8, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0.6, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0);

        Aliment aliment = entityManager.find(Aliment.class, 4);

        assertNotNull(aliment);
        assertEquals(huileOlive, aliment);
    }
}
