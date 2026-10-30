package newfarma.unidadmedida;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import newfarma.model.UnidadMedida;
import newfarma.unidadmedida.dto.UnidadMedidaListRequest;
import newfarma.unidadmedida.dto.UnidadMedidaListResponse;
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
        controllers = UnidadMedidaController.class,
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
@DisplayName("Pruebas Unitarias - UnidadMedidaController")
class UnidadMedidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UnidadMedidaServiceI serviceI;

    @Nested
    @DisplayName("Operación: GET /api/unidadmedida (Listar)")
    class ListTests {

        @Test
        @DisplayName("Debe listar unidades de medida y retornar HTTP 200 con UnidadMedidaListResponse")
        void list_ShouldReturn200AndUnidadMedidaListResponse() throws Exception {
            UnidadMedidaListResponse response = UnidadMedidaListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(10)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.list(any(UnidadMedidaListRequest.class))).thenReturn(response);

            mockMvc.perform(get("/api/unidadmedida")
                            .param("page", "1")
                            .param("xpage", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.xpage").value(10));

            verify(serviceI, times(1)).list(any(UnidadMedidaListRequest.class));
        }
    }

    @Nested
    @DisplayName("Operación: POST /api/unidadmedida (Guardar)")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar unidad de medida válidamente y retornar HTTP 200 con BaseResponse")
        void save_WhenValidUnidadMedida_ShouldReturn200AndBaseResponse() throws Exception {
            UnidadMedida unidadMedida = new UnidadMedida();
            unidadMedida.setIdunidadmedida(1);
            unidadMedida.setNombreunidad("Miligramos");

            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Unidad de medida guardada exitosamente")
                    .build();

            when(serviceI.save(any(UnidadMedida.class))).thenReturn(baseResponse);

            mockMvc.perform(post("/api/unidadmedida")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(unidadMedida)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Unidad de medida guardada exitosamente"));

            verify(serviceI, times(1)).save(any(UnidadMedida.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el body no es provisto")
        void save_WhenBodyIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(post("/api/unidadmedida")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Operación: DELETE /api/unidadmedida/{id} (Eliminar)")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar unidad de medida por ID entero y retornar HTTP 200 con BaseResponse")
        void eliminar_WhenValidId_ShouldReturn200AndBaseResponse() throws Exception {
            Integer id = 1;
            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Unidad de medida eliminada exitosamente")
                    .build();

            when(serviceI.eliminar(eq(id))).thenReturn(baseResponse);

            mockMvc.perform(delete("/api/unidadmedida/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Unidad de medida eliminada exitosamente"));

            verify(serviceI, times(1)).eliminar(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID a eliminar no es numérico")
        void eliminar_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(delete("/api/unidadmedida/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).eliminar(any());
        }
    }
}
