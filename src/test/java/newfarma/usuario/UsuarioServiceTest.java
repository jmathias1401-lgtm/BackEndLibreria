package newfarma.usuario;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Cargo;
import newfarma.model.Usuario;
import newfarma.usuario.dto.UserListRequest;
import newfarma.usuario.dto.UserListResponse;
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
@DisplayName("Pruebas Unitarias - UsuarioService")
class UsuarioServiceTest {
    @Mock private UsuarioRepository repository;
    @Mock private UsuarioRepositoryJPA usuarioRepositoryJPA;
    @InjectMocks private UsuarioService service;
    private Usuario sampleUser;

    @BeforeEach void setUp() {
        Cargo cargo = new Cargo(); cargo.setIdcargo(1L); cargo.setNombrecargo("ADMIN");
        sampleUser = Usuario.builder()
                .idusuario(1L).nombreusuario("admin").clave("pass")
                .fechacreacion(new Date()).cargo(cargo).build();
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista paginada")
        void list_ShouldReturnPaginatedResponse() {
            UserListRequest p = new UserListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sampleUser));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            UserListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save() - Usuario") class SaveTests {
        @Test @DisplayName("Actualiza usuario existente")
        void save_WhenIdNotNull_ShouldUpdate() {
            when(usuarioRepositoryJPA.findById(1L)).thenReturn(Optional.of(sampleUser));
            when(usuarioRepositoryJPA.save(any())).thenReturn(sampleUser);
            Usuario result = service.save(sampleUser);
            assertThat(result).isNotNull();
            verify(usuarioRepositoryJPA).save(any());
        }
        @Test @DisplayName("Crea usuario cuando nombreusuario no existe")
        void save_WhenNewUsername_ShouldCreate() {
            Usuario nuevo = Usuario.builder().nombreusuario("nuevo").clave("pass")
                    .fechacreacion(new Date()).cargo(sampleUser.getCargo()).build();
            when(usuarioRepositoryJPA.existsUsuarioByNombreusuario("nuevo")).thenReturn(false);
            when(usuarioRepositoryJPA.save(any())).thenReturn(nuevo);
            assertThat(service.save(nuevo)).isNotNull();
        }
        @Test @DisplayName("Retorna null si nombreusuario ya existe")
        void save_WhenUsernameExists_ShouldReturnNull() {
            Usuario dup = Usuario.builder().nombreusuario("admin").clave("x")
                    .fechacreacion(new Date()).cargo(sampleUser.getCargo()).build();
            when(usuarioRepositoryJPA.existsUsuarioByNombreusuario("admin")).thenReturn(true);
            assertThat(service.save(dup)).isNull();
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina usuario existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(usuarioRepositoryJPA.findById(1L)).thenReturn(Optional.of(sampleUser));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(usuarioRepositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
    @Nested @DisplayName("CountUsuario()") class CountTests {
        @Test @DisplayName("Retorna el conteo de usuarios")
        void count_ShouldReturnUserCount() {
            when(usuarioRepositoryJPA.CountUsuario()).thenReturn(5);
            assertThat(service.CountUsuario()).isEqualTo(5);
        }
    }
}