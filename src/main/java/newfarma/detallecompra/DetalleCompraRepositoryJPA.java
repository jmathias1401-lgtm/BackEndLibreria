package newfarma.detallecompra;

import newfarma.model.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleCompraRepositoryJPA extends JpaRepository<DetalleCompra,Long> {
    @Query(value = "from DetalleCompra dc where dc.codigodetallecompra = ?1 ")
    public List<DetalleCompra> findByCodigodetallecompra(String codigo);

    public boolean existsDetalleCompraByCodigodetallecompra(String codigo);

    @Query( value = "select * from DetalleCompra dc where dc.compra_idcompra =?1",  nativeQuery = true)
    public List<DetalleCompra> listDetalleCompraById(Integer id);

    @Query(value = "select max(d.costounidad) from detallecompra d where d.producto_idproducto=?1",nativeQuery = true)
    public Double listDetalleCompraByProductoId(Integer id);

    @Query(value = "select count(*) from DetalleCompra",nativeQuery = true)
    public Integer CountDetalleCompra();
}