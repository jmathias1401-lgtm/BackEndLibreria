package newfarma.detallecompra;

import newfarma.model.detallecompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleCompraRepositoryJPA extends JpaRepository<detallecompra,Long> {
    @Query(value = "from detallecompra dc where dc.codigodetallecompra = ?1 ")
    public List<detallecompra> findByCodigodetallecompra(String codigo);

    public boolean existsDetalleCompraByCodigodetallecompra(String codigo);

    @Query( value = "select * from detallecompra dc where dc.compra_idcompra =?1",  nativeQuery = true)
    public List<detallecompra> listDetalleCompraById(Integer id);

    @Query(value = "select max(d.costounidad) from detallecompra d where d.producto_idproducto=?1",nativeQuery = true)
    public Double listDetalleCompraByProductoId(Integer id);

    @Query(value = "select count(*) from detallecompra",nativeQuery = true)
    public Integer CountDetalleCompra();
}