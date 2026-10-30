package newfarma.detallecompra;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import newfarma.detallecompra.dto.DetalleCompraListRequest;
import newfarma.detallecompra.dto.DetalleCompraListResponse;
import newfarma.model.detallecompra;
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
        controllers = DetalleCompraController.class,
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
@DisplayName("Pruebas Unitarias - DetalleCompraController")
class DetalleCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DetalleCompraServiceI serviceI;

    @MockBean
    private DetalleCompraRepositoryJPA detalleCompraRepositoryJPA;

    @Nested
    @DisplayName("Operación: GET /api/detallecompra (Listar)")
    class ListTests {

        @Test
        @DisplayName("Debe listar detalles de compra y retornar HTTP 200 con DetalleCompraListResponse")
        void list_ShouldReturn200AndDetalleCompraListResponse() throws Exception {
            DetalleCompraListResponse response = DetalleCompraListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(10)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.list(any(DetalleCompraListRequest.class))).thenReturn(response);

            mockMvc.perform(get("/api/detallecompra")
                            .param("page", "1")
                            .param("xpage", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.xpage").value(10));

            verify(serviceI, times(1)).list(any(DetalleCompraListRequest.class));
        }
    }

    @Nested
    @DisplayName("Operación: GET /api/detallecompra/{id} (Buscar por ID)")
    class GetByIdTests {

        @Test
        @DisplayName("Debe retornar HTTP 200 cuando el ID de detalle de compra existe")
        void listbyId_WhenValidId_ShouldReturn200AndDetalleCompraListResponse() throws Exception {
            int id = 1;
            DetalleCompraListResponse response = DetalleCompraListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(1)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.DetalleCompraListById(eq(id))).thenReturn(response);

            mockMvc.perform(get("/api/detallecompra/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.total").value(1));

            verify(serviceI, times(1)).DetalleCompraListById(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID no es numérico")
        void listbyId_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(get("/api/detallecompra/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).DetalleCompraListById(anyInt());
        }
    }

    @Nested
    @DisplayName("Operación: GET /api/detallecompra/producto/{id} (Buscar por ID Producto)")
    class GetByProductoIdTests {

        @Test
        @DisplayName("Debe retornar HTTP 200 cuando el ID de producto existe")
        void listbyproductoId_WhenValidId_ShouldReturn200AndDetalleCompraListResponse() throws Exception {
            int productoId = 10;
            DetalleCompraListResponse response = DetalleCompraListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(1)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.listDetalleCompraByProductoId(eq(productoId))).thenReturn(response);

            mockMvc.perform(get("/api/detallecompra/producto/{id}", productoId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.total").value(1));

            verify(serviceI, times(1)).listDetalleCompraByProductoId(eq(productoId));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID de producto no es numérico")
        void listbyproductoId_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(get("/api/detallecompra/producto/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).listDetalleCompraByProductoId(anyInt());
        }
    }

    @Nested
    @DisplayName("Operación: POST /api/detallecompra (Guardar)")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar detalle de compra válidamente y retornar HTTP 200 con BaseResponse")
        void save_WhenValidDetalleCompra_ShouldReturn200AndBaseResponse() throws Exception {
            detallecompra item = new detallecompra();
            item.setIddetallecompra(1L);
            item.setUnidades(10);
            item.setCostounidad(5.50);
            item.setTotal(55.0);

            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Detalle de compra guardado exitosamente")
                    .build();

            when(serviceI.save(any(detallecompra.class))).thenReturn(baseResponse);

            mockMvc.perform(post("/api/detallecompra")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(item)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Detalle de compra guardado exitosamente"));

            verify(serviceI, times(1)).save(any(detallecompra.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el body no es provisto")
        void save_WhenBodyIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(post("/api/detallecompra")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Operación: DELETE /api/detallecompra/{id} (Eliminar)")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar detalle de compra por ID y retornar HTTP 200 con BaseResponse")
        void eliminar_WhenValidId_ShouldReturn200AndBaseResponse() throws Exception {
            Long id = 1L;
            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Detalle de compra eliminado exitosamente")
                    .build();

            when(serviceI.eliminar(eq(id))).thenReturn(baseResponse);

            mockMvc.perform(delete("/api/detallecompra/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Detalle de compra eliminado exitosamente"));

            verify(serviceI, times(1)).eliminar(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID a eliminar no es numérico")
        void eliminar_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(delete("/api/detallecompra/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).eliminar(any());
        }
    }
}
