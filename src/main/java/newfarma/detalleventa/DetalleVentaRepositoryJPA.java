package newfarma.detalleventa;

import newfarma.model.detalleventa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleVentaRepositoryJPA extends JpaRepository<detalleventa, Long> {
    @Query(value = "from detalleventa dv where dv.codigodetalleventa = ?1 ")
    public List<detalleventa> findByCodigodetalleventa(String codigo);

    public boolean existsDetalleVentaByCodigodetalleventa(String codigo);

    @Query(value = "select * from detalleventa dv where dv.venta_idventa =?1", nativeQuery = true)
    public List<detalleventa> listDetalleVentaById(Integer id);

    @Query(value = "select count(*) from detalleventa", nativeQuery = true)
    public Integer CountDetalleVenta();

    @Query(value = "SELECT dv.iddetalleventa,dv.codigodetalleventa,dv.costounidad,dv.total,dv.subtotal,dv.unidades,dv.descuentounidad,dv.venta_idventa,dv.producto_idproducto, p.nombre as Nombre  FROM venta v INNER JOIN detalleventa dv ON v.idventa = dv.venta_idventa INNER JOIN producto p ON dv.producto_idproducto = p.idproducto WHERE YEAR(v.fechaventa) = ?2 AND MONTH(v.fechaventa) = ?1", nativeQuery = true)
    public List<detalleventa> ventasMes(int mes, int anio);
}