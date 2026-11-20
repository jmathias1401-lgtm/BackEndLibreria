package newfarma.kardex;

import newfarma.model.Kardex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KardexRepositoryJPA extends JpaRepository<Kardex, Long> {
    @Query("SELECT k FROM Kardex k WHERE k.producto.idproducto = :idProducto ORDER BY k.fecha DESC")
    List<Kardex> findByProductoId(@Param("idProducto") Long idProducto);
}

