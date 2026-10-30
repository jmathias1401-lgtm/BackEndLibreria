package newfarma.empresa;

import jakarta.persistence.EntityNotFoundException;
import newfarma.empresa.dto.EmpresaListRequest;
import newfarma.empresa.dto.EmpresaListResponse;
import newfarma.model.empresa;
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
@DisplayName("Pruebas Unitarias - EmpresaService")
class EmpresaServiceTest {
    @Mock private EmpresaRepository repository;
    @Mock private EmpresaRepositoryJPA empresaRepositoryJPA;
    @InjectMocks private EmpresaService service;
    private empresa sample;

    @BeforeEach void setUp() {
        sample = new empresa(); sample.setIdempresa(1L); sample.setNombre("FARMACIA CENTRAL");
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            EmpresaListRequest p = new EmpresaListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            EmpresaListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza empresa existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(empresaRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(empresaRepositoryJPA.save(any())).thenReturn(sample);
            assertThat(service.save(sample).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nueva empresa cuando nombre no existe")
        void save_WhenNew_ShouldCreate() {
            empresa nuevo = new empresa(); nuevo.setNombre("NUEVA FARMACIA");
            when(empresaRepositoryJPA.existsEmpresaByNombre("NUEVA FARMACIA")).thenReturn(false);
            when(empresaRepositoryJPA.save(any())).thenReturn(nuevo);
            assertThat(service.save(nuevo).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error si nombre ya existe")
        void save_WhenDuplicate_ShouldReturnError() {
            empresa dup = new empresa(); dup.setNombre("FARMACIA CENTRAL");
            when(empresaRepositoryJPA.existsEmpresaByNombre("FARMACIA CENTRAL")).thenReturn(true);
            assertThat(service.save(dup).getStatus()).isEqualTo(500);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina empresa existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(empresaRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(empresaRepositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
    @Nested @DisplayName("CountEmpresa()") class CountTests {
        @Test @DisplayName("Retorna conteo de empresas")
        void count_ShouldReturnCount() {
            when(empresaRepositoryJPA.CountEmpresa()).thenReturn(3);
            assertThat(service.CountEmpresa()).isEqualTo(3);
        }
    }
}