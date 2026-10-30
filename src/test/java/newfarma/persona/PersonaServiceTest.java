package newfarma.persona;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Persona;
import newfarma.persona.dto.PersonaListRequest;
import newfarma.persona.dto.PersonaListResponse;
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
@DisplayName("Pruebas Unitarias - PersonaService")
class PersonaServiceTest {
    @Mock private PersonaRepository repository;
    @Mock private PersonaRepositoryJPA PersonaRepositoryJPA;
    @InjectMocks private PersonaService service;
    private Persona sample;

    @BeforeEach void setUp() {
        sample = new Persona();
        sample.setIdpersona(1L); sample.setDni(12345678L);
        sample.setNombre("Juan"); sample.setPaterno("Perez"); sample.setMaterno("Lopez");
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            PersonaListRequest p = new PersonaListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(Collections.emptyList());
            when(repository.list(any(), eq("T"))).thenReturn(0L);
            PersonaListResponse r = service.list(p);
            assertThat(r).isNotNull();
            assertThat(r.getTotal()).isEqualTo(0L);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza persona existente cuando idpersona no es nulo")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(PersonaRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(PersonaRepositoryJPA.save(any())).thenReturn(sample);
            BaseResponse r = service.save(sample);
            assertThat(r.getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nueva persona cuando dni no existe")
        void save_WhenNewDni_ShouldCreate() {
            Persona nueva = new Persona(); nueva.setDni(99999999L); nueva.setNombre("Ana");
            when(PersonaRepositoryJPA.existsPersonaByDni(99999999L)).thenReturn(false);
            when(PersonaRepositoryJPA.save(any())).thenReturn(nueva);
            assertThat(service.save(nueva).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error 500 si el dni ya existe")
        void save_WhenDniExists_ShouldReturnError() {
            Persona dup = new Persona(); dup.setDni(12345678L);
            when(PersonaRepositoryJPA.existsPersonaByDni(12345678L)).thenReturn(true);
            assertThat(service.save(dup).getStatus()).isEqualTo(500);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina persona existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(PersonaRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
            verify(PersonaRepositoryJPA).delete(sample);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(PersonaRepositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
}