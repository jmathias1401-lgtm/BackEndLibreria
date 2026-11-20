package newfarma.detalleventa;

import jakarta.persistence.EntityNotFoundException;
import newfarma.detalleventa.dto.DetalleVentaListRequest;
import newfarma.detalleventa.dto.DetalleVentaListResponse;
import newfarma.detalleventa.dto.VentaTrazabilidadDto;
import newfarma.model.detalleventa;
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
@DisplayName("Pruebas Unitarias - DetalleVentaService")
class DetalleVentaServiceTest {
    @Mock private DetalleVentaRepository repository;
    @Mock private DetalleVentaRepositoryJPA detalleVentaRepositoryJPA;
    @InjectMocks private DetalleVentaService service;
    private detalleventa sample;

    @BeforeEach void setUp() {
        sample = new detalleventa(); sample.setIddetalleventa(1L);
        sample.setCodigodetalleventa("DV-001"); sample.setUnidades(5);
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            DetalleVentaListRequest p = new DetalleVentaListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            DetalleVentaListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza detalle venta existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(detalleVentaRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(detalleVentaRepositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nuevo detalle cuando codigo no existe")
        void save_WhenNew_ShouldCreate() {
            detalleventa nuevo = new detalleventa(); nuevo.setCodigodetalleventa("DV-002"); nuevo.setUnidades(3);
            when(detalleVentaRepositoryJPA.existsDetalleVentaByCodigodetalleventa("DV-002")).thenReturn(false);
            when(detalleVentaRepositoryJPA.save(any())).thenReturn(nuevo);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error 500 si codigo ya existe")
        void save_WhenDuplicate_ShouldReturnError() {
            detalleventa dup = new detalleventa(); dup.setCodigodetalleventa("DV-001");
            when(detalleVentaRepositoryJPA.existsDetalleVentaByCodigodetalleventa("DV-001")).thenReturn(true);
            assertThat(service.save(dup).getStatus()).isEqualTo(500);
        }
        @Test @DisplayName("Guarda detalle venta mediante DTO")
        void save_WithDto_ShouldSucceed() {
            newfarma.detalleventa.dto.DetalleVentaRequestDto dto = new newfarma.detalleventa.dto.DetalleVentaRequestDto();
            dto.setCodigoDetalleVenta("DV-003");
            when(detalleVentaRepositoryJPA.existsDetalleVentaByCodigodetalleventa("DV-003")).thenReturn(false);
            when(detalleVentaRepositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(dto).getStatus()).isEqualTo(200);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina detalle venta existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(detalleVentaRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(detalleVentaRepositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
    @Nested @DisplayName("CountDetalleVenta()") class CountTests {
        @Test @DisplayName("Retorna conteo de detalles de venta")
        void count_ShouldReturnCount() {
            when(detalleVentaRepositoryJPA.CountDetalleVenta()).thenReturn(20);
            assertThat(service.CountDetalleVenta()).isEqualTo(20);
        }
    }
    @Nested @DisplayName("ventasMes(mes, anio)") class VentasMesTests {
        @Test @DisplayName("Retorna lista de ventas del mes")
        void ventasMes_ShouldReturnMonthSales() {
            when(detalleVentaRepositoryJPA.ventasMes(10, 2024)).thenReturn(List.of(sample));
            DetalleVentaListResponse r = service.ventasMes(10, 2024);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("getTrazabilidadVentas(idProducto)") class TrazabilidadTests {
        @Test @DisplayName("Retorna lista de ventas con detalle de vendedor, cliente y unidades")
        void getTrazabilidadVentas_ShouldReturnList() {
            Object[] row = new Object[]{
                100L, "2026-11-20 15:30:00", "B001", "0000123", "BOLETA", 2, 4.5, 9.0, "Juan Perez", "Maria Lopez"
            };
            when(detalleVentaRepositoryJPA.findTrazabilidadVentasByProducto(1L))
                .thenReturn(Collections.singletonList(row));

            List<VentaTrazabilidadDto> r = service.getTrazabilidadVentas(1L);
            assertThat(r).hasSize(1);
            assertThat(r.get(0).getVendedor()).isEqualTo("Juan Perez");
            assertThat(r.get(0).getCliente()).isEqualTo("Maria Lopez");
            assertThat(r.get(0).getUnidades()).isEqualTo(2);
            assertThat(r.get(0).getComprobante()).isEqualTo("BOLETA B001-0000123");
        }

        @Test @DisplayName("Maneja valores nulos en fila de trazabilidad con defaults seguros")
        void getTrazabilidadVentas_WithNulls_ShouldHandleGracefully() {
            Object[] row = new Object[]{
                null, null, null, null, null, null, null, null, "", "   "
            };
            when(detalleVentaRepositoryJPA.findTrazabilidadVentasByProducto(2L))
                .thenReturn(Collections.singletonList(row));

            List<VentaTrazabilidadDto> r = service.getTrazabilidadVentas(2L);
            assertThat(r).hasSize(1);
            assertThat(r.get(0).getVendedor()).isEqualTo("No registrado");
            assertThat(r.get(0).getCliente()).isEqualTo("Público General");
            assertThat(r.get(0).getUnidades()).isEqualTo(0);
        }
    }
}