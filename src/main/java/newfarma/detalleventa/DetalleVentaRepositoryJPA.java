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

    @Query(value = "SELECT dv.iddetalleventa, v.fechaventa, v.serie, v.correlativo, " +
            "COALESCE(tc.nombrecomprobante, 'TICKET') AS comprobante, " +
            "dv.unidades, dv.costounidad AS precio_unitario, dv.total, " +
            "TRIM(CONCAT(COALESCE(pe.nombre, ''), ' ', COALESCE(pe.paterno, ''))) AS vendedor, " +
            "TRIM(CONCAT(COALESCE(pc.nombre, ''), ' ', COALESCE(pc.paterno, ''))) AS cliente " +
            "FROM detalleventa dv " +
            "JOIN venta v ON dv.venta_idventa = v.idventa " +
            "LEFT JOIN empleado e ON v.empleado_idempleado = e.idempleado " +
            "LEFT JOIN persona pe ON e.persona_idpersona = pe.idpersona " +
            "LEFT JOIN cliente c ON v.cliente_idcliente = c.idcliente " +
            "LEFT JOIN persona pc ON c.persona_idpersona = pc.idpersona " +
            "LEFT JOIN tipocomprobante tc ON v.tipocomprobante_idtipocomprobante = tc.idtipocomprobante " +
            "WHERE dv.producto_idproducto = ?1 " +
            "ORDER BY v.fechaventa DESC", nativeQuery = true)
    public List<Object[]> findTrazabilidadVentasByProducto(Long idProducto);
}