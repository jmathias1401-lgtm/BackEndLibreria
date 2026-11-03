package newfarma.cliente;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.cliente.dto.ClienteListRequest;
import newfarma.cliente.dto.ClienteRequestDto;
import newfarma.cliente.dto.ClienteResponse;
import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import newfarma.model.Persona;
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
        controllers = ClienteController.class,
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
@DisplayName("Pruebas Unitarias - ClienteController")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteServiceI serviceI;

    @MockBean
    private ClienteRepositoryJPA clienteRepositoryJPA;

    @Nested
    @DisplayName("Operación: GET /api/cliente (Listar)")
    class ListTests {

        @Test
        @DisplayName("Debe listar clientes y retornar HTTP 200 con ClienteResponse")
        void list_ShouldReturn200AndClienteResponse() throws Exception {
            ClienteResponse response = ClienteResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(10)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.list(any(ClienteListRequest.class))).thenReturn(response);

            mockMvc.perform(get("/api/cliente")
                            .param("page", "1")
                            .param("xpage", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.xpage").value(10));

            verify(serviceI, times(1)).list(any(ClienteListRequest.class));
        }
    }

    @Nested
    @DisplayName("Operación: POST /api/cliente (Guardar)")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar cliente válidamente y retornar HTTP 200 con BaseResponse")
        void save_WhenValidCliente_ShouldReturn200AndBaseResponse() throws Exception {
            Persona persona = new Persona();
            persona.setIdpersona(10L);
            persona.setNombre("Juan");

            ClienteRequestDto clienteDto = ClienteRequestDto.builder()
                    .idCliente(1L)
                    .persona(persona)
                    .build();

            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Cliente guardado exitosamente")
                    .build();

            when(serviceI.save(any(ClienteRequestDto.class))).thenReturn(baseResponse);

            mockMvc.perform(post("/api/cliente")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Cliente guardado exitosamente"));

            verify(serviceI, times(1)).save(any(ClienteRequestDto.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el body no es provisto")
        void save_WhenBodyIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(post("/api/cliente")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any(ClienteRequestDto.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando falta la persona obligatoria")
        void save_WhenPersonaIsMissing_ShouldReturn400BadRequest() throws Exception {
            ClienteRequestDto invalidDto = new ClienteRequestDto();

            mockMvc.perform(post("/api/cliente")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDto)))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any(ClienteRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Operación: DELETE /api/cliente/{id} (Eliminar)")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar cliente por ID y retornar HTTP 200 con BaseResponse")
        void eliminar_WhenValidId_ShouldReturn200AndBaseResponse() throws Exception {
            Long id = 1L;
            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Cliente eliminado exitosamente")
                    .build();

            when(serviceI.eliminar(eq(id))).thenReturn(baseResponse);

            mockMvc.perform(delete("/api/cliente/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Cliente eliminado exitosamente"));

            verify(serviceI, times(1)).eliminar(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID a eliminar no es numérico")
        void eliminar_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(delete("/api/cliente/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).eliminar(any());
        }
    }
}
