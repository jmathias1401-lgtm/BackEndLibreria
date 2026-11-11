package newfarma.venta;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Venta;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
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
@DisplayName("Pruebas Unitarias - VentaService")
class VentaServiceTest {
    @Mock private ventaRepository repository;
    @Mock private VentaRepositoryJPA ventaRepositoryJPA;
    @InjectMocks private VentaService service;
    private Venta sample;

    @BeforeEach void setUp() {
        sample = new Venta(); sample.setIdventa(1L);
        sample.setSerie("B001"); sample.setCorrelativo("00001");
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            VentaListRequest p = new VentaListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            VentaListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza venta existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(ventaRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(ventaRepositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nueva venta cuando correlativo no existe")
        void save_WhenNew_ShouldCreate() {
            Venta nuevo = new Venta(); nuevo.setSerie("B002"); nuevo.setCorrelativo("00002");
            when(ventaRepositoryJPA.existsVentaByCorrelativo("00002")).thenReturn(false);
            when(ventaRepositoryJPA.save(any())).thenReturn(nuevo);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error 500 si correlativo ya existe")
        void save_WhenDuplicate_ShouldReturnError() {
            Venta dup = new Venta(); dup.setSerie("B001"); dup.setCorrelativo("00001");
            when(ventaRepositoryJPA.existsVentaByCorrelativo("00001")).thenReturn(true);
            assertThat(service.save(dup).getStatus()).isEqualTo(500);
        }
        @Test @DisplayName("Guarda venta mediante DTO")
        void save_WithDto_ShouldSucceed() {
            newfarma.venta.dto.VentaRequestDto dto = new newfarma.venta.dto.VentaRequestDto();
            dto.setSerie("B003");
            dto.setCorrelativo("00003");
            when(ventaRepositoryJPA.existsVentaByCorrelativo("00003")).thenReturn(false);
            when(ventaRepositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(dto).getStatus()).isEqualTo(200);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina venta existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(ventaRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Retorna 500 si id de venta es nulo")
        void eliminar_WhenIdNull_ShouldReturn500() {
            Venta noId = new Venta();
            when(ventaRepositoryJPA.findById(2L)).thenReturn(Optional.of(noId));
            assertThat(service.eliminar(2L).getStatus()).isEqualTo(500);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(ventaRepositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
}