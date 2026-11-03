package newfarma.venta;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import newfarma.utils.BaseResponse;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
import newfarma.venta.dto.VentaRequestDto;
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
        controllers = VentaController.class,
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
@DisplayName("Pruebas Unitarias - VentaController")
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VentaServiceI serviceI;

    @MockBean
    private VentaRepositoryJPA personaRepositoryJPA;

    @Nested
    @DisplayName("Operación: GET /api/venta (Listar)")
    class ListTests {

        @Test
        @DisplayName("Debe listar ventas y retornar HTTP 200 con VentaListResponse")
        void list_ShouldReturn200AndVentaListResponse() throws Exception {
            VentaListResponse response = VentaListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(10)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.list(any(VentaListRequest.class))).thenReturn(response);

            mockMvc.perform(get("/api/venta")
                            .param("page", "1")
                            .param("xpage", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.xpage").value(10));

            verify(serviceI, times(1)).list(any(VentaListRequest.class));
        }
    }

    @Nested
    @DisplayName("Operación: POST /api/venta (Guardar)")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar venta válidamente y retornar HTTP 200 con BaseResponse")
        void save_WhenValidVenta_ShouldReturn200AndBaseResponse() throws Exception {
            VentaRequestDto ventaDto = VentaRequestDto.builder()
                    .idVenta(1L)
                    .serie("V001")
                    .correlativo("0000456")
                    .igv(18.0)
                    .subtotal(100.0)
                    .costoVenta(118.0)
                    .build();

            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Venta guardada exitosamente")
                    .build();

            when(serviceI.save(any(VentaRequestDto.class))).thenReturn(baseResponse);

            mockMvc.perform(post("/api/venta")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(ventaDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Venta guardada exitosamente"));

            verify(serviceI, times(1)).save(any(VentaRequestDto.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el body no es provisto")
        void save_WhenBodyIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(post("/api/venta")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any(VentaRequestDto.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando los campos obligatorios están en blanco")
        void save_WhenFieldsInvalid_ShouldReturn400() throws Exception {
            VentaRequestDto invalidDto = new VentaRequestDto();

            mockMvc.perform(post("/api/venta")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDto)))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any(VentaRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Operación: DELETE /api/venta/{id} (Eliminar)")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar venta por ID y retornar HTTP 200 con BaseResponse")
        void eliminar_WhenValidId_ShouldReturn200AndBaseResponse() throws Exception {
            Long id = 1L;
            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Venta eliminada exitosamente")
                    .build();

            when(serviceI.eliminar(eq(id))).thenReturn(baseResponse);

            mockMvc.perform(delete("/api/venta/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Venta eliminada exitosamente"));

            verify(serviceI, times(1)).eliminar(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID a eliminar no es numérico")
        void eliminar_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(delete("/api/venta/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).eliminar(any());
        }
    }
}
