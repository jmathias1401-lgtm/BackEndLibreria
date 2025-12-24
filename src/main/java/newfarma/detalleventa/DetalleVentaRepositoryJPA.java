package newfarma.detalleventa;

import newfarma.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleVentaRepositoryJPA extends JpaRepository<DetalleVenta,Long> {
    @Query(value = "from DetalleVenta dv where dv.codigodetalleventa = ?1 ")
    public List<DetalleVenta> findByCodigodetalleventa(String codigo);

    public boolean existsDetalleVentaByCodigodetalleventa(String codigo);

    @Query( value = "select * from DetalleVenta dv where dv.venta_idventa =?1",  nativeQuery = true)
    public List<DetalleVenta> listDetalleVentaById(Integer id);

    @Query(value = "select count(*) from DetalleVenta",nativeQuery = true)
    public Integer CountDetalleVenta();
}