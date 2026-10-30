package newfarma.laboratorio;

import jakarta.persistence.EntityNotFoundException;
import newfarma.laboratorio.dto.LaboratorioListRequest;
import newfarma.laboratorio.dto.LaboratorioListResponse;
import newfarma.model.Laboratorio;
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
@DisplayName("Pruebas Unitarias - LaboratorioService")
class LaboratorioServiceTest {
    @Mock private LaboratorioRepository repository;
    @Mock private LaboratorioRepositoryJPA repositoryJPA;
    @InjectMocks private LaboratorioService service;
    private Laboratorio sample;

    @BeforeEach void setUp() {
        sample = new Laboratorio(); sample.setIdlaboratorio(1L); sample.setNombrelaboratorio("BAYER");
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            LaboratorioListRequest p = new LaboratorioListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            LaboratorioListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza laboratorio existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(repositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(repositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea cuando nombre no existe")
        void save_WhenNew_ShouldCreate() {
            Laboratorio nuevo = new Laboratorio(); nuevo.setNombrelaboratorio("PFIZER");
            when(repositoryJPA.existsLaboratorioByNombrelaboratorio("PFIZER")).thenReturn(false);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error si nombre duplicado")
        void save_WhenDuplicate_ShouldReturnError() {
            Laboratorio dup = new Laboratorio(); dup.setNombrelaboratorio("BAYER");
            when(repositoryJPA.existsLaboratorioByNombrelaboratorio("BAYER")).thenReturn(true);
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