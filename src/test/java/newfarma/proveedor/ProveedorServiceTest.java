package newfarma.proveedor;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Persona;
import newfarma.model.Proveedor;
import newfarma.persona.PersonaService;
import newfarma.proveedor.dto.ProveedorListRequest;
import newfarma.proveedor.dto.ProveedorResponse;
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
@DisplayName("Pruebas Unitarias - ProveedorService")
class ProveedorServiceTest {
    @Mock private ProveedorRepository repository;
    @Mock private ProveedorRepositoryJPA repositoryJPA;
    @Mock private PersonaService ServicioPersona;
    @InjectMocks private ProveedorService service;
    private Proveedor sample;
    private Persona persona;

    @BeforeEach void setUp() {
        persona = new Persona(); persona.setIdpersona(1L);
        sample = new Proveedor(); sample.setIdproveedor(1L); sample.setPersona(persona);
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            ProveedorListRequest p = new ProveedorListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            ProveedorResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza proveedor existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(repositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(repositoryJPA.save(any())).thenReturn(sample);
            when(ServicioPersona.save(any())).thenReturn(BaseResponse.builder().status(200).code("OK").message("ok").build());
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nuevo proveedor cuando id es nulo")
        void save_WhenIdNull_ShouldCreate() {
            Proveedor nuevo = new Proveedor(); nuevo.setPersona(persona);
            when(repositoryJPA.save(any())).thenReturn(nuevo);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina proveedor existente")
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