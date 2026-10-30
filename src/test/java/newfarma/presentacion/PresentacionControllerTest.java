package newfarma.presentacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import newfarma.model.Presentacion;
import newfarma.presentacion.dto.PresentacionListRequest;
import newfarma.presentacion.dto.PresentacionListResponse;
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
        controllers = PresentacionController.class,
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
@DisplayName("Pruebas Unitarias - PresentacionController")
class PresentacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PresentacionServiceI serviceI;

    @Nested
    @DisplayName("Operación: GET /api/presentacion (Listar)")
    class ListTests {

        @Test
        @DisplayName("Debe listar presentaciones y retornar HTTP 200 con PresentacionListResponse")
        void list_ShouldReturn200AndPresentacionListResponse() throws Exception {
            PresentacionListResponse response = PresentacionListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(10)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.list(any(PresentacionListRequest.class))).thenReturn(response);

            mockMvc.perform(get("/api/presentacion")
                            .param("page", "1")
                            .param("xpage", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.xpage").value(10));

            verify(serviceI, times(1)).list(any(PresentacionListRequest.class));
        }
    }

    @Nested
    @DisplayName("Operación: POST /api/presentacion (Guardar)")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar presentación válidamente y retornar HTTP 200 con BaseResponse")
        void save_WhenValidPresentacion_ShouldReturn200AndBaseResponse() throws Exception {
            Presentacion presentacion = new Presentacion();
            presentacion.setIdpresentacion(1L);
            presentacion.setNombrepresentacion("Caja x 100 tabletas");

            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Presentación guardada exitosamente")
                    .build();

            when(serviceI.save(any(Presentacion.class))).thenReturn(baseResponse);

            mockMvc.perform(post("/api/presentacion")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(presentacion)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Presentación guardada exitosamente"));

            verify(serviceI, times(1)).save(any(Presentacion.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el body no es provisto")
        void save_WhenBodyIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(post("/api/presentacion")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Operación: DELETE /api/presentacion/{id} (Eliminar)")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar presentación por ID y retornar HTTP 200 con BaseResponse")
        void eliminar_WhenValidId_ShouldReturn200AndBaseResponse() throws Exception {
            Long id = 1L;
            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Presentación eliminada exitosamente")
                    .build();

            when(serviceI.eliminar(eq(id))).thenReturn(baseResponse);

            mockMvc.perform(delete("/api/presentacion/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Presentación eliminada exitosamente"));

            verify(serviceI, times(1)).eliminar(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID a eliminar no es numérico")
        void eliminar_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(delete("/api/presentacion/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).eliminar(any());
        }
    }
}
