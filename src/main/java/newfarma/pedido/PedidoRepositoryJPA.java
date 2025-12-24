package newfarma.pedido;

import newfarma.model.Pedido;
import newfarma.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoRepositoryJPA extends JpaRepository<Pedido,Long> {
    @Query(value = "select p.codigopedido from Pedido p order by 1 desc limit 1 ")
    public List<String> lastPedido();
}