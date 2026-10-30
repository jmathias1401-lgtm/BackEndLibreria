package newfarma.productos;

import newfarma.model.Producto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - ProductoRepositoryJPA")
class ProductoRepositoryJPATest {

    @Autowired
    private ProductoRepositoryJPA productoRepositoryJPA;

    @Test
    @DisplayName("findByCodigoproducto retorna producto por código")
    void findByCodigoproducto_ShouldReturnProducto() {
        Producto p = new Producto();
        p.setNombre("Prod Test");
        p.setCodigoproducto("COD_" + System.currentTimeMillis());
        productoRepositoryJPA.save(p);

        List<Producto> list = productoRepositoryJPA.findByCodigoproducto(p.getCodigoproducto());

        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getCodigoproducto()).isEqualTo(p.getCodigoproducto());
    }

    @Test
    @DisplayName("existsProductoByCodbarra retorna true si existe por código de barra")
    void existsProductoByCodbarra_ShouldReturnTrueWhenExists() {
        Producto p = new Producto();
        p.setNombre("Prod Barcode");
        p.setCodbarra("BAR_" + System.currentTimeMillis());
        productoRepositoryJPA.save(p);

        boolean exists = productoRepositoryJPA.existsProductoByCodbarra(p.getCodbarra());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsProductoByCodbarra retorna false si no existe")
    void existsProductoByCodbarra_ShouldReturnFalseWhenNotExists() {
        boolean exists = productoRepositoryJPA.existsProductoByCodbarra("BAR_INEXISTENTE_99999");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("existsProductoByCodigoproducto retorna true si existe")
    void existsProductoByCodigoproducto_ShouldReturnTrueWhenExists() {
        Producto p = new Producto();
        p.setNombre("Prod Code");
        p.setCodigoproducto("CP_" + System.currentTimeMillis());
        productoRepositoryJPA.save(p);

        boolean exists = productoRepositoryJPA.existsProductoByCodigoproducto(p.getCodigoproducto());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("listProductosById retorna producto por ID entero")
    void listProductosById_ShouldReturnProducto() {
        Producto p = new Producto();
        p.setNombre("Prod ListId");
        Producto saved = productoRepositoryJPA.save(p);

        List<Producto> list = productoRepositoryJPA.listProductosById(saved.getIdproducto().intValue());

        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getIdproducto()).isEqualTo(saved.getIdproducto());
    }

    @Test
    @DisplayName("CountProduct retorna conteo de productos")
    void countProduct_ShouldReturnCount() {
        Integer count = productoRepositoryJPA.CountProduct();

        assertThat(count).isNotNull();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("save y findById persisten y recuperan correctamente")
    void saveAndFindById_ShouldWork() {
        Producto p = new Producto();
        p.setNombre("Prod CRUD");
        p.setPrecioventa(9.99);
        Producto saved = productoRepositoryJPA.save(p);

        Optional<Producto> found = productoRepositoryJPA.findById(saved.getIdproducto());

        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Prod CRUD");
        assertThat(found.get().getPrecioventa()).isEqualTo(9.99);
    }
}
