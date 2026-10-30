package newfarma.tipopersona;

import jakarta.persistence.EntityNotFoundException;
import newfarma.tipopersona.dto.TipoPersonaListRequest;
import newfarma.tipopersona.dto.TipoPersonaListResponse;
import newfarma.model.TipoPersona;
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
@DisplayName("Pruebas Unitarias - TipoPersonaService")
class TipoPersonaServiceTest {
    @Mock private TipoPersonaRepository repository;
    @Mock private TipoPersonaRepositoryJPA repositoryJPA;
    @InjectMocks private TipoPersonaService service;
    private TipoPersona sample;

    @BeforeEach void setUp() {
        sample = new TipoPersona(); sample.setIdtipopersona(1L); sample.setNombre("NATURAL");
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            TipoPersonaListRequest p = new TipoPersonaListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            TipoPersonaListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza tipo persona existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(repositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(repositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea cuando nombre no existe")
        void save_WhenNew_ShouldCreate() {
            TipoPersona nuevo = new TipoPersona(); nuevo.setNombre("JURIDICA");
            when(repositoryJPA.existsTipoPersonaByNombre("JURIDICA")).thenReturn(false);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error si nombre duplicado")
        void save_WhenDuplicate_ShouldReturnError() {
            TipoPersona dup = new TipoPersona(); dup.setNombre("NATURAL");
            when(repositoryJPA.existsTipoPersonaByNombre("NATURAL")).thenReturn(true);
            assertThat(service.save(dup).getStatus()).isEqualTo(500);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina existente")
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