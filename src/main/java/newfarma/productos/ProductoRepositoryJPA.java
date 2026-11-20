package newfarma.productos;

import newfarma.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepositoryJPA extends JpaRepository<Producto, Long> {
    @Query(value = "from Producto p where p.codigoproducto = ?1 ")
    public List<Producto> findByCodigoproducto(String codigo);

    public boolean existsProductoByCodbarra(String codigo);

    public boolean existsProductoByCodigoproducto(String codigo);

    @Query(value = "select * from producto p where p.idproducto =?1", nativeQuery = true)
    public List<Producto> listProductosById(Integer productId);

    @Query(value = "select count(*) from Producto", nativeQuery = true)
    public Integer CountProduct();

    @Query(value = "SELECT COUNT(CASE WHEN stock <= 5 THEN 1 END), COUNT(CASE WHEN stock = 0 THEN 1 END), COUNT(*) FROM producto", nativeQuery = true)
    public List<Object[]> getStockAlertSummary();

}
