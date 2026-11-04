package newfarma.detalleventa;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import newfarma.detalleventa.dto.DetalleVentaListRequest;
import newfarma.detalleventa.dto.DetalleVentaListResponse;
import newfarma.detalleventa.dto.DetalleVentaRequestDto;
import newfarma.model.detalleventa;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = DetalleVentaController.class,
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
@DisplayName("Pruebas Unitarias - DetalleVentaController")
class DetalleVentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DetalleVentaServiceI serviceI;

    @MockBean
    private DetalleVentaRepositoryJPA detalleVentaRepositoryJPA;

    @Nested
    @DisplayName("Operación: GET /api/detalleventa (Listar)")
    class ListTests {

        @Test
        @DisplayName("Debe listar detalles de venta y retornar HTTP 200 con DetalleVentaListResponse")
        void list_ShouldReturn200AndDetalleVentaListResponse() throws Exception {
            DetalleVentaListResponse response = DetalleVentaListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(10)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.list(any(DetalleVentaListRequest.class))).thenReturn(response);

            mockMvc.perform(get("/api/detalleventa")
                            .param("page", "1")
                            .param("xpage", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.xpage").value(10));

            verify(serviceI, times(1)).list(any(DetalleVentaListRequest.class));
        }
    }

    @Nested
    @DisplayName("Operación: GET /api/detalleventa/{id} (Buscar por ID)")
    class GetByIdTests {

        @Test
        @DisplayName("Debe retornar HTTP 200 cuando el ID de detalle de venta existe")
        void listbyId_WhenValidId_ShouldReturn200AndDetalleVentaListResponse() throws Exception {
            int id = 1;
            DetalleVentaListResponse response = DetalleVentaListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(1)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.DetalleVentaListById(eq(id))).thenReturn(response);

            mockMvc.perform(get("/api/detalleventa/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.total").value(1));

            verify(serviceI, times(1)).DetalleVentaListById(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID no es numérico")
        void listbyId_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(get("/api/detalleventa/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).DetalleVentaListById(anyInt());
        }
    }

    @Nested
    @DisplayName("Operación: GET /api/detalleventa/ventasMes/{mes}/{anio} (Reporte mensual)")
    class VentasMesTests {

        @Test
        @DisplayName("Debe retornar HTTP 200 con reporte de ventas mensual")
        void ventasMes_WhenValidParams_ShouldReturn200AndReport() throws Exception {
            int mes = 10;
            int anio = 2026;
            DetalleVentaListResponse response = DetalleVentaListResponse.builder()
                    .total(5)
                    .page(1)
                    .xpage(10)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.ventasMes(eq(mes), eq(anio))).thenReturn(response);

            mockMvc.perform(get("/api/detalleventa/ventasMes/{mes}/{anio}", mes, anio))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.total").value(5));

            verify(serviceI, times(1)).ventasMes(eq(mes), eq(anio));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando mes o año no son numéricos")
        void ventasMes_WhenInvalidParams_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(get("/api/detalleventa/ventasMes/octubre/2026"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).ventasMes(anyInt(), anyInt());
        }
    }

    @Nested
    @DisplayName("Operación: POST /api/detalleventa (Guardar)")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar detalle de venta válidamente y retornar HTTP 200 con BaseResponse")
        void save_WhenValidDetalleVenta_ShouldReturn200AndBaseResponse() throws Exception {
            DetalleVentaRequestDto item = DetalleVentaRequestDto.builder()
                    .idDetalleVenta(1L)
                    .codigoDetalleVenta("DV001")
                    .unidades(2)
                    .costoUnidad(15.0)
                    .total(30.0)
                    .build();

            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Detalle de venta guardado exitosamente")
                    .build();

            when(serviceI.save(any(DetalleVentaRequestDto.class))).thenReturn(baseResponse);

            mockMvc.perform(post("/api/detalleventa")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(item)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Detalle de venta guardado exitosamente"));

            verify(serviceI, times(1)).save(any(DetalleVentaRequestDto.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el body no es provisto")
        void save_WhenBodyIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(post("/api/detalleventa")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any(DetalleVentaRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Operación: DELETE /api/detalleventa/{id} (Eliminar)")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar detalle de venta por ID y retornar HTTP 200 con BaseResponse")
        void eliminar_WhenValidId_ShouldReturn200AndBaseResponse() throws Exception {
            Long id = 1L;
            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Detalle de venta eliminado exitosamente")
                    .build();

            when(serviceI.eliminar(eq(id))).thenReturn(baseResponse);

            mockMvc.perform(delete("/api/detalleventa/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Detalle de venta eliminado exitosamente"));

            verify(serviceI, times(1)).eliminar(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID a eliminar no es numérico")
        void eliminar_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(delete("/api/detalleventa/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).eliminar(any());
        }
    }
}
