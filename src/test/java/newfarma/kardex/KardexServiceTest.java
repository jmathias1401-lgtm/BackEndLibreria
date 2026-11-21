package newfarma.kardex;

import jakarta.persistence.EntityNotFoundException;
import newfarma.kardex.dto.KardexListResponse;
import newfarma.kardex.dto.KardexRequestDto;
import newfarma.model.Kardex;
import newfarma.model.Producto;
import newfarma.productos.ProductoRepositoryJPA;
import newfarma.utils.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KardexServiceTest {

    @Mock
    private KardexRepositoryJPA kardexRepositoryJPA;

    @Mock
    private ProductoRepositoryJPA productoRepositoryJPA;

    @InjectMocks
    private KardexService kardexService;

    private Producto producto;
    private Kardex kardex;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setIdproducto(1L);
        producto.setNombre("Cuaderno Stanford 100 Hojas");
        producto.setStock(20);

        kardex = Kardex.builder()
                .idkardex(1L)
                .producto(producto)
                .fecha(LocalDateTime.now())
                .tipoOperacion("CONTEO")
                .motivo("Conteo físico de estante")
                .cantidad(5)
                .stockAnterior(15)
                .stockNuevo(20)
                .usuario("admin")
                .build();
    }

    @Test
    void testListByProducto_ReturnsList() {
        when(kardexRepositoryJPA.findByProductoId(1L)).thenReturn(Arrays.asList(kardex));

        KardexListResponse response = kardexService.listByProducto(1L);

        assertNotNull(response);
        assertEquals(1, response.getTotal());
        assertEquals(1, response.getList().size());
        assertEquals("Conteo físico de estante", response.getList().get(0).getMotivo());
        verify(kardexRepositoryJPA, times(1)).findByProductoId(1L);
    }

    @Test
    void testListByProducto_ReturnsEmpty() {
        when(kardexRepositoryJPA.findByProductoId(2L)).thenReturn(Collections.emptyList());

        KardexListResponse response = kardexService.listByProducto(2L);

        assertNotNull(response);
        assertEquals(0, response.getTotal());
        assertTrue(response.getList().isEmpty());
    }

    @Test
    void testListAuditoria_ReturnsFilteredMovements() {
        when(kardexRepositoryJPA.findAuditoriaFiltrada("SALIDA", "admin", "Cuaderno"))
                .thenReturn(Collections.singletonList(kardex));

        KardexListResponse response = kardexService.listAuditoria("SALIDA", "admin", "Cuaderno");

        assertNotNull(response);
        assertEquals(1, response.getTotal());
        assertEquals("Conteo físico de estante", response.getList().get(0).getMotivo());
        verify(kardexRepositoryJPA, times(1)).findAuditoriaFiltrada("SALIDA", "admin", "Cuaderno");
    }

    @Test
    void testListAuditoria_WithTodos_PassesNullTipoOperacion() {
        when(kardexRepositoryJPA.findAuditoriaFiltrada(null, null, null))
                .thenReturn(Collections.singletonList(kardex));

        KardexListResponse response = kardexService.listAuditoria("TODOS", "  ", "");

        assertNotNull(response);
        assertEquals(1, response.getTotal());
        verify(kardexRepositoryJPA, times(1)).findAuditoriaFiltrada(null, null, null);
    }

    @Test
    void testSave_Success() {
        KardexRequestDto dto = KardexRequestDto.builder()
                .idProducto(1L)
                .tipoOperacion("ENTRADA")
                .motivo("Ingreso por compra")
                .cantidad(10)
                .stockAnterior(20)
                .stockNuevo(30)
                .usuario("supervisor")
                .build();

        when(productoRepositoryJPA.findById(1L)).thenReturn(Optional.of(producto));
        when(kardexRepositoryJPA.save(any(Kardex.class))).thenReturn(kardex);

        BaseResponse response = kardexService.save(dto);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("200 OK", response.getCode());
        verify(kardexRepositoryJPA, times(1)).save(any(Kardex.class));
    }

    @Test
    void testSave_WithDefaultValues() {
        KardexRequestDto dto = KardexRequestDto.builder()
                .idProducto(1L)
                .build();

        when(productoRepositoryJPA.findById(1L)).thenReturn(Optional.of(producto));
        when(kardexRepositoryJPA.save(any(Kardex.class))).thenReturn(kardex);

        BaseResponse response = kardexService.save(dto);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
        verify(kardexRepositoryJPA, times(1)).save(any(Kardex.class));
    }

    @Test
    void testSave_NullProductoId_ThrowsException() {
        KardexRequestDto dto = KardexRequestDto.builder()
                .idProducto(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> kardexService.save(dto));
        verify(kardexRepositoryJPA, never()).save(any(Kardex.class));
    }

    @Test
    void testSave_ProductoNotFound_ThrowsException() {
        KardexRequestDto dto = KardexRequestDto.builder()
                .idProducto(99L)
                .build();

        when(productoRepositoryJPA.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> kardexService.save(dto));
        verify(kardexRepositoryJPA, never()).save(any(Kardex.class));
    }
}

