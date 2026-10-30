package newfarma.pedido;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import newfarma.model.Pedido;
import newfarma.pedido.dto.PedidoListRequest;
import newfarma.pedido.dto.PedidoListResponse;
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
        controllers = PedidoController.class,
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
@DisplayName("Pruebas Unitarias - PedidoController")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoServiceI serviceI;

    @Nested
    @DisplayName("Operación: GET /api/pedido (Listar)")
    class ListTests {

        @Test
        @DisplayName("Debe listar pedidos y retornar HTTP 200 con PedidoListResponse")
        void list_ShouldReturn200AndPedidoListResponse() throws Exception {
            PedidoListResponse response = PedidoListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(10)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.list(any(PedidoListRequest.class))).thenReturn(response);

            mockMvc.perform(get("/api/pedido")
                            .param("page", "1")
                            .param("xpage", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.xpage").value(10));

            verify(serviceI, times(1)).list(any(PedidoListRequest.class));
        }
    }

    @Nested
    @DisplayName("Operación: POST /api/pedido (Guardar)")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar pedido válidamente y retornar HTTP 200 con BaseResponse")
        void save_WhenValidPedido_ShouldReturn200AndBaseResponse() throws Exception {
            Pedido pedido = new Pedido();
            pedido.setIdpedido(1L);
            pedido.setCodigopedido("PED-001");
            pedido.setStock(5);

            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Pedido guardado exitosamente")
                    .build();

            when(serviceI.save(any(Pedido.class))).thenReturn(baseResponse);

            mockMvc.perform(post("/api/pedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedido)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Pedido guardado exitosamente"));

            verify(serviceI, times(1)).save(any(Pedido.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el body no es provisto")
        void save_WhenBodyIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(post("/api/pedido")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Operación: DELETE /api/pedido/{id} (Eliminar)")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar pedido por ID y retornar HTTP 200 con BaseResponse")
        void eliminar_WhenValidId_ShouldReturn200AndBaseResponse() throws Exception {
            Long id = 1L;
            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Pedido eliminado exitosamente")
                    .build();

            when(serviceI.eliminar(eq(id))).thenReturn(baseResponse);

            mockMvc.perform(delete("/api/pedido/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Pedido eliminado exitosamente"));

            verify(serviceI, times(1)).eliminar(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID a eliminar no es numérico")
        void eliminar_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(delete("/api/pedido/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).eliminar(any());
        }
    }

    @Nested
    @DisplayName("Operación: GET /api/pedido/lastPedido (Último código de pedido)")
    class LastPedidoTests {

        @Test
        @DisplayName("Debe retornar HTTP 200 con el correlativo del último pedido")
        void lastPedido_ShouldReturn200AndCodeMap() throws Exception {
            when(serviceI.lastPedido()).thenReturn("PED-00042");

            mockMvc.perform(get("/api/pedido/lastPedido"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigopedido").value("PED-00042"));

            verify(serviceI, times(1)).lastPedido();
        }
    }
}
