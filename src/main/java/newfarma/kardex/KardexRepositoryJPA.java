package newfarma.kardex;

import newfarma.model.Kardex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KardexRepositoryJPA extends JpaRepository<Kardex, Long> {
    @Query("SELECT k FROM Kardex k WHERE k.producto.idproducto = :idProducto ORDER BY k.fecha DESC")
    List<Kardex> findByProductoId(@Param("idProducto") Long idProducto);

    @Query("SELECT k FROM Kardex k JOIN FETCH k.producto p " +
            "WHERE (:tipoOperacion IS NULL OR :tipoOperacion = '' OR k.tipoOperacion = :tipoOperacion) " +
            "AND (:usuario IS NULL OR :usuario = '' OR LOWER(k.usuario) LIKE LOWER(CONCAT('%', :usuario, '%'))) " +
            "AND (:search IS NULL OR :search = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(k.motivo) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY k.fecha DESC")
    List<Kardex> findAuditoriaFiltrada(@Param("tipoOperacion") String tipoOperacion,
                                      @Param("usuario") String usuario,
                                      @Param("search") String search);
}

