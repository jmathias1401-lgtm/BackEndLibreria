package newfarma.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import newfarma.model.Usuario;
import newfarma.usuario.dto.UserListRequest;
import newfarma.usuario.dto.UserListResponse;
import newfarma.utils.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UsuarioController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas Unitarias - UsuarioController")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioServiceI serviceI;

    @MockBean
    private UsuarioRepositoryJPA usuarioRepositoryJPA;

    @Nested
    @DisplayName("Operación: GET /api/usuario (Listar)")
    class ListTests {

        @Test
        @DisplayName("Debe listar usuarios y retornar HTTP 200 con UserListResponse")
        void list_ShouldReturn200AndUserListResponse() throws Exception {
            UserListResponse response = UserListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(10)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.list(any(UserListRequest.class))).thenReturn(response);

            mockMvc.perform(get("/api/usuario")
                            .param("page", "1")
                            .param("xpage", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.xpage").value(10));

            verify(serviceI, times(1)).list(any(UserListRequest.class));
        }
    }

    @Nested
    @DisplayName("Operación: GET /api/usuario/{id} (Buscar por ID)")
    class GetByIdTests {

        @Test
        @DisplayName("Debe retornar HTTP 200 cuando el ID de usuario existe")
        void listbyId_WhenValidId_ShouldReturn200AndUserListResponse() throws Exception {
            Long id = 1L;
            UserListResponse response = UserListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(1)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.UserListById(eq(id))).thenReturn(response);

            mockMvc.perform(get("/api/usuario/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.total").value(1));

            verify(serviceI, times(1)).UserListById(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID no es numérico")
        void listbyId_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(get("/api/usuario/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).UserListById(any());
        }
    }

    @Nested
    @DisplayName("Operación: POST /api/usuario (Guardar)")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar usuario válidamente y retornar HTTP 200 con el objeto guardado")
        void save_WhenValidUser_ShouldReturn200AndSavedUser() throws Exception {
            newfarma.model.Cargo cargo = new newfarma.model.Cargo();
            cargo.setIdcargo(1L);
            cargo.setNombrecargo("ADMIN");

            Usuario usuario = Usuario.builder()
                    .idusuario(1L)
                    .nombreusuario("admin")
                    .clave("123456")
                    .cargo(cargo)
                    .build();

            when(serviceI.save(any(Usuario.class))).thenReturn(usuario);

            String usuarioJson = """
                    {
                        "nombreusuario": "admin",
                        "clave": "123456"
                    }
                    """;

            mockMvc.perform(post("/api/usuario")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(usuarioJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idusuario").value(1L))
                    .andExpect(jsonPath("$.nombreusuario").value("admin"));

            verify(serviceI, times(1)).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el body está vacío")
        void save_WhenBodyIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(post("/api/usuario")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Operación: DELETE /api/usuario/{id} (Eliminar)")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar usuario por ID y retornar HTTP 200 con BaseResponse")
        void eliminar_WhenValidId_ShouldReturn200AndBaseResponse() throws Exception {
            Long id = 1L;
            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Usuario eliminado exitosamente")
                    .build();

            when(serviceI.eliminar(eq(id))).thenReturn(baseResponse);

            mockMvc.perform(delete("/api/usuario/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Usuario eliminado exitosamente"));

            verify(serviceI, times(1)).eliminar(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID a eliminar no es numérico")
        void eliminar_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(delete("/api/usuario/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).eliminar(any());
        }
    }
}
