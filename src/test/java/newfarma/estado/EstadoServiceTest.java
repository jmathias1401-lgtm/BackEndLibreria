package newfarma.estado;

import jakarta.persistence.EntityNotFoundException;
import newfarma.estado.dto.EstadoListRequest;
import newfarma.estado.dto.EstadoListResponse;
import newfarma.model.Estado;
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
@DisplayName("Pruebas Unitarias - EstadoService")
class EstadoServiceTest {
    @Mock private EstadoRepository repository;
    @Mock private EstadoRepositoryJPA repositoryJPA;
    @InjectMocks private EstadoService service;
    private Estado sampleEstado;

    @BeforeEach void setUp() {
        sampleEstado = new Estado(); sampleEstado.setIdestado(1L); sampleEstado.setEstado("ACTIVO");
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            EstadoListRequest p = new EstadoListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sampleEstado));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            EstadoListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
            assertThat(r.getTotal()).isEqualTo(1L);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza estado existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(repositoryJPA.findById(1L)).thenReturn(Optional.of(sampleEstado));
            when(repositoryJPA.save(any())).thenReturn(sampleEstado);
            assertThat(service.save(sampleEstado).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nuevo estado cuando nombre no existe")
        void save_WhenNew_ShouldCreate() {
            Estado nuevo = new Estado(); nuevo.setEstado("INACTIVO");
            when(repositoryJPA.existsEstadoByEstado("INACTIVO")).thenReturn(false);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error 500 si nombre ya existe")
        void save_WhenDuplicate_ShouldReturnError() {
            Estado dup = new Estado(); dup.setEstado("ACTIVO");
            when(repositoryJPA.existsEstadoByEstado("ACTIVO")).thenReturn(true);
            assertThat(service.save(dup).getStatus()).isEqualTo(500);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(repositoryJPA.findById(1L)).thenReturn(Optional.of(sampleEstado));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
            verify(repositoryJPA).delete(sampleEstado);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(repositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
}