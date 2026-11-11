package newfarma.productos;

import jakarta.persistence.EntityNotFoundException;
import newfarma.laboratorio.LaboratorioService;
import newfarma.model.Producto;
import newfarma.presentacion.PresentacionService;
import newfarma.productos.dto.ProductListRequest;
import newfarma.productos.dto.ProductListResponse;
import newfarma.productos.dto.ProductoRequestDto;
import newfarma.unidadmedida.UnidadMedidaService;
import newfarma.utils.BaseResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
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
        ReflectionTestUtils.setField(service, "rutaBaseImagenes", "target/test-images");
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
            ProductoRequestDto dto = new ProductoRequestDto();
            dto.setIdproducto(1L);
            dto.setCodigoproducto("MED-001");
            dto.setNombre("PARACETAMOL");
            dto.setVencimiento("2025-12-31");
            dto.setStock(100);

            when(productoRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(productoRepositoryJPA.save(any())).thenReturn(sample);
            MockMultipartFile emptyImage = new MockMultipartFile("imagen", new byte[0]);
            assertThat(service.save(dto, emptyImage).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Actualiza producto existente con relaciones de presentacion, unidad y laboratorio")
        void save_WhenRelationsPresent_ShouldUpdateRelations() {
            newfarma.presentacion.PresentacionRepositoryJPA presJPA = mock(newfarma.presentacion.PresentacionRepositoryJPA.class);
            newfarma.unidadmedida.UnidadMedidaRepositoryJPA umJPA = mock(newfarma.unidadmedida.UnidadMedidaRepositoryJPA.class);
            newfarma.laboratorio.LaboratorioRepositoryJPA labJPA = mock(newfarma.laboratorio.LaboratorioRepositoryJPA.class);

            org.springframework.test.util.ReflectionTestUtils.setField(service, "presentacionRepositoryJPA", presJPA);
            org.springframework.test.util.ReflectionTestUtils.setField(service, "unidadMedidaRepositoryJPA", umJPA);
            org.springframework.test.util.ReflectionTestUtils.setField(service, "laboratorioRepositoryJPA", labJPA);

            ProductoRequestDto dto = new ProductoRequestDto();
            dto.setIdproducto(1L);
            dto.setCodigoproducto("MED-001");
            dto.setNombre("PARACETAMOL");
            dto.setVencimiento("2025-12-31");
            dto.setIdpresentacion(1L);
            dto.setIdunidadmedida(1);
            dto.setIdlaboratorio(1L);

            dto.unpackLaboratorio(Map.of("idlaboratorio", 1L));
            dto.unpackPresentacion(Map.of("idpresentacion", 1L));
            dto.unpackUnidadMedida(Map.of("idunidadmedida", 1));
            dto.setIdLaboratorioCamel(1L);
            dto.setIdPresentacionCamel(1L);
            dto.setIdUnidadMedidaCamel(1);

            when(productoRepositoryJPA.findById(1L)).thenReturn(Optional.of(sample));
            when(presJPA.findById(1L)).thenReturn(Optional.of(new newfarma.model.Presentacion()));
            when(umJPA.findById(1)).thenReturn(Optional.of(new newfarma.model.UnidadMedida()));
            when(labJPA.findById(1L)).thenReturn(Optional.of(new newfarma.model.Laboratorio()));
            when(productoRepositoryJPA.save(any())).thenReturn(sample);

            MockMultipartFile emptyImage = new MockMultipartFile("imagen", new byte[0]);
            assertThat(service.save(dto, emptyImage).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Crea nuevo producto cuando codigo no existe")
        void save_WhenNewCode_ShouldCreate() {
            ProductoRequestDto nuevo = new ProductoRequestDto();
            nuevo.setCodigoproducto("MED-002"); nuevo.setNombre("IBUPROFENO");
            nuevo.setVencimiento("2026-01-01"); nuevo.setImagen_path("img.jpg");
            when(productoRepositoryJPA.existsProductoByCodigoproducto("MED-002")).thenReturn(false);
            when(productoRepositoryJPA.save(any())).thenReturn(sample);
            MockMultipartFile emptyImage = new MockMultipartFile("imagen", new byte[0]);
            assertThat(service.save(nuevo, emptyImage).getStatus()).isEqualTo(200);
        }
        @Test @DisplayName("Error 500 si codigo ya existe")
        void save_WhenDuplicate_ShouldReturnError() {
            ProductoRequestDto dup = new ProductoRequestDto(); dup.setCodigoproducto("MED-001"); dup.setVencimiento("2025-12-31");
            when(productoRepositoryJPA.existsProductoByCodigoproducto("MED-001")).thenReturn(true);
            MockMultipartFile emptyImage = new MockMultipartFile("imagen", new byte[0]);
            assertThat(service.save(dup, emptyImage).getStatus()).isEqualTo(500);
        }
        @Test @DisplayName("Guarda producto con imagen sanitizando intento de path traversal")
        void save_WithImageAndPathTraversal_ShouldSanitizeAndSave() {
            ProductoRequestDto nuevo = new ProductoRequestDto();
            nuevo.setCodigoproducto("MED-003"); nuevo.setNombre("ASPIRINA");
            nuevo.setVencimiento("2026-05-01");
            nuevo.setImagen_path("../../etc/malicious.png");

            when(productoRepositoryJPA.existsProductoByCodigoproducto("MED-003")).thenReturn(false);
            when(productoRepositoryJPA.save(any())).thenReturn(sample);

            MockMultipartFile image = new MockMultipartFile("imagen", "test.png", "image/png", "datos".getBytes());
            assertThat(service.save(nuevo, image).getStatus()).isEqualTo(200);
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