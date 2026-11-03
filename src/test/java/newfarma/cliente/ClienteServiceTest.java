package newfarma.cliente;

import jakarta.persistence.EntityNotFoundException;
import newfarma.cliente.dto.ClienteListRequest;
import newfarma.cliente.dto.ClienteRequestDto;
import newfarma.cliente.dto.ClienteResponse;
import newfarma.model.Cliente;
import newfarma.model.Persona;
import newfarma.persona.PersonaService;
import newfarma.utils.BaseResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - ClienteService")
class ClienteServiceTest {
    @Mock private ClienteRepository repository;
    @Mock private ClienteRepositoryJPA repositoryJPA;
    @Mock private PersonaService ServicioPersona;
    @InjectMocks private ClienteService service;
    private Cliente sample;
    private Persona persona;

    @BeforeEach void setUp() {
        persona = new Persona(); persona.setIdpersona(1L); persona.setDni(11111111L);
        sample = new Cliente(); sample.setIdcliente(1L); sample.setPersona(persona);
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            ClienteListRequest p = new ClienteListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            ClienteResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza cliente existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(repositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(repositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nuevo cliente cuando id es nulo")
        void save_WhenIdNull_ShouldCreate() {
            Cliente nuevo = new Cliente(); nuevo.setPersona(persona);
            when(ServicioPersona.buscarPorDni(11111111L)).thenReturn(persona);
            when(repositoryJPA.existsClienteByPersonaId(1L)).thenReturn(false);
            when(repositoryJPA.save(any())).thenReturn(nuevo);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Guarda cliente mediante DTO")
        void save_WithDto_ShouldSucceed() {
            ClienteRequestDto dto = new ClienteRequestDto();
            dto.setPersona(persona);
            when(ServicioPersona.buscarPorDni(11111111L)).thenReturn(persona);
            when(repositoryJPA.existsClienteByPersonaId(1L)).thenReturn(false);
            when(repositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(dto).getStatus()).isEqualTo(200);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina cliente existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(repositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(repositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
}
