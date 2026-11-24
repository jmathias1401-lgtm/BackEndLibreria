package newfarma.venta;

import newfarma.model.Persona;
import newfarma.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VentaRepositoryJPA extends JpaRepository<Venta,Long> {
    @Query(value = "from Producto p where p.codigoproducto = ?1 ")
    public List<Venta> findByNombre(String nombre);
    public boolean existsVentaByCorrelativo(String dni);

    @Query(value = "SELECT COALESCE(SUM(costoventa), 0), COUNT(*) FROM venta WHERE DATE(fechaventa) = CURRENT_DATE", nativeQuery = true)
    public List<Object[]> getVentasHoySummary();

    @Query(value = "SELECT COALESCE(SUM(costoventa), 0), COUNT(*) FROM venta WHERE YEAR(fechaventa) = YEAR(CURRENT_DATE) AND MONTH(fechaventa) = MONTH(CURRENT_DATE)", nativeQuery = true)
    public List<Object[]> getVentasMesSummary();

    @Query(value = "SELECT HOUR(fechaventa) as h, COUNT(*) as c, COALESCE(SUM(costoventa), 0) as s FROM venta WHERE DATE(fechaventa) = CURRENT_DATE GROUP BY HOUR(fechaventa) ORDER BY h ASC", nativeQuery = true)
    public List<Object[]> getVentasPorHoraHoy();

    @Query(value = "SELECT COALESCE(tc.nombrecomprobante, 'TICKET') as tc_nombre, COUNT(*) as cnt, COALESCE(SUM(v.costoventa), 0) as sm " +
            "FROM venta v LEFT JOIN tipocomprobante tc ON v.tipocomprobante_idtipocomprobante = tc.idtipocomprobante " +
            "WHERE YEAR(v.fechaventa) = YEAR(CURRENT_DATE) AND MONTH(v.fechaventa) = MONTH(CURRENT_DATE) " +
            "GROUP BY tc.nombrecomprobante", nativeQuery = true)
    public List<Object[]> getVentasPorComprobanteMes();

    @Query(value = "SELECT e.idempleado, TRIM(CONCAT(COALESCE(pe.nombre, ''), ' ', COALESCE(pe.paterno, ''))) as vend, COUNT(*) as cnt, COALESCE(SUM(v.costoventa), 0) as sm " +
            "FROM venta v " +
            "LEFT JOIN empleado e ON v.empleado_idempleado = e.idempleado " +
            "LEFT JOIN persona pe ON e.persona_idpersona = pe.idpersona " +
            "WHERE YEAR(v.fechaventa) = YEAR(CURRENT_DATE) AND MONTH(v.fechaventa) = MONTH(CURRENT_DATE) " +
            "GROUP BY e.idempleado, pe.nombre, pe.paterno ORDER BY sm DESC LIMIT 5", nativeQuery = true)
    public List<Object[]> getTopVendedoresMes();

    @Query(value = "SELECT " +
            "e.idempleado, " +
            "TRIM(CONCAT(COALESCE(p.nombre, ''), ' ', COALESCE(p.paterno, ''), ' ', COALESCE(p.materno, ''))) as empleado, " +
            "COALESCE(SUM(CASE WHEN DATE(v.fechaventa) = CURRENT_DATE THEN v.costoventa ELSE 0 END), 0) as ventas_hoy, " +
            "COALESCE(SUM(CASE WHEN v.fechaventa >= DATE_SUB(CURRENT_DATE, INTERVAL WEEKDAY(CURRENT_DATE) DAY) THEN v.costoventa ELSE 0 END), 0) as ventas_semana, " +
            "COALESCE(SUM(CASE WHEN YEAR(v.fechaventa) = YEAR(CURRENT_DATE) AND MONTH(v.fechaventa) = MONTH(CURRENT_DATE) THEN v.costoventa ELSE 0 END), 0) as ventas_mes, " +
            "COALESCE(SUM(CASE WHEN YEAR(v.fechaventa) = YEAR(CURRENT_DATE) THEN v.costoventa ELSE 0 END), 0) as ventas_anio, " +
            "COUNT(v.idventa) as total_transacciones " +
            "FROM empleado e " +
            "JOIN persona p ON e.persona_idpersona = p.idpersona " +
            "LEFT JOIN venta v ON v.empleado_idempleado = e.idempleado " +
            "GROUP BY e.idempleado, p.nombre, p.paterno, p.materno " +
            "ORDER BY ventas_mes DESC, ventas_anio DESC", nativeQuery = true)
    public List<Object[]> getRendimientoEmpleadosAgregados();
}
