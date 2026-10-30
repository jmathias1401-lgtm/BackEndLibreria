package newfarma.unidadmedida;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.UnidadMedida;
import newfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.unidadmedida.dto.UnidadMedidaListResponse;
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
@DisplayName("Pruebas Unitarias - UnidadMedidaService")
class UnidadMedidaServiceTest {
    @Mock private UnidadMedidaRepository repository;
    @Mock private UnidadMedidaRepositoryJPA unidadMedidaRepositoryJPA;
    @InjectMocks private UnidadMedidaService service;
    private UnidadMedida sample;

    @BeforeEach void setUp() {
        sample = new UnidadMedida(); sample.setIdunidadmedida(1); sample.setNombreunidad("MILILITRO");
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            UnidadMedidaListRequest p = new UnidadMedidaListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            UnidadMedidaListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza unidad existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(unidadMedidaRepositoryJPA.findById(1)).thenReturn(Optional.of(sample));
            when(unidadMedidaRepositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nueva cuando nombre no existe")
        void save_WhenNew_ShouldCreate() {
            UnidadMedida nuevo = new UnidadMedida(); nuevo.setNombreunidad("GRAMO");
            when(unidadMedidaRepositoryJPA.existsUnidadMedidaByNombreunidad("GRAMO")).thenReturn(false);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error 500 si nombre duplicado")
        void save_WhenDuplicate_ShouldReturnError() {
            UnidadMedida dup = new UnidadMedida(); dup.setNombreunidad("MILILITRO");
            when(unidadMedidaRepositoryJPA.existsUnidadMedidaByNombreunidad("MILILITRO")).thenReturn(true);
            assertThat(service.save(dup).getStatus()).isEqualTo(500);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(unidadMedidaRepositoryJPA.findById(1)).thenReturn(Optional.of(sample));
            assertThat(service.eliminar(1).getStatus()).isEqualTo(200);
            verify(unidadMedidaRepositoryJPA).delete(sample);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(unidadMedidaRepositoryJPA.findById(99)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99));
        }
    }
}