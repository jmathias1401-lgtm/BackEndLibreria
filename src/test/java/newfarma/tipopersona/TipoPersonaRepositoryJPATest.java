package newfarma.tipopersona;

import newfarma.model.TipoPersona;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - TipoPersonaRepositoryJPA")
class TipoPersonaRepositoryJPATest {

    @Autowired
    private TipoPersonaRepositoryJPA tipoPersonaRepositoryJPA;

    @Test
    @DisplayName("existsTipoPersonaByNombre retorna true si existe")
    void existsTipoPersonaByNombre_ShouldReturnTrueWhenExists() {
        TipoPersona tipo = new TipoPersona();
        tipo.setNombre("TIPO_" + System.currentTimeMillis());
        tipoPersonaRepositoryJPA.save(tipo);

        boolean exists = tipoPersonaRepositoryJPA.existsTipoPersonaByNombre(tipo.getNombre());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsTipoPersonaByNombre retorna false si no existe")
    void existsTipoPersonaByNombre_ShouldReturnFalseWhenNotExists() {
        boolean exists = tipoPersonaRepositoryJPA.existsTipoPersonaByNombre("TIPO_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("existsTipoPersonaByIdtipopersona retorna true si existe por ID")
    void existsTipoPersonaByIdtipopersona_ShouldReturnTrueWhenExists() {
        TipoPersona tipo = new TipoPersona();
        tipo.setNombre("TIPO_ID_" + System.currentTimeMillis());
        TipoPersona saved = tipoPersonaRepositoryJPA.save(tipo);

        boolean exists = tipoPersonaRepositoryJPA.existsTipoPersonaByIdtipopersona(saved.getIdtipopersona().intValue());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        TipoPersona tipo = new TipoPersona();
        tipo.setNombre("TIPO_CRUD_" + System.currentTimeMillis());
        TipoPersona saved = tipoPersonaRepositoryJPA.save(tipo);

        Optional<TipoPersona> found = tipoPersonaRepositoryJPA.findById(saved.getIdtipopersona());

        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo(tipo.getNombre());
    }
}
