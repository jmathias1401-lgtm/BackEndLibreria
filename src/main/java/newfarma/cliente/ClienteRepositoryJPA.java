package newfarma.cliente;

import newfarma.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepositoryJPA extends JpaRepository<Cliente, Long> {

    boolean existsClienteByIdcliente(Integer id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c WHERE c.persona.idpersona = :idPersona")
    boolean existsClienteByPersonaId(@Param("idPersona") Long idPersona);
}
