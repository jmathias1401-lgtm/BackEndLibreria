package newfarma.unidadmedida;

import newfarma.model.UnidadMedida;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - UnidadMedidaRepositoryJPA")
class UnidadMedidaRepositoryJPATest {

    @Autowired
    private UnidadMedidaRepositoryJPA unidadMedidaRepositoryJPA;

    @Test
    @DisplayName("existsUnidadMedidaByNombreunidad retorna true si la unidad existe")
    void existsUnidadMedidaByNombreunidad_ShouldReturnTrueWhenExists() {
        UnidadMedida um = new UnidadMedida();
        um.setNombreunidad("UNIDAD_" + System.currentTimeMillis());
        unidadMedidaRepositoryJPA.save(um);

        boolean exists = unidadMedidaRepositoryJPA.existsUnidadMedidaByNombreunidad(um.getNombreunidad());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsUnidadMedidaByNombreunidad retorna false si no existe")
    void existsUnidadMedidaByNombreunidad_ShouldReturnFalseWhenNotExists() {
        boolean exists = unidadMedidaRepositoryJPA.existsUnidadMedidaByNombreunidad("UNIDAD_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        UnidadMedida um = new UnidadMedida();
        um.setNombreunidad("UNIDAD_CRUD_" + System.currentTimeMillis());
        UnidadMedida saved = unidadMedidaRepositoryJPA.save(um);

        Optional<UnidadMedida> found = unidadMedidaRepositoryJPA.findById(saved.getIdunidadmedida());

        assertThat(found).isPresent();
        assertThat(found.get().getNombreunidad()).isEqualTo(um.getNombreunidad());
    }
}
