package newfarma.compra;

import jakarta.persistence.EntityNotFoundException;
import newfarma.compra.dto.CompraListRequest;
import newfarma.compra.dto.CompraListResponse;
import newfarma.model.Compra;
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
@DisplayName("Pruebas Unitarias - CompraService")
class CompraServiceTest {
    @Mock private compraRepository repository;
    @Mock private CompraRepositoryJPA compraRepositoryJPA;
    @InjectMocks private CompraService service;
    private Compra sample;

    @BeforeEach void setUp() {
        sample = new Compra(); sample.setIdcompra(1L);
        sample.setSerie("F001"); sample.setCorrelativo("00001");
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            CompraListRequest p = new CompraListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            CompraListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza compra existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(compraRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(compraRepositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nueva compra cuando correlativo-serie no existe")
        void save_WhenNew_ShouldCreate() {
            Compra nuevo = new Compra(); nuevo.setSerie("F002"); nuevo.setCorrelativo("00002");
            when(compraRepositoryJPA.existsCompraBySerieAndAndCorrelativo(any(), any())).thenReturn(false);
            when(compraRepositoryJPA.save(any())).thenReturn(nuevo);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error 500 si correlativo-serie ya existe")
        void save_WhenDuplicate_ShouldReturnError() {
            Compra dup = new Compra(); dup.setSerie("F001"); dup.setCorrelativo("00001");
            when(compraRepositoryJPA.existsCompraBySerieAndAndCorrelativo(any(), any())).thenReturn(true);
            assertThat(service.save(dup).getStatus()).isEqualTo(500);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina compra existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(compraRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(compraRepositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
}