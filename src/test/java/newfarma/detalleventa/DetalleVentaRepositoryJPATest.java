package newfarma.detalleventa;

import newfarma.model.Producto;
import newfarma.model.Venta;
import newfarma.model.detalleventa;
import newfarma.productos.ProductoRepositoryJPA;
import newfarma.venta.VentaRepositoryJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - DetalleVentaRepositoryJPA")
class DetalleVentaRepositoryJPATest {

    @Autowired
    private DetalleVentaRepositoryJPA detalleVentaRepositoryJPA;

    @Autowired
    private VentaRepositoryJPA ventaRepositoryJPA;

    @Autowired
    private ProductoRepositoryJPA productoRepositoryJPA;

    @Test
    @DisplayName("findByCodigodetalleventa retorna lista por código")
    void findByCodigodetalleventa_ShouldReturnDetalles() {
        detalleventa dv = new detalleventa();
        dv.setCodigodetalleventa("DV_" + System.currentTimeMillis());
        dv.setUnidades(2);
        dv.setCostounidad(20.0);
        dv.setTotal(40.0);
        detalleVentaRepositoryJPA.save(dv);

        List<detalleventa> result = detalleVentaRepositoryJPA.findByCodigodetalleventa(dv.getCodigodetalleventa());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCodigodetalleventa()).isEqualTo(dv.getCodigodetalleventa());
    }

    @Test
    @DisplayName("existsDetalleVentaByCodigodetalleventa retorna true si existe")
    void existsDetalleVentaByCodigodetalleventa_ShouldReturnTrueWhenExists() {
        detalleventa dv = new detalleventa();
        dv.setCodigodetalleventa("DV_EX_" + System.currentTimeMillis());
        dv.setUnidades(1);
        detalleVentaRepositoryJPA.save(dv);

        boolean exists = detalleVentaRepositoryJPA.existsDetalleVentaByCodigodetalleventa(dv.getCodigodetalleventa());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsDetalleVentaByCodigodetalleventa retorna false si no existe")
    void existsDetalleVentaByCodigodetalleventa_ShouldReturnFalseWhenNotExists() {
        boolean exists = detalleVentaRepositoryJPA.existsDetalleVentaByCodigodetalleventa("DV_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("listDetalleVentaById retorna detalles de la venta")
    void listDetalleVentaById_ShouldReturnDetalles() {
        Venta v = new Venta();
        v.setSerie("B010");
        v.setCorrelativo("V_DET_" + System.currentTimeMillis());
        v.setFechaventa(new Date());
        v = ventaRepositoryJPA.save(v);

        detalleventa dv = new detalleventa();
        dv.setCodigodetalleventa("DV_VTA_" + System.currentTimeMillis());
        dv.setVenta(v);
        dv.setUnidades(3);
        detalleVentaRepositoryJPA.save(dv);

        List<detalleventa> list = detalleVentaRepositoryJPA.listDetalleVentaById(v.getIdventa().intValue());

        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getVenta().getIdventa()).isEqualTo(v.getIdventa());
    }

    @Test
    @DisplayName("CountDetalleVenta retorna conteo de detalles")
    void countDetalleVenta_ShouldReturnCount() {
        Integer count = detalleVentaRepositoryJPA.CountDetalleVenta();

        assertThat(count).isNotNull();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("ventasMes retorna lista de detalles para mes y año consultados")
    void ventasMes_ShouldExecuteNativeQuery() {
        // Query execution test for native query
        List<detalleventa> list = detalleVentaRepositoryJPA.ventasMes(10, 2026);

        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        detalleventa dv = new detalleventa();
        dv.setCodigodetalleventa("DV_CRUD_" + System.currentTimeMillis());
        dv.setCostounidad(30.0);
        dv.setUnidades(2);
        detalleventa saved = detalleVentaRepositoryJPA.save(dv);

        Optional<detalleventa> found = detalleVentaRepositoryJPA.findById(saved.getIddetalleventa());

        assertThat(found).isPresent();
        assertThat(found.get().getCodigodetalleventa()).isEqualTo(dv.getCodigodetalleventa());
    }
}
