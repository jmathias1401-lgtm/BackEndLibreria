package newfarma.detalleventa;

import newfarma.model.detalleventa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleVentaRepositoryJPA extends JpaRepository<detalleventa,Long> {
    @Query(value = "from detalleventa dv where dv.codigodetalleventa = ?1 ")
    public List<detalleventa> findByCodigodetalleventa(String codigo);

    public boolean existsDetalleVentaByCodigodetalleventa(String codigo);

    @Query( value = "select * from detalleventa dv where dv.venta_idventa =?1",  nativeQuery = true)
    public List<detalleventa> listDetalleVentaById(Integer id);

    @Query(value = "select count(*) from detalleventa",nativeQuery = true)
    public Integer CountDetalleVenta();
}