package newfarma.persona;

import newfarma.model.Persona;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - PersonaRepositoryJPA")
class PersonaRepositoryJPATest {

    @Autowired
    private PersonaRepositoryJPA personaRepositoryJPA;

    @Test
    @DisplayName("existsPersonaByDni retorna true si existe persona con dicho DNI")
    void existsPersonaByDni_ShouldReturnTrueWhenExists() {
        Persona persona = new Persona();
        persona.setNombre("Carlos");
        persona.setPaterno("Perez");
        long dni = 80000000L + (System.currentTimeMillis() % 10000000L);
        persona.setDni(dni);
        personaRepositoryJPA.save(persona);

        boolean exists = personaRepositoryJPA.existsPersonaByDni(dni);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsPersonaByDni retorna false si no existe")
    void existsPersonaByDni_ShouldReturnFalseWhenNotExists() {
        boolean exists = personaRepositoryJPA.existsPersonaByDni(-1L);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Persona persona = new Persona();
        persona.setNombre("Maria");
        persona.setPaterno("Lopez");
        persona.setTelefono("987654321");
        Persona saved = personaRepositoryJPA.save(persona);

        Optional<Persona> found = personaRepositoryJPA.findById(saved.getIdpersona());

        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Maria");
        assertThat(found.get().getTelefono()).isEqualTo("987654321");
    }
}
