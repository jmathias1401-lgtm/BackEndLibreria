package newfarma.detallecompra;

import jakarta.persistence.EntityNotFoundException;
import newfarma.detallecompra.dto.DetalleCompraListRequest;
import newfarma.detallecompra.dto.DetalleCompraListResponse;
import newfarma.model.detallecompra;
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
@DisplayName("Pruebas Unitarias - DetalleCompraService")
class DetalleCompraServiceTest {
    @Mock private DetalleCompraRepository repository;
    @Mock private DetalleCompraRepositoryJPA detalleCompraRepositoryJPA;
    @InjectMocks private DetalleCompraService service;
    private detallecompra sample;

    @BeforeEach void setUp() {
        sample = new detallecompra(); sample.setIddetallecompra(1L);
        sample.setCodigodetallecompra("DC-001"); sample.setUnidades(10);
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            DetalleCompraListRequest p = new DetalleCompraListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            DetalleCompraListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza detalle compra existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(detalleCompraRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(detalleCompraRepositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nuevo detalle cuando codigo no existe")
        void save_WhenNew_ShouldCreate() {
            detallecompra nuevo = new detallecompra(); nuevo.setCodigodetallecompra("DC-002"); nuevo.setUnidades(5);
            when(detalleCompraRepositoryJPA.existsDetalleCompraByCodigodetallecompra("DC-002")).thenReturn(false);
            when(detalleCompraRepositoryJPA.save(any())).thenReturn(nuevo);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error 500 si codigo ya existe")
        void save_WhenDuplicate_ShouldReturnError() {
            detallecompra dup = new detallecompra(); dup.setCodigodetallecompra("DC-001");
            when(detalleCompraRepositoryJPA.existsDetalleCompraByCodigodetallecompra("DC-001")).thenReturn(true);
            assertThat(service.save(dup).getStatus()).isEqualTo(500);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina detalle compra existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(detalleCompraRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(detalleCompraRepositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
    @Nested @DisplayName("CountDetalleCompra()") class CountTests {
        @Test @DisplayName("Retorna conteo de detalles de compra")
        void count_ShouldReturnCount() {
            when(detalleCompraRepositoryJPA.CountDetalleCompra()).thenReturn(15);
            assertThat(service.CountDetalleCompra()).isEqualTo(15);
        }
    }
}