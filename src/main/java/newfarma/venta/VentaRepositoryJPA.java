package newfarma.venta;

import newfarma.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VentaRepositoryJPA extends JpaRepository<Persona,Long> {
    @Query(value = "from Producto p where p.codigoproducto = ?1 ")
    public List<Persona> findByNombre(String nombre);
    public boolean existsPersonaByDni(Long dni);
}
