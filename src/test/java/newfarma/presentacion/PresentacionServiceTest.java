package newfarma.presentacion;

import jakarta.persistence.EntityNotFoundException;
import newfarma.presentacion.dto.PresentacionListRequest;
import newfarma.presentacion.dto.PresentacionListResponse;
import newfarma.model.Presentacion;
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
@DisplayName("Pruebas Unitarias - PresentacionService")
class PresentacionServiceTest {
    @Mock private PresentacionRepository repository;
    @Mock private PresentacionRepositoryJPA presentacionRepositoryJPA;
    @InjectMocks private PresentacionService service;
    private Presentacion sample;

    @BeforeEach void setUp() {
        sample = new Presentacion(); sample.setIdpresentacion(1L); sample.setNombrepresentacion("CAJA");
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            PresentacionListRequest p = new PresentacionListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            PresentacionListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza presentacion existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(presentacionRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(presentacionRepositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nueva cuando nombre no existe")
        void save_WhenNew_ShouldCreate() {
            Presentacion nuevo = new Presentacion(); nuevo.setNombrepresentacion("BLISTER");
            when(presentacionRepositoryJPA.existsLaboratorioByNombrepresentacion("BLISTER")).thenReturn(false);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error 500 si nombre duplicado")
        void save_WhenDuplicate_ShouldReturnError() {
            Presentacion dup = new Presentacion(); dup.setNombrepresentacion("CAJA");
            when(presentacionRepositoryJPA.existsLaboratorioByNombrepresentacion("CAJA")).thenReturn(true);
            assertThat(service.save(dup).getStatus()).isEqualTo(500);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(presentacionRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
            verify(presentacionRepositoryJPA).delete(sample);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(presentacionRepositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
}