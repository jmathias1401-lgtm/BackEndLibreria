package newfarma.cliente;


import newfarma.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepositoryJPA extends JpaRepository<Cliente,Long> {

    public boolean existsClienteByIdcliente(Integer id);
}
