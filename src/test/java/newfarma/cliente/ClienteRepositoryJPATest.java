package newfarma.cliente;

import newfarma.model.Cliente;
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
@DisplayName("Pruebas de Repositorio - ClienteRepositoryJPA")
class ClienteRepositoryJPATest {

    @Autowired
    private ClienteRepositoryJPA clienteRepositoryJPA;

    @Autowired
    private PersonaRepositoryJPA personaRepositoryJPA;

    @Test
    @DisplayName("existsClienteByIdcliente retorna true si el cliente existe por ID")
    void existsClienteByIdcliente_ShouldReturnTrueWhenExists() {
        Persona persona = new Persona();
        persona.setNombre("ClientePersona");
        persona.setDni(71000000L + (System.currentTimeMillis() % 10000000L));
        persona = personaRepositoryJPA.save(persona);

        Cliente cliente = new Cliente();
        cliente.setPersona(persona);
        Cliente saved = clienteRepositoryJPA.save(cliente);

        boolean exists = clienteRepositoryJPA.existsClienteByIdcliente(saved.getIdcliente().intValue());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsClienteByIdcliente retorna false si no existe")
    void existsClienteByIdcliente_ShouldReturnFalseWhenNotExists() {
        boolean exists = clienteRepositoryJPA.existsClienteByIdcliente(99999);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Persona persona = new Persona();
        persona.setNombre("PersonaTest");
        persona.setDni(72000000L + (System.currentTimeMillis() % 10000000L));
        persona = personaRepositoryJPA.save(persona);

        Cliente cliente = new Cliente();
        cliente.setPersona(persona);
        Cliente saved = clienteRepositoryJPA.save(cliente);

        Optional<Cliente> found = clienteRepositoryJPA.findById(saved.getIdcliente());

        assertThat(found).isPresent();
        assertThat(found.get().getPersona().getIdpersona()).isEqualTo(persona.getIdpersona());
    }
}
