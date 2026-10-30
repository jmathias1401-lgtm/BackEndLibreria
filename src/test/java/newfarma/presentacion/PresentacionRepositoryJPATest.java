package newfarma.presentacion;

import newfarma.model.Presentacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - PresentacionRepositoryJPA")
class PresentacionRepositoryJPATest {

    @Autowired
    private PresentacionRepositoryJPA presentacionRepositoryJPA;

    @Test
    @DisplayName("existsLaboratorioByNombrepresentacion retorna true si la presentación existe")
    void existsLaboratorioByNombrepresentacion_ShouldReturnTrueWhenExists() {
        Presentacion p = new Presentacion();
        p.setNombrepresentacion("PRES_" + System.currentTimeMillis());
        presentacionRepositoryJPA.save(p);

        boolean exists = presentacionRepositoryJPA.existsLaboratorioByNombrepresentacion(p.getNombrepresentacion());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsLaboratorioByNombrepresentacion retorna false si no existe")
    void existsLaboratorioByNombrepresentacion_ShouldReturnFalseWhenNotExists() {
        boolean exists = presentacionRepositoryJPA.existsLaboratorioByNombrepresentacion("PRES_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("findByCodigoproducto retorna lista por ID de presentación")
    void findByCodigoproducto_ShouldReturnPresentacion() {
        Presentacion p = new Presentacion();
        p.setNombrepresentacion("PRES_COD_" + System.currentTimeMillis());
        Presentacion saved = presentacionRepositoryJPA.save(p);

        List<Presentacion> list = presentacionRepositoryJPA.findByCodigoproducto(saved.getIdpresentacion().intValue());

        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getIdpresentacion()).isEqualTo(saved.getIdpresentacion());
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Presentacion p = new Presentacion();
        p.setNombrepresentacion("PRES_CRUD_" + System.currentTimeMillis());
        Presentacion saved = presentacionRepositoryJPA.save(p);

        Optional<Presentacion> found = presentacionRepositoryJPA.findById(saved.getIdpresentacion());

        assertThat(found).isPresent();
        assertThat(found.get().getNombrepresentacion()).isEqualTo(p.getNombrepresentacion());
    }
}
