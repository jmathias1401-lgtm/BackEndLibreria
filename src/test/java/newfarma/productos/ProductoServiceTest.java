package newfarma.productos;

import jakarta.persistence.EntityNotFoundException;
import newfarma.laboratorio.LaboratorioService;
import newfarma.model.Producto;
import newfarma.presentacion.PresentacionService;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
import newfarma.unidadmedida.UnidadMedidaService;
import newfarma.utils.BaseResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - ProductoService")
class ProductoServiceTest {
    @Mock private ProductoRepository repository;
    @Mock private ProductoRepositoryJPA productoRepositoryJPA;
    @Mock private LaboratorioService laboratorioService;
    @Mock private PresentacionService presentacionService;
    @Mock private UnidadMedidaService unidadMedidaService;
    @InjectMocks private ProductoService service;
    private Producto sample;

    @BeforeEach void setUp() {
        sample = new Producto();
        sample.setIdproducto(1L);
        sample.setCodigoproducto("MED-001");
        sample.setNombre("PARACETAMOL");
        sample.setVencimiento("2025-12-31");
        sample.setStock(100);
    }

    @Nested @DisplayName("list()") class ListTests {
        @Test @DisplayName("Retorna lista y total")
        void list_ShouldReturnResponse() {
            ProductListRequest p = new ProductListRequest(); p.setPage(1); p.setXpage(10);
            when(repository.list(any(), eq("L"))).thenReturn(List.of(sample));
            when(repository.list(any(), eq("T"))).thenReturn(1L);
            ProductListResponse r = service.list(p);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("ProductListById(int)") class ListByIdTests {
        @Test @DisplayName("Retorna productos por id")
        void productListById_ShouldReturnList() {
            when(productoRepositoryJPA.listProductosById(1)).thenReturn(List.of(sample));
            ProductListResponse r = service.ProductListById(1);
            assertThat(r.getList()).hasSize(1);
        }
    }
    @Nested @DisplayName("save()") class SaveTests {
        @Test @DisplayName("Actualiza producto existente sin imagen")
        void save_WhenIdNotNull_ShouldUpdate() {
            sample.setVencimiento("2025-12-31");
            when(productoRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(productoRepositoryJPA.save(any())).thenReturn(sample);
            MockMultipartFile emptyImage = new MockMultipartFile("imagen", new byte[0]);
            assertThat(service.save(sample, emptyImage).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nuevo producto cuando codigo no existe")
        void save_WhenNewCode_ShouldCreate() {
            Producto nuevo = new Producto();
            nuevo.setCodigoproducto("MED-002"); nuevo.setNombre("IBUPROFENO");
            nuevo.setVencimiento("2026-01-01"); nuevo.setImagen_path("img.jpg");
            when(productoRepositoryJPA.existsProductoByCodigoproducto("MED-002")).thenReturn(false);
            when(productoRepositoryJPA.save(any())).thenReturn(nuevo);
            MockMultipartFile emptyImage = new MockMultipartFile("imagen", new byte[0]);
            assertThat(service.save(nuevo, emptyImage).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error 500 si codigo ya existe")
        void save_WhenDuplicate_ShouldReturnError() {
            Producto dup = new Producto(); dup.setCodigoproducto("MED-001"); dup.setVencimiento("2025-12-31");
            when(productoRepositoryJPA.existsProductoByCodigoproducto("MED-001")).thenReturn(true);
            MockMultipartFile emptyImage = new MockMultipartFile("imagen", new byte[0]);
            assertThat(service.save(dup, emptyImage).getStatus()).isEqualTo(500);
        }
    }
    @Nested @DisplayName("eliminar()") class EliminarTests {
        @Test @DisplayName("Elimina producto existente")
        void eliminar_WhenExists_ShouldDelete() {
            when(productoRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            assertThat(service.eliminar(1L).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Lanza excepcion si no existe")
        void eliminar_WhenNotFound_ShouldThrow() {
            when(productoRepositoryJPA.findById(99L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.eliminar(99L));
        }
    }
    @Nested @DisplayName("CountProducto()") class CountTests {
        @Test @DisplayName("Retorna conteo de productos")
        void count_ShouldReturnCount() {
            when(productoRepositoryJPA.CountProduct()).thenReturn(50);
            assertThat(service.CountProducto()).isEqualTo(50);
        }
    }
}