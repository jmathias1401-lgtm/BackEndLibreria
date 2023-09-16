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
}
