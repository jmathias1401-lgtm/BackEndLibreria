package newfarma.pedido;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Pedido;
import newfarma.pedido.dto.PedidoListRequest;
import newfarma.pedido.dto.PedidoListResponse;
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
@DisplayName("Pruebas Unitarias - PedidoService")
class PedidoServiceTest {
    @Mock private PedidoRepository repository;
    @Mock private PedidoRepositoryJPA repositoryJPA;
    @InjectMocks private PedidoService service;
    private Pedido sample;

    @BeforeEach void setUp() {
        sample = new Pedido(); sample.setIdpedido(1L); sample.setCodigopedido("P-001");
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            PedidoListRequest p = new PedidoListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            PedidoListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza pedido existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(repositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(repositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nuevo pedido cuando id es nulo")
        void save_WhenIdNull_ShouldCreate() {
            Pedido nuevo = new Pedido(); nuevo.setCodigopedido("P-002");
            when(repositoryJPA.save(any())).thenReturn(nuevo);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina pedido existente")
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
    @Nested @DisplayName("lastPedido()") class LastPedidoTests {
        @Test @DisplayName("Retorna el ultimo codigo de pedido")
        void lastPedido_ShouldReturnLastCode() {
            when(repositoryJPA.lastPedido()).thenReturn(List.of("P-099"));
            assertThat(service.lastPedido()).isEqualTo("[P-099]");
        }
    }
}