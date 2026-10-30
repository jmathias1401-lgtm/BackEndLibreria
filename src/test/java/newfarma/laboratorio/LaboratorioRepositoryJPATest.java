package newfarma.laboratorio;

import newfarma.model.Laboratorio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - LaboratorioRepositoryJPA")
class LaboratorioRepositoryJPATest {

    @Autowired
    private LaboratorioRepositoryJPA laboratorioRepositoryJPA;

    @Test
    @DisplayName("existsLaboratorioByNombrelaboratorio retorna true si el laboratorio existe")
    void existsLaboratorioByNombrelaboratorio_ShouldReturnTrueWhenExists() {
        Laboratorio lab = new Laboratorio();
        lab.setNombrelaboratorio("LAB_" + System.currentTimeMillis());
        laboratorioRepositoryJPA.save(lab);

        boolean exists = laboratorioRepositoryJPA.existsLaboratorioByNombrelaboratorio(lab.getNombrelaboratorio());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsLaboratorioByNombrelaboratorio retorna false si no existe")
    void existsLaboratorioByNombrelaboratorio_ShouldReturnFalseWhenNotExists() {
        boolean exists = laboratorioRepositoryJPA.existsLaboratorioByNombrelaboratorio("LAB_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("existsLaboratorioByIdlaboratorio retorna true si existe por ID")
    void existsLaboratorioByIdlaboratorio_ShouldReturnTrueWhenExists() {
        Laboratorio lab = new Laboratorio();
        lab.setNombrelaboratorio("LAB_ID_" + System.currentTimeMillis());
        Laboratorio saved = laboratorioRepositoryJPA.save(lab);

        boolean exists = laboratorioRepositoryJPA.existsLaboratorioByIdlaboratorio(saved.getIdlaboratorio().intValue());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Laboratorio lab = new Laboratorio();
        lab.setNombrelaboratorio("LAB_CRUD_" + System.currentTimeMillis());
        Laboratorio saved = laboratorioRepositoryJPA.save(lab);

        Optional<Laboratorio> found = laboratorioRepositoryJPA.findById(saved.getIdlaboratorio());

        assertThat(found).isPresent();
        assertThat(found.get().getNombrelaboratorio()).isEqualTo(lab.getNombrelaboratorio());
    }
}
