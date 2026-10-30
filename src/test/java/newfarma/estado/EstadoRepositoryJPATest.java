package newfarma.estado;

import newfarma.model.Estado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - EstadoRepositoryJPA")
class EstadoRepositoryJPATest {

    @Autowired
    private EstadoRepositoryJPA estadoRepositoryJPA;

    @Test
    @DisplayName("existsEstadoByEstado retorna true si el estado existe")
    void existsEstadoByEstado_ShouldReturnTrueWhenExists() {
        Estado estado = new Estado();
        estado.setEstado("ESTADO_" + System.currentTimeMillis());
        estadoRepositoryJPA.save(estado);

        boolean exists = estadoRepositoryJPA.existsEstadoByEstado(estado.getEstado());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsEstadoByEstado retorna false si no existe")
    void existsEstadoByEstado_ShouldReturnFalseWhenNotExists() {
        boolean exists = estadoRepositoryJPA.existsEstadoByEstado("ESTADO_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("existsEstadoByIdestado retorna true si existe por ID")
    void existsEstadoByIdestado_ShouldReturnTrueWhenExists() {
        Estado estado = new Estado();
        estado.setEstado("ESTADO_ID_" + System.currentTimeMillis());
        Estado saved = estadoRepositoryJPA.save(estado);

        boolean exists = estadoRepositoryJPA.existsEstadoByIdestado(saved.getIdestado());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsEstadoByIdestado retorna false si no existe")
    void existsEstadoByIdestado_ShouldReturnFalseWhenNotExists() {
        boolean exists = estadoRepositoryJPA.existsEstadoByIdestado(-1L);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Estado estado = new Estado();
        estado.setEstado("ESTADO_CRUD_" + System.currentTimeMillis());
        Estado saved = estadoRepositoryJPA.save(estado);

        Optional<Estado> found = estadoRepositoryJPA.findById(saved.getIdestado());

        assertThat(found).isPresent();
        assertThat(found.get().getEstado()).isEqualTo(estado.getEstado());
    }
}
