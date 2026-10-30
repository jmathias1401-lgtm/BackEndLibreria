package newfarma.proveedor;

import newfarma.model.Persona;
import newfarma.model.Proveedor;
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
@DisplayName("Pruebas de Repositorio - ProveedorRepositoryJPA")
class ProveedorRepositoryJPATest {

    @Autowired
    private ProveedorRepositoryJPA proveedorRepositoryJPA;

    @Autowired
    private PersonaRepositoryJPA personaRepositoryJPA;

    @Test
    @DisplayName("existsProveedorByIdproveedor retorna true si existe por ID")
    void existsProveedorByIdproveedor_ShouldReturnTrueWhenExists() {
        Persona persona = new Persona();
        persona.setNombre("ProveedorPersona");
        persona.setPaterno("PaternoProv");
        persona.setDni(75000000L + (System.currentTimeMillis() % 10000000L));
        persona = personaRepositoryJPA.save(persona);

        Proveedor prov = new Proveedor();
        prov.setPersona(persona);
        Proveedor saved = proveedorRepositoryJPA.save(prov);

        boolean exists = proveedorRepositoryJPA.existsProveedorByIdproveedor(saved.getIdproveedor().intValue());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsProveedorByIdproveedor retorna false si no existe")
    void existsProveedorByIdproveedor_ShouldReturnFalseWhenNotExists() {
        boolean exists = proveedorRepositoryJPA.existsProveedorByIdproveedor(99999);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Persona persona = new Persona();
        persona.setNombre("PersonaProv2");
        persona.setPaterno("PaternoProv2");
        persona.setDni(76000000L + (System.currentTimeMillis() % 10000000L));
        persona = personaRepositoryJPA.save(persona);

        Proveedor prov = new Proveedor();
        prov.setPersona(persona);
        Proveedor saved = proveedorRepositoryJPA.save(prov);

        Optional<Proveedor> found = proveedorRepositoryJPA.findById(saved.getIdproveedor());

        assertThat(found).isPresent();
        assertThat(found.get().getPersona().getIdpersona()).isEqualTo(persona.getIdpersona());
    }
}
