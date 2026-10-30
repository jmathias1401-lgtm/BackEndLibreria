package newfarma.empleado;

import newfarma.model.Empleado;
import newfarma.model.Persona;
import newfarma.persona.PersonaRepositoryJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - EmpleadoRepositoryJPA")
class EmpleadoRepositoryJPATest {

    @Autowired
    private EmpleadoRepositoryJPA empleadoRepositoryJPA;

    @Autowired
    private PersonaRepositoryJPA personaRepositoryJPA;

    @Test
    @DisplayName("existsEmpleadoByIdempleado retorna true si el empleado existe")
    void existsEmpleadoByIdempleado_ShouldReturnTrueWhenExists() {
        Persona persona = new Persona();
        persona.setNombre("EmpleadoPersona");
        persona.setDni(73000000L + (System.currentTimeMillis() % 10000000L));
        persona = personaRepositoryJPA.save(persona);

        Empleado empleado = new Empleado();
        empleado.setPersona(persona);
        Empleado saved = empleadoRepositoryJPA.save(empleado);

        boolean exists = empleadoRepositoryJPA.existsEmpleadoByIdempleado(saved.getIdempleado());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsEmpleadoByIdempleado retorna false si no existe")
    void existsEmpleadoByIdempleado_ShouldReturnFalseWhenNotExists() {
        boolean exists = empleadoRepositoryJPA.existsEmpleadoByIdempleado(99999L);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Persona persona = new Persona();
        persona.setNombre("EmpleadoTest");
        persona.setDni(74000000L + (System.currentTimeMillis() % 10000000L));
        persona = personaRepositoryJPA.save(persona);

        Empleado empleado = new Empleado();
        empleado.setPersona(persona);
        Empleado saved = empleadoRepositoryJPA.save(empleado);

        Optional<Empleado> found = empleadoRepositoryJPA.findById(saved.getIdempleado());

        assertThat(found).isPresent();
        assertThat(found.get().getPersona().getIdpersona()).isEqualTo(persona.getIdpersona());
    }
}
