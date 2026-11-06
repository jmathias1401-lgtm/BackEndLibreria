package newfarma.persona;

import newfarma.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonaRepositoryJPA extends JpaRepository<Persona, Long> {
    @Query(value = "from Producto p where p.codigoproducto = ?1 ")
    List<Persona> findByNombre(String nombre);

    boolean existsPersonaByDni(Long dni);

    Optional<Persona> findByDni(Long dni);

    boolean existsPersonaByRuc(Long ruc);

    Optional<Persona> findByRuc(Long ruc);
}
