package newfarma.productos;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import newfarma.model.Producto;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ProductoController.class,
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
@DisplayName("Pruebas Unitarias - ProductoController")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoServiceI serviceI;

    @MockBean
    private ProductoRepositoryJPA productoRepositoryJPA;

    @Nested
    @DisplayName("Operación: GET /api/producto (Listar)")
    class ListTests {

        @Test
        @DisplayName("Debe listar productos y retornar HTTP 200 con ProductListResponse")
        void list_ShouldReturn200AndProductListResponse() throws Exception {
            ProductListResponse response = ProductListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(10)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.list(any(ProductListRequest.class))).thenReturn(response);

            mockMvc.perform(get("/api/producto")
                            .param("page", "1")
                            .param("xpage", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.xpage").value(10));

            verify(serviceI, times(1)).list(any(ProductListRequest.class));
        }
    }

    @Nested
    @DisplayName("Operación: GET /api/producto/{id} (Buscar por ID)")
    class GetByIdTests {

        @Test
        @DisplayName("Debe retornar HTTP 200 cuando el ID de producto existe")
        void listbyId_WhenValidId_ShouldReturn200AndProductListResponse() throws Exception {
            int id = 1;
            ProductListResponse response = ProductListResponse.builder()
                    .total(1)
                    .page(1)
                    .xpage(1)
                    .list(Collections.emptyList())
                    .build();

            when(serviceI.ProductListById(eq(id))).thenReturn(response);

            mockMvc.perform(get("/api/producto/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.total").value(1));

            verify(serviceI, times(1)).ProductListById(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID no es numérico")
        void listbyId_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(get("/api/producto/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).ProductListById(anyInt());
        }
    }

    @Nested
    @DisplayName("Operación: POST /api/producto (Guardar con Multipart)")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar producto con archivo de imagen y retornar HTTP 200 con BaseResponse")
        void save_WithProductAndImage_ShouldReturn200AndBaseResponse() throws Exception {
            Producto producto = new Producto();
            producto.setIdproducto(1L);
            producto.setNombre("Paracetamol");

            MockMultipartFile productoPart = new MockMultipartFile(
                    "producto",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(producto)
            );

            MockMultipartFile imagenPart = new MockMultipartFile(
                    "imagen",
                    "paracetamol.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "fake-image-bytes".getBytes()
            );

            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Producto guardado exitosamente")
                    .build();

            when(serviceI.save(any(Producto.class), any())).thenReturn(baseResponse);

            mockMvc.perform(multipart("/api/producto")
                            .file(productoPart)
                            .file(imagenPart))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Producto guardado exitosamente"));

            verify(serviceI, times(1)).save(any(Producto.class), any());
        }

        @Test
        @DisplayName("Debe guardar producto sin archivo de imagen opcional y retornar HTTP 200")
        void save_WithoutImage_ShouldReturn200AndBaseResponse() throws Exception {
            Producto producto = new Producto();
            producto.setIdproducto(2L);
            producto.setNombre("Ibuprofeno");

            MockMultipartFile productoPart = new MockMultipartFile(
                    "producto",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(producto)
            );

            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Producto guardado sin imagen")
                    .build();

            when(serviceI.save(any(Producto.class), eq(null))).thenReturn(baseResponse);

            mockMvc.perform(multipart("/api/producto")
                            .file(productoPart))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"));

            verify(serviceI, times(1)).save(any(Producto.class), eq(null));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando la parte obligatoria 'producto' está ausente")
        void save_WhenProductPartIsMissing_ShouldReturn400BadRequest() throws Exception {
            MockMultipartFile imagenPart = new MockMultipartFile(
                    "imagen",
                    "foto.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "fake-bytes".getBytes()
            );

            mockMvc.perform(multipart("/api/producto")
                            .file(imagenPart))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).save(any(), any());
        }
    }

    @Nested
    @DisplayName("Operación: DELETE /api/producto/{id} (Eliminar)")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar producto por ID y retornar HTTP 200 con BaseResponse")
        void eliminar_WhenValidId_ShouldReturn200AndBaseResponse() throws Exception {
            Long id = 1L;
            BaseResponse baseResponse = BaseResponse.builder()
                    .code("200")
                    .status(200)
                    .message("Producto eliminado exitosamente")
                    .build();

            when(serviceI.eliminar(eq(id))).thenReturn(baseResponse);

            mockMvc.perform(delete("/api/producto/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("Producto eliminado exitosamente"));

            verify(serviceI, times(1)).eliminar(eq(id));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 cuando el ID a eliminar no es numérico")
        void eliminar_WhenInvalidIdType_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(delete("/api/producto/invalido"))
                    .andExpect(status().isBadRequest());

            verify(serviceI, never()).eliminar(any());
        }
    }
}
