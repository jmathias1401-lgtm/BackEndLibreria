package newfarma.detallecompra;

import newfarma.compra.CompraRepositoryJPA;
import newfarma.model.Compra;
import newfarma.model.Producto;
import newfarma.model.detallecompra;
import newfarma.productos.ProductoRepositoryJPA;
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
@DisplayName("Pruebas de Repositorio - DetalleCompraRepositoryJPA")
class DetalleCompraRepositoryJPATest {

    @Autowired
    private DetalleCompraRepositoryJPA detalleCompraRepositoryJPA;

    @Autowired
    private CompraRepositoryJPA compraRepositoryJPA;

    @Autowired
    private ProductoRepositoryJPA productoRepositoryJPA;

    @Test
    @DisplayName("findByCodigodetallecompra retorna detalles por código")
    void findByCodigodetallecompra_ShouldReturnDetalles() {
        detallecompra dc = new detallecompra();
        dc.setCodigodetallecompra("DC_" + System.currentTimeMillis());
        dc.setUnidades(5);
        dc.setCostounidad(10.0);
        dc.setTotal(50.0);
        detalleCompraRepositoryJPA.save(dc);

        List<detallecompra> result = detalleCompraRepositoryJPA.findByCodigodetallecompra(dc.getCodigodetallecompra());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCodigodetallecompra()).isEqualTo(dc.getCodigodetallecompra());
    }

    @Test
    @DisplayName("existsDetalleCompraByCodigodetallecompra retorna true si existe")
    void existsDetalleCompraByCodigodetallecompra_ShouldReturnTrueWhenExists() {
        detallecompra dc = new detallecompra();
        dc.setCodigodetallecompra("DC_EX_" + System.currentTimeMillis());
        dc.setUnidades(2);
        detalleCompraRepositoryJPA.save(dc);

        boolean exists = detalleCompraRepositoryJPA.existsDetalleCompraByCodigodetallecompra(dc.getCodigodetallecompra());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsDetalleCompraByCodigodetallecompra retorna false si no existe")
    void existsDetalleCompraByCodigodetallecompra_ShouldReturnFalseWhenNotExists() {
        boolean exists = detalleCompraRepositoryJPA.existsDetalleCompraByCodigodetallecompra("DC_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("listDetalleCompraById retorna detalles por id de compra")
    void listDetalleCompraById_ShouldReturnDetalles() {
        Compra c = new Compra();
        c.setSerie("F100");
        c.setCorrelativo("C_DC_" + System.currentTimeMillis());
        c.setFechacompra(new Date());
        c = compraRepositoryJPA.save(c);

        detallecompra dc = new detallecompra();
        dc.setCodigodetallecompra("DC_COMPRA_" + System.currentTimeMillis());
        dc.setCompra(c);
        dc.setUnidades(3);
        detalleCompraRepositoryJPA.save(dc);

        List<detallecompra> list = detalleCompraRepositoryJPA.listDetalleCompraById(c.getIdcompra().intValue());

        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getCompra().getIdcompra()).isEqualTo(c.getIdcompra());
    }

    @Test
    @DisplayName("listDetalleCompraByProductoId retorna costo máximo del producto")
    void listDetalleCompraByProductoId_ShouldReturnMaxCost() {
        Producto prod = new Producto();
        prod.setNombre("Producto Detalle");
        prod.setCodigoproducto("PROD_DC_" + System.currentTimeMillis());
        prod = productoRepositoryJPA.save(prod);

        detallecompra dc = new detallecompra();
        dc.setCodigodetallecompra("DC_PROD_" + System.currentTimeMillis());
        dc.setProducto(prod);
        dc.setCostounidad(25.50);
        detalleCompraRepositoryJPA.save(dc);

        Double maxCost = detalleCompraRepositoryJPA.listDetalleCompraByProductoId(prod.getIdproducto().intValue());

        assertThat(maxCost).isNotNull();
        assertThat(maxCost).isGreaterThanOrEqualTo(25.50);
    }

    @Test
    @DisplayName("CountDetalleCompra retorna conteo de detalles")
    void countDetalleCompra_ShouldReturnCount() {
        Integer count = detalleCompraRepositoryJPA.CountDetalleCompra();

        assertThat(count).isNotNull();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        detallecompra dc = new detallecompra();
        dc.setCodigodetallecompra("DC_CRUD_" + System.currentTimeMillis());
        dc.setCostounidad(12.0);
        dc.setUnidades(4);
        detallecompra saved = detalleCompraRepositoryJPA.save(dc);

        Optional<detallecompra> found = detalleCompraRepositoryJPA.findById(saved.getIddetallecompra());

        assertThat(found).isPresent();
        assertThat(found.get().getCodigodetallecompra()).isEqualTo(dc.getCodigodetallecompra());
    }
}
