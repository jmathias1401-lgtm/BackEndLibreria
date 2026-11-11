package newfarma.cargo;

import jakarta.persistence.EntityNotFoundException;
import newfarma.cargo.dto.CargoListRequest;
import newfarma.cargo.dto.CargoListResponse;
import newfarma.model.Cargo;
import newfarma.utils.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - CargoService")
class CargoServiceTest {

    @Mock private CargoRepository repository;
    @Mock private CargoRepositoryJPA repositoryJPA;
    @InjectMocks private CargoService service;

    private Cargo sampleCargo;

    @BeforeEach
    void setUp() {
        sampleCargo = new Cargo();
        sampleCargo.setIdcargo(1L);
        sampleCargo.setNombrecargo("ADMINISTRADOR");
    }

    @Nested
    @DisplayName("Operacion: list(CargoListRequest)")
    class ListTests {
        @Test
        @DisplayName("Debe retornar respuesta con lista y total correctos")
        void list_ShouldReturnResponseWithListAndTotal() {
            CargoListRequest params = new CargoListRequest();
            params.setPage(1); params.setXpage(10);
            when(repository.list(any(CargoListRequest.class), eq("L"))).thenReturn(List.of(sampleCargo));
            when(repository.list(any(CargoListRequest.class), eq("T"))).thenReturn(1L);

            CargoListResponse response = service.list(params);

            assertThat(response).isNotNull();
            assertThat(response.getList()).hasSize(1);
            assertThat(response.getTotal()).isEqualTo(1L);
            verify(repository, times(1)).list(any(), eq("L"));
            verify(repository, times(1)).list(any(), eq("T"));
        }
    }

    @Nested
    @DisplayName("Operacion: save(Cargo)")
    class SaveTests {
        @Test
        @DisplayName("Debe actualizar cargo existente cuando idcargo no es nulo")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(repositoryJPA.findById(1L)).thenReturn(Optional.of(sampleCargo));
            when(repositoryJPA.save(any(Cargo.class))).thenReturn(sampleCargo);

            BaseResponse response = service.save(sampleCargo);

            assertThat(response.getStatus()).isEqualTo(200);
            verify(repositoryJPA, times(1)).findById(1L);
            verify(repositoryJPA, times(1)).save(any(Cargo.class));
        }

        @Test
        @DisplayName("Debe crear nuevo cargo cuando idcargo es nulo y nombre no existe")
        void save_WhenIdNullAndNameNew_ShouldCreate() {
            Cargo nuevo = new Cargo();
            nuevo.setNombrecargo("NUEVO");
            when(repositoryJPA.existsCargoByNombrecargo("NUEVO")).thenReturn(false);
            when(repositoryJPA.save(any(Cargo.class))).thenReturn(nuevo);

            BaseResponse response = service.save(nuevo);

            assertThat(response.getStatus()).isEqualTo(200);
            verify(repositoryJPA, times(1)).save(nuevo);
        }

        @Test
        @DisplayName("Debe retornar error 500 cuando nombre de cargo ya existe")
        void save_WhenNameAlreadyExists_ShouldReturnError() {
            Cargo duplicado = new Cargo();
            duplicado.setNombrecargo("DUPLICADO");
            when(repositoryJPA.existsCargoByNombrecargo("DUPLICADO")).thenReturn(true);

            BaseResponse response = service.save(duplicado);

            assertThat(response.getStatus()).isEqualTo(500);
            verify(repositoryJPA, never()).save(any());
        }

        @Test
        @DisplayName("Debe guardar cargo mediante DTO")
        void save_WithDto_ShouldSucceed() {
            newfarma.cargo.dto.CargoRequestDto dto = new newfarma.cargo.dto.CargoRequestDto();
            dto.setNombreCargo("DTO_CARGO");
            when(repositoryJPA.existsCargoByNombrecargo("DTO_CARGO")).thenReturn(false);
            when(repositoryJPA.save(any(Cargo.class))).thenReturn(sampleCargo);

            BaseResponse response = service.save(dto);

            assertThat(response.getStatus()).isEqualTo(200);
        }
    }

    @Nested
    @DisplayName("Operacion: eliminar(Long id)")
    class EliminarTests {
        @Test
        @DisplayName("Debe eliminar cargo existente correctamente")
        void eliminar_WhenExists_ShouldDelete() {
            when(repositoryJPA.findById(1L)).thenReturn(Optional.of(sampleCargo));

            BaseResponse response = service.eliminar(1L);

            assertThat(response.getStatus()).isEqualTo(200);
            verify(repositoryJPA, times(1)).delete(sampleCargo);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFoundException si cargo no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(repositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
}